package seedu.address.storage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.tag.Tag;

/**
 * Jackson-friendly version of {@link Patient}.
 */
class JsonAdaptedPatient extends JsonAdaptedPerson {

    private final List<List<String>> appointment;
    private final List<String> notes;
    private final JsonAdaptedTag tag;
    private final JsonAdaptedCaretaker caretaker;

    @JsonCreator
    public JsonAdaptedPatient(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone,
                              @JsonProperty("address") String address,
                              @JsonProperty("appointment") List<List<String>> appointment,
                              @JsonProperty("note") String note,
                              @JsonProperty("notes") List<String> notes,
                              @JsonProperty("tags") JsonAdaptedTag tag,
                              @JsonProperty("caretaker") JsonAdaptedCaretaker caretaker) {
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
        this.caretaker = caretaker;

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
                    eachAppt.getNote().ifPresent(note -> apptDetails.add(note.value));
                    return apptDetails;
                })
                .collect(Collectors.toList());

        this.notes = source.getNotes().stream()
                .map(note -> note.value)
                .collect(Collectors.toList());
        tag = source.getTag().map(t -> new JsonAdaptedTag(t)).orElse(null);
        this.caretaker = source.getCaretaker() == null ? null : new JsonAdaptedCaretaker(source.getCaretaker());
    }

    @Override
    public Patient toModelType() throws IllegalValueException {
        BaseFields base = parseBase(); // name, phone, address parsed & validated

        List<Appointment> modelAppointment = new ArrayList<>();
        if (appointment != null) {
            for (List<String> apptDetails : appointment) {
                if (apptDetails == null || apptDetails.size() < 2) {
                    throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
                }
                String date = apptDetails.get(0);
                String time = apptDetails.get(1);
                String desc = apptDetails.size() > 2 ? apptDetails.get(2) : null;
                if (date == null || time == null) {
                    throw new IllegalValueException(Appointment.MESSAGE_CONSTRAINTS);
                }
                try {
                    if (desc == null) {
                        modelAppointment.add(new Appointment(date, time));
                    } else {
                        modelAppointment.add(new Appointment(date, time, new Note(desc)));
                    }
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
        final Caretaker modelCaretaker = (caretaker == null) ? null : caretaker.toModelType();

        return new Patient(
                base.name, base.phone, base.address,
                modelTag, modelNotes, modelAppointment, modelCaretaker
        );
    }
}
