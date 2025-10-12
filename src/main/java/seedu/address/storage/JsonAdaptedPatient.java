package seedu.address.storage;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient extends JsonAdaptedPerson {

    public static final String MISSING_APPOINTMENT_MESSAGE = "Patient's appointment field is missing!";

    private final String appointment;
    private final String note;

    /**
     * Constructs a {@code JsonAdaptedPatient} with the given patient details.
     */
    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone,
                              @JsonProperty("address") String address,
                              @JsonProperty("appointment") String appointment,
                              @JsonProperty("note") String note,
                              @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        super(name, phone, address, tags);
        this.appointment = appointment;
        this.note = note;
    }

    /**
     * Converts a given {@code Patient} into this class for Jackson use.
     */
    public JsonAdaptedPatient(Patient source) {
        super(source);
        this.appointment = source.getAppointment() == null ? null : source.getAppointment().toString();
        this.note = source.getNote() == null ? null : source.getNote().value;
    }

    @Override
    public Patient toModelType() throws IllegalValueException {
        Person basePerson = super.toModelType();
        if (!(basePerson instanceof Patient)) {
            throw new IllegalValueException("Base person is not a patient.");
        }

        Patient basePatient = (Patient) basePerson;

        Appointment modelAppointment = null;
        if (appointment != null) {
            if (!Appointment.isValidAppointment(appointment)) {
                throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
            }
            modelAppointment = new Appointment(appointment);
        }

        Note modelNote = null;
        if (note != null) {
            if (!Note.isValidNote(note)) {
                throw new IllegalValueException(Note.MESSAGE_CONSTRAINTS);
            }
            modelNote = new Note(note);
        }

        return new Patient(basePatient.getName(),
                basePatient.getPhone(),
                basePatient.getAddress(),
                basePatient.getTags(),
                modelNote,
                modelAppointment);
    }
}
