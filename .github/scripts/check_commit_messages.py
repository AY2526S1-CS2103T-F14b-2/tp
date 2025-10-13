import os
import subprocess
import re
import sys

FIELD_SEPARATOR = "\x1f"
RECORD_SEPARATOR = "\x1e"


def ensure_ref_available(ref: str) -> None:
    """Ensure the given git ref exists locally, fetching it when necessary."""
    try:
        subprocess.check_output(
            ["git", "rev-parse", "--verify", ref],
            stderr=subprocess.STDOUT,
        )
    except subprocess.CalledProcessError:
        remote = "origin"
        fetch_target = ref
        if ref.startswith("origin/"):
            fetch_target = ref.split("/", 1)[1]
        print(f"Fetching {ref} from {remote}...")
        subprocess.check_call(["git", "fetch", remote, fetch_target])


def determine_base_ref() -> str:
    """Determine which ref to compare against HEAD for commit message checks."""
    base_ref = os.getenv("COMMIT_BASE_REF")
    if base_ref:
        return base_ref

    github_base_ref = os.getenv("GITHUB_BASE_REF")
    if github_base_ref:
        return (
            f"origin/{github_base_ref}"
            if not github_base_ref.startswith("origin/")
            else github_base_ref
        )

    default_branch = os.getenv("GITHUB_DEFAULT_BRANCH", "main")
    return (
        f"origin/{default_branch}"
        if not default_branch.startswith("origin/")
        else default_branch
    )


BASE_REF = determine_base_ref()

try:
    ensure_ref_available(BASE_REF)
    raw_log = subprocess.check_output(
        [
            "git",
            "log",
            f"{BASE_REF}..HEAD",
            f"--pretty=format:%H{FIELD_SEPARATOR}%B{RECORD_SEPARATOR}",
        ],
        text=True,
    )
except subprocess.CalledProcessError as e:
    print("Failed to read commit history:", e)
    sys.exit(1)

entries = [entry for entry in raw_log.split(RECORD_SEPARATOR) if entry.strip()]

if not entries:
    print(f"No new commits found compared to {BASE_REF}.")
    sys.exit(0)

print("Checking commit messages...\n")

# === Define rules ===
MAX_SUBJECT_LENGTH = 72
IMPERATIVE_HINTS = [
    "add",
    "remove",
    "fix",
    "update",
    "create",
    "delete",
    "refactor",
    "rename",
    "move",
    "improve",
    "implement",
    "change",
    "merge",
]
MERGE_SUBJECT_PATTERN = re.compile(r"^Merge\b", re.IGNORECASE)

errors_found = False

for entry in entries:
    try:
        _, full_message = entry.split(FIELD_SEPARATOR, 1)
    except ValueError:
        continue

    full_message = full_message.rstrip("\n")
    if not full_message:
        continue

    message_lines = full_message.splitlines()
    if not message_lines:
        continue

    subject = message_lines[0].strip()
    body_lines = message_lines[1:]

    if MERGE_SUBJECT_PATTERN.match(subject):
        print(f"Skipping merge commit: {subject}")
        continue

    print(f"Checking: {subject}")

    if subject and len(subject) > MAX_SUBJECT_LENGTH:
        print(f"Subject too long ({len(subject)} chars, max {MAX_SUBJECT_LENGTH})")
        errors_found = True

    if subject and not subject[0].isupper():
        print("Subject must start with a capital letter")
        errors_found = True

    first_word = re.split(r"[\s:]", subject)[0].lower() if subject else ""
    if first_word and first_word not in IMPERATIVE_HINTS:
        print(f"Subject may not be imperative (‘{first_word}’ not common verb)")

    if subject.endswith("."):
        print("Subject must not end with a period (.)")
        errors_found = True

    if ":" in subject:
        prefix, _ = subject.split(":", 1)
        if not re.match(r"^[\w\s\-]+$", prefix):
            print("Suspicious scope/category format before ':'")

    body_has_content = any(line.strip() for line in body_lines)
    displayed_body_lines = []

    if body_has_content:
        first_body_line = body_lines[0] if body_lines else ""
        if first_body_line.strip() != "":
            print("Missing blank line between subject and body")
            errors_found = True
            displayed_body_lines = body_lines
        else:
            displayed_body_lines = body_lines[1:]

    for line in displayed_body_lines:
        if len(line) > 72:
            print(f"Body line exceeds 72 characters: '{line[:50]}...'")
            errors_found = True

print("Commit message check completed.")
if errors_found:
    print("Some commit messages do not follow SE-EDU conventions.")
    sys.exit(1)
else:
    print("All commit messages follow SE-EDU conventions!")
