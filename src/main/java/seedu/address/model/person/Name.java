package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Person's name in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidName(String)}
 */
public class Name {

    public static final String BLANK_NAME =
            "Name cannot be blank";

    public static final String INVALID_CHARS =
            "Name contains invalid characters. "
                    + "Only letters, hyphen (-), slash (/) and comma (,) are allowed.";

    public static final String MESSAGE_CONSTRAINTS =
            "String is in invalid format";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "^[A-Za-z](?:[A-Za-z ,/\\-]*[A-Za-z])?$";



    public final String fullName;

    /**
     * Constructs a {@code Name}.
     *
     * @param name A valid name.
     */
    public Name(String name) {
        requireNonNull(name);
        final String trimmedName = name.trim();
        checkArgument(isValidName(name), INVALID_CHARS);
        fullName = formatName(trimmedName).trim();
    }

    private static String formatName(String s) {
        if (s.isEmpty()) {
            return s;
        }
        final String delimiters = " -/,";
        StringBuilder sb = new StringBuilder();
        boolean capitaliseNextLetter = true;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (delimiters.indexOf(c) >= 0) {
                sb.append(c);
                capitaliseNextLetter = true;
            } else {
                sb.append(capitaliseNextLetter ? Character.toTitleCase(c) : Character.toLowerCase(c));
                capitaliseNextLetter = false;
            }
        }

        return sb.toString();
    }



    /**
     * Returns true if a given string is a valid name.
     */
    public static boolean isValidName(String test) {
        return test.matches(VALIDATION_REGEX);
    }


    @Override
    public String toString() {
        return fullName;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Name)) {
            return false;
        }

        Name otherName = (Name) other;
        return fullName.equals(otherName.fullName);
    }

    @Override
    public int hashCode() {
        return fullName.hashCode();
    }

}
