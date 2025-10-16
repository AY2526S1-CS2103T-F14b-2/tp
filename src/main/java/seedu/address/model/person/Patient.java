package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;


/**
 * Represents a Patient in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Patient extends Person {

    private final List<Note> notes;
    private final Appointment appointment;

    /**
     * Allows Patient to be instantiated without accompanying note.
     */
    public Patient(Name name, Phone phone, Address address, Tag tag) {
        super(name, phone, address, tag);
        this.notes = new ArrayList<>();
        this.appointment = null;
    }

    /**
     * Constructor with single note for backward compatibility.
     */
    public Patient(Name name, Phone phone, Address address, Tag tag, Note note) {
        super(name, phone, address, tag);
        requireAllNonNull(note);
        this.notes = new ArrayList<>();
        if (!note.value.equals("NIL")) {
            this.notes.add(note);
        }
        this.appointment = null;
    }

    /**
     * Constructor with single note and appointment for backward compatibility.
     */
    public Patient(Name name, Phone phone, Address address, Tag tag, Note note, Appointment appointment) {
        super(name, phone, address, tag);
        requireAllNonNull(note);
        this.notes = new ArrayList<>();
        if (!note.value.equals("NIL")) {
            this.notes.add(note);
        }
        this.appointment = appointment;
    }

    /**
     * Constructs a Patient with multiple notes but no appointment.
     * Creates a defensive copy of the provided notes list to ensure immutability.
     *
     * @param name the patient's name, must not be null
     * @param phone the patient's phone number, must not be null
     * @param address the patient's address, must not be null
     * @param notes the list of notes for the patient, must not be null (can be empty)
     * @throws NullPointerException if any parameter is null
     */
    public Patient(Name name, Phone phone, Address address, Tag tag, List<Note> notes) {
        super(name, phone, address, tag);
        requireAllNonNull(notes);
        this.notes = new ArrayList<>(notes);
        this.appointment = null;
    }

    /**
     * Constructs a Patient with multiple notes and an appointment.
     * Creates a defensive copy of the provided notes list to ensure immutability.
     * This is the most comprehensive constructor supporting all patient data fields.
     *
     * @param name the patient's name, must not be null
     * @param phone the patient's phone number, must not be null
     * @param address the patient's address, must not be null
     * @param tag the urgency associated with the patient condition, can be null if no tag is given
     * @param notes the list of notes for the patient, must not be null (can be empty)
     * @param appointment the patient's appointment, can be null if no appointment is scheduled
     * @throws NullPointerException if any required parameter is null
     */
    public Patient(Name name, Phone phone, Address address, Tag tag, List<Note> notes, Appointment appointment) {
        super(name, phone, address, tag);
        requireAllNonNull(notes);
        this.notes = new ArrayList<>(notes);
        this.appointment = appointment;
    }


    /**
     * Returns the notes of the patient.
     * @return the notes of the patient.
     */
    public List<Note> getNotes() {
        return Collections.unmodifiableList(notes);
    }

    /**
     * Returns the first note of the patient, or a "NIL" note if no notes exist.
     * @return the first note of the patient.
     */
    public Note getNote() {
        return notes.isEmpty() ? new Note("NIL") : notes.get(0);
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
                this.getTag().orElse(null), this.notes, appointment);
    }

    /**
     * Adds a note to this patient.
     * @param note the note to add
     * @return a new Patient with the note added
     */
    public Patient addNote(Note note) {
        requireAllNonNull(note);
        List<Note> newNotes = new ArrayList<>(this.notes);
        newNotes.add(note);
        return new Patient(this.getName(), this.getPhone(), this.getAddress(),
                this.getTag().orElse(null), newNotes, this.appointment);
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
                && notes.equals(otherPatient.notes)
                && Objects.equals(appointment, otherPatient.appointment);

    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(super.hashCode(), notes, appointment);
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this)
                .add("name", this.getName())
                .add("phone", this.getPhone())
                .add("address", this.getAddress());

        super.getTag().ifPresent(tag -> sb.add("tag", tag));

        sb.add("note", this.getNote())
                .add("appointment", this.getAppointment());

        return sb.toString();
    }
}
