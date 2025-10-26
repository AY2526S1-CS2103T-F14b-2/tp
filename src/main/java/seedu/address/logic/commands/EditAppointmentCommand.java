package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Edits an appointment of an existing patient in the address book.
 */
public class EditAppointmentCommand extends AbstractEditCommand<Patient,
    EditAppointmentCommand.EditAppointmentDescriptor> {

    public static final String COMMAND_WORD = "editappt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an appointment of the patient identified "
            + "by the index number used in the displayed person list. "
            + "The appointment to edit is identified by its appointment index (1-based).\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ITEM_INDEX + "ITEM_INDEX "
            + "[" + PREFIX_DATE + "NEW_DATE] "
            + "[" + PREFIX_TIME + "NEW_TIME] "
            + "[" + PREFIX_NOTE + "NEW_NOTE]\n"
            + "At least one of NEW_DATE, NEW_TIME, or NEW_NOTE must be provided.\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ITEM_INDEX + "2 "
            + PREFIX_DATE + "12-10-2026 "
            + PREFIX_TIME + "12:00 "
            + PREFIX_NOTE + "Dental visit";

    public static final String MESSAGE_EDIT_APPOINTMENT_SUCCESS = "Edited appointment for patient: %1$s";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
            + "Appointment can only be edited for patients.";
    public static final String MESSAGE_INVALID_ITEM_INDEX = "The appointment index %1$s is invalid. "
            + "Patient has %2$s appointment(s).";
    public static final String MESSAGE_NO_APPOINTMENT = "Patient has no appointment to edit.";

    /**
     * Creates an EditAppointmentCommand to edit the specified appointment of the patient at the specified index.
     */
    public EditAppointmentCommand(Index targetIndex, EditAppointmentDescriptor editAppointmentDescriptor) {
        super(targetIndex, editAppointmentDescriptor);
    }

    @Override
    protected List<Patient> getTargetList(Model model) {
        // Convert the person list to a proper patient list with type checking
        List<Person> personList = model.getFilteredPersonList();
        List<Patient> patientList = new ArrayList<>();

        for (Person person : personList) {
            if (person instanceof Patient) {
                patientList.add((Patient) person);
            } else {
                // Add a null placeholder to maintain index correspondence
                patientList.add(null);
            }
        }

        return patientList;
    }

    @Override
    protected void validateEdit(Model model, Patient patientToEdit, EditAppointmentDescriptor editDescriptor)
            throws CommandException {
        // If patientToEdit is null, it means the person at this index is not a Patient
        if (patientToEdit == null) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, super.getIndex().getOneBased()));
        }

        // Check if patient has appointment
        List<Appointment> appointments = patientToEdit.getAppointment();
        if (appointments.isEmpty()) {
            throw new CommandException(MESSAGE_NO_APPOINTMENT);
        }

        // Check if appointment index is valid
        int appointmentIndex = editDescriptor.getAppointmentIndex();
        if (appointmentIndex < 1 || appointmentIndex > appointments.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_ITEM_INDEX,
                    appointmentIndex, appointments.size()));
        }

        Appointment originalAppointment = appointments.get(appointmentIndex - 1);
        try {
            editDescriptor.buildUpdatedAppointment(originalAppointment);
        } catch (IllegalArgumentException iae) {
            throw new CommandException(iae.getMessage(), iae);
        }
    }

    @Override
    protected boolean isAnyFieldEdited(EditAppointmentDescriptor editDescriptor) {
        return editDescriptor.isAnyFieldEdited();
    }

    @Override
    protected Patient createEditedItem(Patient patientToEdit, EditAppointmentDescriptor editAppointmentDescriptor) {
        // patientToEdit is the correct patient from the filtered list
        assert patientToEdit != null;

        int appointmentIndex = editAppointmentDescriptor.getAppointmentIndex() - 1; // Convert to 0-based index
        Appointment currentAppointment = patientToEdit.getAppointment().get(appointmentIndex);

        Appointment newAppointment = editAppointmentDescriptor.buildUpdatedAppointment(currentAppointment);

        return patientToEdit.editAppointment(appointmentIndex, newAppointment);
    }

    @Override
    protected void updateModel(Model model, Patient originalPatient, Patient editedPatient) {
        model.setPerson(originalPatient, editedPatient);
    }

    @Override
    protected String formatSuccessMessage(Patient editedPatient) {
        return String.format(MESSAGE_EDIT_APPOINTMENT_SUCCESS, Messages.format(editedPatient));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", super.getIndex())
                .add("editAppointmentDescriptor", super.getEditDescriptor())
                .toString();
    }

    /**
     * Stores the details to edit a appointment with. The appointment field will replace the
     * corresponding appointment at the specified index.
     */
    public static class EditAppointmentDescriptor {
        private int appointmentIndex;
        private String date;
        private String time;
        private Note note;
        private boolean isNoteEdited;
        private boolean noteCleared;

        public EditAppointmentDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditAppointmentDescriptor(EditAppointmentDescriptor toCopy) {
            setAppointmentIndex(toCopy.appointmentIndex);
            setDate(toCopy.date);
            setTime(toCopy.time);
            if (toCopy.noteCleared) {
                clearNote();
            } else if (toCopy.isNoteEdited) {
                setNote(toCopy.note);
            }
        }

        /**
         * Sets the appointment index (1-based) to edit.
         */
        public void setAppointmentIndex(int appointmentIndex) {
            this.appointmentIndex = appointmentIndex;
        }

        /**
         * Returns the appointment index (1-based) to edit.
         */
        public int getAppointmentIndex() {
            return appointmentIndex;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Optional<String> getDate() {
            return Optional.ofNullable(date);
        }

        public void setTime(String time) {
            this.time = time;
        }

        public Optional<String> getTime() {
            return Optional.ofNullable(time);
        }

        public void setNote(Note note) {
            Objects.requireNonNull(note);
            this.note = note;
            this.isNoteEdited = true;
            this.noteCleared = false;
        }

        public Optional<Note> getNote() {
            if (!isNoteEdited || noteCleared) {
                return Optional.empty();
            }
            return Optional.of(note);
        }

        /**
         * Indicates that updated appointment will have no description
         */
        public void clearNote() {
            this.note = null;
            this.isNoteEdited = true;
            this.noteCleared = true;
        }

        public boolean isNoteCleared() {
            return isNoteEdited && noteCleared;
        }

        public boolean isAnyFieldEdited() {
            return date != null || time != null || isNoteEdited;
        }

        /**
         * Builds the updated {@link Appointment} by applying the edited fields, falling back to the
         * values from {@code originalAppointment} when a field is not provided.
         */
        public Appointment buildUpdatedAppointment(Appointment originalAppointment) {
            String updatedDate = getDate().orElse(originalAppointment.getDate());
            String updatedTime = getTime().orElse(originalAppointment.getTime());
            Note updatedNote;
            if (isNoteCleared()) {
                updatedNote = null;
            } else if (getNote().isPresent()) {
                updatedNote = getNote().get();
            } else {
                updatedNote = originalAppointment.getNote().orElse(null);
            }

            return new Appointment(updatedDate, updatedTime, updatedNote);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof EditAppointmentDescriptor)) {
                return false;
            }

            EditAppointmentDescriptor otherEditAppointmentDescriptor = (EditAppointmentDescriptor) other;
            return Objects.equals(date, otherEditAppointmentDescriptor.date)
                && Objects.equals(time, otherEditAppointmentDescriptor.time)
                && Objects.equals(note, otherEditAppointmentDescriptor.note)
                && isNoteEdited == otherEditAppointmentDescriptor.isNoteEdited
                && noteCleared == otherEditAppointmentDescriptor.noteCleared
                && appointmentIndex == otherEditAppointmentDescriptor.appointmentIndex;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("date", date)
                    .add("time", time)
                    .add("note", note)
                    .add("isNoteEdited", isNoteEdited)
                    .add("isNoteCleared", noteCleared)
                    .add("appointmentIndex", appointmentIndex)
                    .toString();
        }
    }
}
