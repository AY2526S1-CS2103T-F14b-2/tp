package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Patient;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for DeleteNoteCommand.
 */
public class DeleteNoteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPatientAndNoteIndex_success() {
        // Create a patient with notes and add to model
        Patient patientWithNotes = PatientBuilder.withMultipleNotes().build();

        model.addPerson(patientWithNotes);

        // Get the index of the added patient
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        DeleteNoteCommand deleteNoteCommand = new DeleteNoteCommand(patientIndex, 2); // Delete second note (1-based)

        Patient editedPatient = patientWithNotes.deleteNote(1); // 0-based internally
        String expectedMessage = String.format(DeleteNoteCommand.MESSAGE_DELETE_NOTE_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithNotes, editedPatient);

        assertCommandSuccess(deleteNoteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);

        DeleteNoteCommand deleteNoteCommand = new DeleteNoteCommand(outOfBoundIndex, 1);

        assertCommandFailure(deleteNoteCommand, model, String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX,
            model.getSize()));
    }

    @Test
    public void execute_patientWithNoNotes_throwsCommandException() {
        Patient patientWithoutNotes = PatientBuilder.withoutNotes().build();

        model.addPerson(patientWithoutNotes);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        DeleteNoteCommand deleteNoteCommand = new DeleteNoteCommand(patientIndex, 1);

        assertCommandFailure(deleteNoteCommand, model,
            String.format(NoteCommand.MESSAGE_INVALID_ITEM_INDEX, 1, 0));
    }

    @Test
    public void execute_invalidNoteIndex_throwsCommandException() {
        Patient patientWithOneNote = PatientBuilder.withSingleNote().build();

        model.addPerson(patientWithOneNote);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        // Invalid - patient only has 1 note
        DeleteNoteCommand deleteNoteCommand = new DeleteNoteCommand(patientIndex, 2);

        assertCommandFailure(deleteNoteCommand, model,
            String.format(NoteCommand.MESSAGE_INVALID_ITEM_INDEX, 2, 1));
    }

    @Test
    public void equals() {
        DeleteNoteCommand deleteNoteFirstCommand = new DeleteNoteCommand(INDEX_FIRST_PERSON, 1);
        DeleteNoteCommand deleteNoteSecondCommand = new DeleteNoteCommand(INDEX_FIRST_PERSON, 2);

        // same object -> returns true
        assertTrue(deleteNoteFirstCommand.equals(deleteNoteFirstCommand));

        // same values -> returns true
        DeleteNoteCommand deleteNoteFirstCommandCopy = new DeleteNoteCommand(INDEX_FIRST_PERSON, 1);
        assertTrue(deleteNoteFirstCommand.equals(deleteNoteFirstCommandCopy));

        // different types -> returns false
        assertFalse(deleteNoteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(deleteNoteFirstCommand.equals(null));

        // different note index -> returns false
        assertFalse(deleteNoteFirstCommand.equals(deleteNoteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        DeleteNoteCommand deleteNoteCommand = new DeleteNoteCommand(targetIndex, 1);
        String expected = DeleteNoteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex
                + ", noteIndex=" + 1 + "}";
        assertEquals(expected, deleteNoteCommand.toString());
    }
}
