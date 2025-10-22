package seedu.address.model.person;

import static java.util.Objects.requireNonNull;
import static seedu.address.commons.util.AppUtil.checkArgument;

/**
 * Represents a Caretaker's relationship in the address book.
 * Guarantees: immutable; is valid as declared in {@link #isValidRelationship(String)}
 */
public class Relationship {

    public static final String BLANK_RELATIONSHIP =
            "Relationship cannot be blank";
    public static final String MESSAGE_CONSTRAINTS = "Relationship can take any values, and it should not be blank";
    /*
     * The first character of the relationship must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Constructs an {@code Relationship}.
     *
     * @param relationship A valid relationship.
     */
    public Relationship(String relationship) {
        requireNonNull(relationship);
        checkArgument(isValidRelationship(relationship), MESSAGE_CONSTRAINTS);
        value = relationship;
    }

    /**
     * Returns true if a given string is a valid email.
     */
    public static boolean isValidRelationship(String test) {
        return test.matches(VALIDATION_REGEX);
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Relationship)) {
            return false;
        }

        Relationship otherRelationship = (Relationship) other;
        return value.equals(otherRelationship.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

}
