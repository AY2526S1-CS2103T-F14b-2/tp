package seedu.address.storage;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

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
    private final List<String> notes;

    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone,
                              @JsonProperty("address") String address,
                              @JsonProperty("appointment") String appointment,
                              @JsonProperty("note") String note,
                              @JsonProperty("notes") List<String> notes,
                              @JsonProperty("tags") List<JsonAdaptedTag> tags) {
        super(name, phone, address, tags);
        this.appointment = appointment;

        // Handle backward compatibility: if notes list is provided, use it; otherwise convert single note
        if (notes != null && !notes.isEmpty()) {
            this.notes = new ArrayList<>(notes);
        } else if (note != null) {
            this.notes = new ArrayList<>();
            this.notes.add(note);
        } else {
            this.notes = new ArrayList<>();
        }
    }

    public JsonAdaptedPatient(Patient source) {
        super(source);
        this.appointment = source.getAppointment() == null ? null : source.getAppointment().toString();
        this.notes = source.getNotes().stream()
                .map(note -> note.value)
                .collect(Collectors.toList());
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

        List<Note> modelNotes = new ArrayList<>();
        if (notes != null) {
            for (String noteValue : notes) {
                if (noteValue != null && !noteValue.equals("NIL")) {
                    modelNotes.add(new Note(noteValue));
                }
            }
        }

        return new Patient(base.getName(), base.getPhone(), base.getAddress(),
                new HashSet<>(base.getTags()), modelNotes, modelAppointment);
    }
}
