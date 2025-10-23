package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code DeleteCaretakerCommand}.
 */
public class DeleteCaretakerCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validIndexUnfilteredList_success() {
        @SuppressWarnings("Unchecked")
        Patient patientToDeleteFrom = (Patient) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCaretakerCommand deleteCommand = new DeleteCaretakerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCaretakerCommand.MESSAGE_DELETE_CARETAKER_SUCCESS,
                Messages.format(patientToDeleteFrom.getCaretaker()));

        ModelManager expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        Patient replacementPatient = new Patient(patientToDeleteFrom.getName(), patientToDeleteFrom.getPhone(),
                patientToDeleteFrom.getAddress(), patientToDeleteFrom.getTag().orElse(null),
                patientToDeleteFrom.getNotes(), patientToDeleteFrom.getAppointment(), null);

        expectedModel.setPerson(patientToDeleteFrom, replacementPatient);

        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexUnfilteredList_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        DeleteCaretakerCommand deleteCommand = new DeleteCaretakerCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void execute_validIndexFilteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        @SuppressWarnings("Unchecked")
        Patient patientToDeleteFrom = (Patient) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        DeleteCaretakerCommand deleteCommand = new DeleteCaretakerCommand(INDEX_FIRST_PERSON);

        String expectedMessage = String.format(DeleteCaretakerCommand.MESSAGE_DELETE_CARETAKER_SUCCESS,
                Messages.format(patientToDeleteFrom.getCaretaker()));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        Patient replacementPatient = new Patient(patientToDeleteFrom.getName(), patientToDeleteFrom.getPhone(),
                patientToDeleteFrom.getAddress(), patientToDeleteFrom.getTag().orElse(null),
                patientToDeleteFrom.getNotes(), patientToDeleteFrom.getAppointment(), null);

        showPersonAtIndex(expectedModel, INDEX_FIRST_PERSON);

        expectedModel.setPerson(patientToDeleteFrom, replacementPatient);
        assertCommandSuccess(deleteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidIndexFilteredList_throwsCommandException() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        DeleteCaretakerCommand deleteCommand = new DeleteCaretakerCommand(outOfBoundIndex);

        assertCommandFailure(deleteCommand, model, Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
    }

    @Test
    public void equals() {
        DeleteCaretakerCommand deleteFirstCommand = new DeleteCaretakerCommand(INDEX_FIRST_PERSON);
        DeleteCaretakerCommand deleteSecondCommand = new DeleteCaretakerCommand(INDEX_SECOND_PERSON);

        // same object -> returns true
        assertTrue(deleteFirstCommand.equals(deleteFirstCommand));

        // same values -> returns true
        DeleteCaretakerCommand deleteFirstCommandCopy = new DeleteCaretakerCommand(INDEX_FIRST_PERSON);
        assertTrue(deleteFirstCommand.equals(deleteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(deleteFirstCommand.equals(deleteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteCaretakerCommand deleteCommand = new DeleteCaretakerCommand(targetIndex);
        String expected = DeleteCaretakerCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + "}";
        assertEquals(expected, deleteCommand.toString());
    }

    /**
     * Updates {@code model}'s filtered list to show no one.
     */
    private void showNoPerson(Model model) {
        model.updateFilteredPersonList(p -> false);

        assertTrue(model.getFilteredPersonList().isEmpty());
    }
}
