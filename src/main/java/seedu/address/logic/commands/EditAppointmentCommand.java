package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

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
public class EditAppointmentCommand extends AbstractEditCommand<Patient, EditAppointmentCommand.EditAppointmentDescriptor> { 

    public static final String COMMAND_WORD = "editappt";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits an appointment of the patient identified "
            + "by the index number used in the displayed person list. "
            + "The appointment to edit is identified by its appointment index (1-based).\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ITEM_INDEX + "ITEM_INDEX "
            + PREFIX_DATE + "NEW_DATE"
            + PREFIX_TIME + "NEW_TIME"
            + PREFIX_NOTE + "NEW_NOTE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ITEM_INDEX + "2 "
            + PREFIX_DATE + "12-10-2026"
            + PREFIX_TIME + "12:00"
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
    }

    @Override
    protected boolean isAnyFieldEdited(EditAppointmentDescriptor editDescriptor) {
        return editDescriptor.getAppointment().isPresent();
    }

    @Override
    protected Patient createEditedItem(Patient patientToEdit, EditAppointmentDescriptor editAppointmentDescriptor) {
        // patientToEdit is the correct patient from the filtered list
        assert patientToEdit != null;

        Appointment newAppointment = editAppointmentDescriptor.getAppointmentValue(); // We know it exists from validation
        int appointmentIndex = editAppointmentDescriptor.getAppointmentIndex() - 1; // Convert to 0-based index

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
        private Appointment appointment;
        private int appointmentIndex;

        public EditAppointmentDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditAppointmentDescriptor(EditAppointmentDescriptor toCopy) {
            setAppointment(toCopy.appointment);
            setAppointmentIndex(toCopy.appointmentIndex);
        }

        /**
         * Sets the appointment to edit to.
         */
        public void setAppointment(Appointment appointment) {
            this.appointment = appointment;
        }

        /**
         * Returns the appointment to edit to.
         */
        public Optional<Appointment> getAppointment() {
            return Optional.ofNullable(appointment);
        }

        /**
         * Returns the appointment value directly, assuming it has been validated to exist.
         * Should only be called after validation confirms the appointment is present.
         */
        public Appointment getAppointmentValue() {
            return appointment;
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

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditAppointmentDescriptor)) {
                return false;
            }

            EditAppointmentDescriptor otherEditAppointmentDescriptor = (EditAppointmentDescriptor) other;
            return Objects.equals(appointment, otherEditAppointmentDescriptor.appointment)
                    && appointmentIndex == otherEditAppointmentDescriptor.appointmentIndex;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("appointment", appointment)
                    .add("appointmentIndex", appointmentIndex)
                    .toString();
        }
    }
}
