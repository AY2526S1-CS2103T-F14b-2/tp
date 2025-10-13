package seedu.address.testutil;

import java.util.HashSet;
import java.util.Set;

import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;
import seedu.address.model.util.SampleDataUtil;




/**
 * A utility class to help with building Patient objects.
 */
public class PatientBuilder {

    public static final String DEFAULT_NAME = "Betty Bee";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_NOTE = "Seafood allergy";

    private Name name;
    private Phone phone;
    private Address address;
    private Set<Tag> tags;
    private Note note;
    private Appointment appointment;

    /**
     * Creates a {@code PatientBuilder} with the default details.
     */
    public PatientBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        tags = new HashSet<>();
        note = new Note(DEFAULT_NOTE);
        appointment = null;
    }

    /**
     * Initializes the PatientBuilder with the data of {@code patientToCopy}.
     */
    public PatientBuilder(Patient patientToCopy) {
        name = patientToCopy.getName();
        phone = patientToCopy.getPhone();
        address = patientToCopy.getAddress();
        tags = new HashSet<>(patientToCopy.getTags());
        note = patientToCopy.getNote();
        appointment = patientToCopy.getAppointment();
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
    public PatientBuilder withTags(String ... tags) {
        this.tags = SampleDataUtil.getTagSet(tags);
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
     * Sets the {@code Note} of the {@code Patient} that we are building.
     */
    public PatientBuilder withNote(String note) {
        this.note = new Note(note);
        return this;
    }

    /**
     * Sets the {@code Appointment} of the {@code Patient} that we are building.
     * @param date
     * @param time
     * @return
     */
    public PatientBuilder withAppointment(String date, String time) {
        this.appointment = new Appointment(date, time);
        return this;
    }

    public Patient build() {
        return new Patient(name, phone, address, tags, note, appointment);
    }

}
