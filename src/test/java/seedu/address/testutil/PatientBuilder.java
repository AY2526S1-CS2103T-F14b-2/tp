package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;





/**
 * A utility class to help with building Patient objects.
 */
public class PatientBuilder {

    public static final String DEFAULT_NAME = "Betty Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_TAG = "high";
    public static final String DEFAULT_RELATIONSHIP = "Father";

    private Name name;
    private Phone phone;
    private Address address;
    private Tag tag;
    private List<Note> notes;
    private List<Appointment> appointments;
    private Caretaker caretaker;

    /**
     * Creates a {@code PatientBuilder} with the default details.
     */
    public PatientBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        tag = new Tag(DEFAULT_TAG);
        notes = new ArrayList<>();
        appointments = new ArrayList<>();
        caretaker = new Caretaker(new Name(DEFAULT_NAME), new Phone(DEFAULT_PHONE), new Address(DEFAULT_ADDRESS),
                new Relationship(DEFAULT_RELATIONSHIP));
    }

    /**
     * Initializes the PatientBuilder with the data of {@code patientToCopy}.
     */
    public PatientBuilder(Patient patientToCopy) {
        name = patientToCopy.getName();
        phone = patientToCopy.getPhone();
        address = patientToCopy.getAddress();
        tag = patientToCopy.getTag().orElse(null);
        notes = new ArrayList<>(patientToCopy.getNotes());
        appointments = new ArrayList<>(patientToCopy.getAppointment());
        caretaker = patientToCopy.getCaretaker();
    }

    /**
     * Sets the {@code Name} of the {@code Patient} that we are building.
     */
    public PatientBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Parses the {@code tags} into a {@code Set<Tag>} and set it to the {@code Patient} that we are building.
     */
    public PatientBuilder withTag(String tagName) {
        this.tag = new Tag(tagName);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Patient} that we are building.
     */
    public PatientBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Patient} that we are building.
     */
    public PatientBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Adds a {@code Note} to the {@code Patient} that we are building.
     */
    public PatientBuilder withNote(String note) {
        this.notes.add(new Note(note));
        return this;
    }

    /**
     * Sets the {@code Appointment} of the {@code Patient} that we are building.
     * @param date
     * @param time
     * @return
     */
    public PatientBuilder withAppointment(String date, String time) {
        if (this.appointments == null) {
            this.appointments = new ArrayList<>();
        }
        this.appointments.add(new Appointment(date, time));
        return this;
    }

    /**
     * Sets the {@code Caretaker} of the {@code Patient} that we are building.
     */
    public PatientBuilder withCaretaker(Caretaker caretaker) {
        this.caretaker = caretaker;
        return this;
    }

    /**
     * Creates a patient with multiple notes for testing note-related commands.
     * This is a convenience method to reduce code duplication in tests.
     * @return PatientBuilder with "John Doe" and two sample notes
     */
    public static PatientBuilder withMultipleNotes() {
        return new PatientBuilder()
                .withName("John Doe")
                .withPhone("98765432")
                .withAddress("123 Main St")
                .withNote("First note")
                .withNote("Second note");
    }

    /**
     * Creates a patient with no notes for testing edge cases.
     * This is a convenience method to reduce code duplication in tests.
     * @return PatientBuilder with "Jane Doe" and no notes
     */
    public static PatientBuilder withoutNotes() {
        return new PatientBuilder()
                .withName("Jane Doe")
                .withPhone("87654321")
                .withAddress("456 Oak St");
    }

    /**
     * Creates a patient with a single note for testing boundary cases.
     * This is a convenience method to reduce code duplication in tests.
     * @return PatientBuilder with "Bob Smith" and one note
     */
    public static PatientBuilder withSingleNote() {
        return new PatientBuilder()
                .withName("Bob Smith")
                .withPhone("76543210")
                .withAddress("789 Pine St")
                .withNote("Only note");
    }

    /**
     * Builds a {@code Patient} from a {@code PatientBuilder}
     * @return {@code Patient}
     */
    public Patient build() {
        return new Patient(this.name, this.phone,
                this.address, this.tag, this.notes, this.appointments, this.caretaker);
    }

}
