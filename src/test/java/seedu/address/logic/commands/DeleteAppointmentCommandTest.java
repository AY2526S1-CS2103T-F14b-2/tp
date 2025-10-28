package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Patient;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains tests for {@link DeleteAppointmentCommand}.
 */
public class DeleteAppointmentCommandTest {

    @Test
    public void execute_patientWithoutAppointments_throwsCommandException() {
        Model model = new ModelManager();
        Patient patientWithoutAppointments = new PatientBuilder().build();
        model.addPerson(patientWithoutAppointments);

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(Index.fromOneBased(1), 1);

        assertCommandFailure(command, model, DeleteAppointmentCommand.MESSAGE_NO_APPOINTMENT);
    }

    @Test
    public void execute_invalidAppointmentIndex_throwsCommandException() {
        Model model = new ModelManager();
        Patient patientWithSingleAppointment = new PatientBuilder()
                .withAppointment("31-12-2025", "14:30")
                .build();
        model.addPerson(patientWithSingleAppointment);

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(Index.fromOneBased(1), 2);
        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_INVALID_APPOINTMENT_INDEX, 2, 1);

        assertCommandFailure(command, model, expectedMessage);
    }

    @Test
    public void execute_validAppointmentIndex_deletesAppointment() {
        Model model = new ModelManager();
        Patient patientWithAppointment = new PatientBuilder()
                .withAppointment("31-12-2025", "14:30")
                .withAppointment("01-01-2026", "09:00")
                .build();
        model.addPerson(patientWithAppointment);

        List<Appointment> updatedAppointments = new ArrayList<>(patientWithAppointment.getAppointment());
        updatedAppointments.remove(0);
        Patient expectedPatient = new Patient(
            patientWithAppointment.getName(),
            patientWithAppointment.getPhone(),
            patientWithAppointment.getAddress(),
            patientWithAppointment.getTag().orElse(null),
            new ArrayList<>(patientWithAppointment.getNotes()),
                updatedAppointments,
                patientWithAppointment.getCaretaker());

        DeleteAppointmentCommand command = new DeleteAppointmentCommand(Index.fromOneBased(1), 1);
        String expectedMessage = String.format(DeleteAppointmentCommand.MESSAGE_DELETE_APPOINTMENT_SUCCESS,
            "1", patientWithAppointment.getName(), patientWithAppointment.getPhone());

        try {
            CommandResult result = command.execute(model);
            assertEquals(expectedMessage, result.getFeedbackToUser());
            Patient updatedPatient = (Patient) model.getFilteredPersonList().get(0);
            assertEquals(expectedPatient, updatedPatient);
        } catch (CommandException e) {
            org.junit.jupiter.api.Assertions.fail("Execution should succeed", e);
        }
    }

    @Test
    public void equals() {
        DeleteAppointmentCommand firstCommand = new DeleteAppointmentCommand(Index.fromOneBased(1), 1);
        DeleteAppointmentCommand secondCommand = new DeleteAppointmentCommand(Index.fromOneBased(2), 1);
        DeleteAppointmentCommand sameAsFirstDifferentAppt = new DeleteAppointmentCommand(Index.fromOneBased(1), 2);

        // same object -> true
        assertTrue(firstCommand.equals(firstCommand));

        // same values -> true
        DeleteAppointmentCommand firstCommandCopy = new DeleteAppointmentCommand(Index.fromOneBased(1), 1);
        assertTrue(firstCommand.equals(firstCommandCopy));

        // different type -> false
        assertFalse(firstCommand.equals(5));

        // null -> false
        assertFalse(firstCommand.equals(null));

        // different patient index -> false
        assertFalse(firstCommand.equals(secondCommand));

        // different appointment index -> false
        assertFalse(firstCommand.equals(sameAsFirstDifferentAppt));
    }
}
