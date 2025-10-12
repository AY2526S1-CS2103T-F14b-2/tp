package seedu.address.model.person;

import java.util.Objects;
import java.util.Set;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Patient in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Patient extends Person {

    private final Note note;
    private final Appointment appointment;

    /**
     * Allows Patient to be instantiated without accompanying note.
     */
    public Patient(Name name, Phone phone, Address address, Set<Tag> tags) {
        super(name, phone, address, tags);
        this.note = new Note("NIL");
        this.appointment = null;
    }

    /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Phone phone, Address address, Set<Tag> tags, Note note) {
        super(name, phone, address, tags);
        requireAllNonNull(note);
        this.note = note;
        this.appointment = null;
    }

   /**
     * Every field must be present and not null.
     */
    public Patient(Name name, Phone phone, Address address, Set<Tag> tags, Note note, Appointment appointment) {
        super(name, phone, address, tags);
        requireAllNonNull(note);
        this.note = note;
        this.appointment = appointment;
    }


    /**
     * Returns the note of the patient.
     * @return the note of the patient.
     */
    public Note getNote() {
        return note;
    }

    /**
     * Returns the appointment of the patient.
     * @return the appointment of the patient.
     */
    public Appointment getAppointment() {
        return appointment;
    }

    public Patient addAppointment(Appointment appointment) {
        return new Patient(this.getName(), this.getPhone(), this.getAddress(),
                this.getTags(), this.getNote(), this.getAppointment());
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

        Patient otherPatient = (Patient) other;
        return this.getName().equals(otherPatient.getName())
                && this.getPhone().equals(otherPatient.getPhone())
                && this.getAddress().equals(otherPatient.getAddress())
                && this.getTags().equals(otherPatient.getTags())
                && this.note.equals(otherPatient.note)
                && this.appointment.equals(otherPatient.appointment);

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(this.getName(), this.getPhone(), this.getAddress(),
                    this.getTags(), this.note, this.appointment);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", this.getName())
                .add("phone", this.getPhone())
                .add("address", this.getAddress())
                .add("tags", this.getTags())
                .add("note", this.getNote())
                .toString();
    }
}
