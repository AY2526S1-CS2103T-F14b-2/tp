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

    /**
     * Adds an appointment to this patient.
     * @param appointment the appointment to add
     * @return a new Patient with the appointment added
     */
    public Patient addAppointment(Appointment appointment) {
        requireAllNonNull(appointment);
        return new Patient(this.getName(), this.getPhone(), this.getAddress(),
                this.getTags(), this.getNote(), appointment);
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
        if (!(other instanceof Person)) {
            return false;
        }

        if (!(other instanceof Patient)) {
            return super.equals(other);
        }

        Patient otherPatient = (Patient) other;
        return super.equals(otherPatient)
                && note.equals(otherPatient.note)
                && Objects.equals(appointment, otherPatient.appointment);

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(super.hashCode(), note, appointment);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("name", this.getName())
                .add("phone", this.getPhone())
                .add("address", this.getAddress())
                .add("tags", this.getTags())
                .add("note", this.getNote())
                .add("appointment", this.getAppointment())
                .toString();
    }
}
