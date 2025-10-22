package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient extends JsonAdaptedPerson {

    private final List<List<String>> appointment;
    private final List<String> notes;
    private final JsonAdaptedTag tag;

    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone,
                              @JsonProperty("address") String address,
                              @JsonProperty("appointment") List<List<String>> appointment,
                              @JsonProperty("note") String note,
                              @JsonProperty("notes") List<String> notes,
                              @JsonProperty("tags") JsonAdaptedTag tag) {
        super(name, phone, address);

        if (appointment != null) {
            this.appointment = new ArrayList<>();
            for (List<String> appt : appointment) {
                if (appt != null) {
                    this.appointment.add(new ArrayList<>(appt));
                }
            }
        } else {
            this.appointment = new ArrayList<>();
        }

        this.tag = tag;

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
        this.appointment = source.getAppointment().stream()
                .map(eachAppt -> {
                    List<String> apptDetails = new ArrayList<>();
                    apptDetails.add(eachAppt.getDate());
                    apptDetails.add(eachAppt.getTime());
                    return apptDetails;
                })
                .collect(Collectors.toList());

        this.notes = source.getNotes().stream()
                .map(note -> note.value)
                .collect(Collectors.toList());
        tag = source.getTag().map(t -> new JsonAdaptedTag(t)).orElse(null);
    }

    @Override
    public Patient toModelType() throws IllegalValueException {
        Person base = super.toModelType();

        List<Appointment> modelAppointment = new ArrayList<>();
        if (appointment != null) {
            for (List<String> apptDetails : appointment) {
                if (apptDetails == null || apptDetails.size() < 2) {
                    throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
                }
                String date = apptDetails.get(0);
                String time = apptDetails.get(1);
                if (date == null || time == null) {
                    throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
                }
                try {
                    modelAppointment.add(new Appointment(date, time));
                } catch (IllegalArgumentException ex) {
                    throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
                }
            }
        }

        List<Note> modelNotes = new ArrayList<>();
        if (notes != null) {
            for (String noteValue : notes) {
                if (noteValue != null) {
                    modelNotes.add(new Note(noteValue));
                }
            }
        }

        final Tag modelTag = (tag == null) ? null : tag.toModelType();

        return new Patient(base.getName(), base.getPhone(), base.getAddress(),
            modelTag, modelNotes, modelAppointment);
    }
}
