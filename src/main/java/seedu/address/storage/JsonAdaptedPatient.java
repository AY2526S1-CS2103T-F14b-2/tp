package seedu.address.storage;

import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient extends JsonAdaptedPerson {

    private final String appointment;
    private final String note;

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

    public JsonAdaptedPatient(Patient source) {
        super(source);
        this.appointment = source.getAppointment() == null ? null : source.getAppointment().toString();
        this.note = source.getNote() == null ? null : source.getNote().value;
    }

    @Override
    public Patient toModelType() throws IllegalValueException {
        Person base = super.toModelType();

        Appointment modelAppointment = null;
        if (appointment != null) {
            String trimmedAppointment = appointment.trim();
            if (trimmedAppointment.isEmpty()) {
                throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
            }
            String[] parts = trimmedAppointment.split("\\s+", 2);
            if (parts.length != 2) {
                throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
            }
            try {
                modelAppointment = new Appointment(parts[0], parts[1]);
            } catch (IllegalArgumentException e) {
                throw new IllegalValueException(e.getMessage());
            }
        }

        Note modelNote = note == null ? new Note("NIL") : new Note(note);

        return new Patient(base.getName(), base.getPhone(), base.getAddress(),
                new HashSet<>(base.getTags()), modelNote, modelAppointment);
    }
}
