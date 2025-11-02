package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;

import java.util.ArrayList;
import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Patient;

/**
 * Deletes an appointment from an existing patient in the address book.
 */
public class DeleteAppointmentCommand extends AbstractDeleteCommand<Patient> {

    public static final String COMMAND_WORD = "deleteappt";

    public static final String MESSAGE_USAGE = COMMAND_WORD
        + ": Deletes the appointment of the patient identified by the index"
        + "number used in the displayed patient list.\n"
        + "The appointment to delete is identified by its appointment index (1-based).\n"
        + "Parameters: INDEX (must be a positive integer) "
        + PREFIX_ITEM_INDEX + "ITEM_INDEX\n"
        + "Example: " + COMMAND_WORD + " 1 " + PREFIX_ITEM_INDEX + "2";


    public static final String MESSAGE_DELETE_APPOINTMENT_SUCCESS = "Appointment %1$s deleted.\n%2$s";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
        + "Appointments can only be deleted for patients.";
    public static final String MESSAGE_NO_APPOINTMENT = "Patient has no appointment to delete.";
    public static final String MESSAGE_INVALID_APPOINTMENT_INDEX = "The appointment at index %1$s is invalid. "
        + "Patient has %2$s appointment(s).";

    public final int apptIndex;

    /**
     * Deletes an appointment from an existing patient in the address book.
     * @param targetIndex target patient index
     * @param apptIndex target appt index
     */
    public DeleteAppointmentCommand(Index targetIndex, int apptIndex) {
        super(targetIndex);
        this.apptIndex = apptIndex;
    }

    @Override
    protected List<Patient> getTargetList(Model model) {
        // Cast the person list to patients for the abstract class
        // We'll validate the actual type in validateEdit if needed
        @SuppressWarnings("unchecked")
        List<Patient> patientList = (List<Patient>) (List<?>) model.getFilteredPersonList();
        return patientList;
    }

    @Override
    protected void deleteItem(Model model, Patient patient) {
        List<Appointment> updatedAppointments = new ArrayList<>(patient.getAppointment());
        updatedAppointments.remove(apptIndex - 1);
        Patient updatedPatient = new Patient(patient.getName(),
            patient.getPhone(), patient.getAddress(), patient.getTag().orElse(null),
            patient.getNotes(), updatedAppointments, patient.getCaretaker());
        model.setPerson(patient, updatedPatient);
    }

    @Override
    protected void validateDeletion(Model model, Patient patient) throws CommandException {
        if (patient == null) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, getTargetIndex().getOneBased()));
        }

        List<Appointment> appointments = patient.getAppointment();
        if (appointments.isEmpty()) {
            throw new CommandException(MESSAGE_NO_APPOINTMENT);
        }

        if (apptIndex < 1 || apptIndex > appointments.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_APPOINTMENT_INDEX,
                    apptIndex, appointments.size()));
        }
    }

    @Override
    protected String formatSuccessMessage(Patient deletedPatient) {
        return String.format(MESSAGE_DELETE_APPOINTMENT_SUCCESS,
            Integer.toString(this.apptIndex), Messages.shortFormat(deletedPatient));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteAppointmentCommand)) {
            return false;
        }

        DeleteAppointmentCommand otherCommand = (DeleteAppointmentCommand) other;
        return super.equals(otherCommand) && apptIndex == otherCommand.apptIndex;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
            .add("targetIndex", getTargetIndex()).toString();
    }
}
