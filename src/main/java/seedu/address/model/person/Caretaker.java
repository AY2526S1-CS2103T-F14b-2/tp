package seedu.address.model.person;

import seedu.address.commons.util.ToStringBuilder;

import java.util.Objects;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

public class Caretaker extends Person {
    private final Relationship relationship;

    /**
     * Allows Caretaker to be instantiated.
     */
    public Caretaker(Name name, Phone phone, Address address, Relationship relationship) {
        super(name, phone, address);
        requireAllNonNull(relationship);
        this.relationship = relationship;
    }

    /**
     * Returns the relationship of the caretaker.
     *
     * @return the relationship of the caretaker with respect to patient.
     */
    public Relationship getRelationship() {
        return this.relationship;
    }

    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        if (!(other instanceof Caretaker)) {
            return super.equals(other);
        }

        Caretaker otherCaretaker = (Caretaker) other;
        return super.equals(otherCaretaker)
                && relationship.equals(otherCaretaker.getRelationship());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(super.hashCode(), relationship);
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this)
                .add("name", this.getName())
                .add("phone", this.getPhone())
                .add("address", this.getAddress())
                .add("relationship", this.getRelationship());

        return sb.toString();
    }
}
