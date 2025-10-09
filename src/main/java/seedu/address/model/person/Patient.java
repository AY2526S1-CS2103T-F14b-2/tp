package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;
import java.util.Set;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Patient in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Patient extends Person {

    private final Note note;

    /**
     * Allows Patient to be instantiated without accompanying note.
     */
    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.note = new Note("NIL");
    }

    /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Note note) {
        super(name, phone, email, address, tags);
        requireAllNonNull(note);
        this.note = note;
    }

    public static Patient of(Name name, Phone phone, Address address, Set<Tag> tags) {
        return new Patient(name, phone, new Email("loleelmao@gmail.com"), address, tags);
    }

    public Note getNote() {
        return note;
    }

    @Override
    public boolean isSamePerson(Person other) {
        if (other == this) {
            return true;
        }
        if (!(other instanceof Patient)) {
            return false;
        }

        Patient p = (Patient) other;

        String thisPhone = this.getPhone().value.trim();
        String otherPhone = p.getPhone().value.trim();

        String thisName = this.getName().toString().trim();
        String otherName = p.getName().toString().trim();

        return thisPhone.equals(otherPhone)
                && thisName.equalsIgnoreCase(otherName);
    }


    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Patient)) {
            return false;
        }

        Person otherPatient = (Patient) other;
        return this.getName().equals(otherPatient.getName())
                && this.getPhone().equals(otherPatient.getPhone())
                && this.getEmail().equals(otherPatient.getEmail())
                && this.getAddress().equals(otherPatient.getAddress())
                && this.getTags().equals(otherPatient.getTags());
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.getName(), this.getPhone(), this.getEmail(), this.getAddress(),
                    this.getTags(), this.getNote());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", this.getName())
                .add("phone", this.getPhone())
                .add("email", this.getEmail())
                .add("address", this.getAddress())
                .add("tags", this.getTags())
                .add("note", this.getNote())
                .toString();
    }
}
