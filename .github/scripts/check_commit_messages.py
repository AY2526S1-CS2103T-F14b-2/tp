import subprocess
import re
import sys
from textwrap import dedent

# === Get PR commit messages ===
try:
    commits = subprocess.check_output(
        ["git", "log", "--format=%H %s%n%b", "origin/main..HEAD"], text=True
    ).strip()
except subprocess.CalledProcessError as e:
    print("❌ Failed to read commit history:", e)
    sys.exit(1)

if not commits:
    print("⚠️ No new commits found compared to main.")
    sys.exit(0)

print("🔍 Checking commit messages...\n")

# === Define rules ===
MAX_SUBJECT_LENGTH = 72
IMPERATIVE_HINTS = [
    "add", "remove", "fix", "update", "create", "delete", "refactor", "rename",
    "move", "improve", "implement", "change", "merge"
]

errors_found = False

# === Process each commit ===
for commit_block in commits.split("\n\ncommit "):
    lines = commit_block.strip().split("\n", 1)
    if not lines:
        continue

    first_line = lines[0].strip()
    if " " not in first_line:
        continue
    commit_hash, subject = first_line.split(" ", 1)

    print(f"🧩 Checking commit {commit_hash[:7]}: {subject}")

    # === Rule 1: Subject line length ===
    if len(subject) > MAX_SUBJECT_LENGTH:
        print(f"  ❌ Subject too long ({len(subject)} chars, max {MAX_SUBJECT_LENGTH})")
        errors_found = True

    # === Rule 2: Capitalized subject ===
    if not subject[0].isupper():
        print("  ❌ Subject must start with a capital letter")
        errors_found = True

    # === Rule 3: Imperative mood (rough heuristic) ===
    first_word = re.split(r"[\s:]", subject)[0].lower()
    if first_word not in IMPERATIVE_HINTS:
        print(f"  ⚠️ Subject may not be imperative (‘{first_word}’ not common verb)")
        # Not fatal — warning only

    # === Rule 4: No trailing period ===
    if subject.endswith("."):
        print("  ❌ Subject must not end with a period (.)")
        errors_found = True

    # === Rule 5: Optional scope format ===
    if ":" in subject:
        prefix, rest = subject.split(":", 1)
        if not re.match(r"^[\w\s\-]+$", prefix):
            print("  ⚠️ Suspicious scope/category format before ':'")
    
    # === Body Checks ===
    try:
        body = lines[1].strip()
    except IndexError:
        body = ""

    if body:
        # Rule 6: Ensure blank line separation
        if not "\n\n" in commit_block:
            print("  ❌ Missing blank line between subject and body")
            errors_found = True

        # Rule 7: Body line length
        for l in body.splitlines():
            if len(l) > 72:
                print(f"  ❌ Body line exceeds 72 characters: '{l[:50]}...'")
                errors_found = True

print("\n✅ Commit message check completed.")
if errors_found:
    print("\n❌ Some commit messages do not follow SE-EDU conventions.")
    sys.exit(1)
else:
    print("\n🎉 All commit messages follow SE-EDU conventions!")
