package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditNoteCommand.EditNoteDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for EditNoteCommand.
 */
public class EditNoteCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPatientAndNoteIndex_success() {
        // Create a patient with notes and add to model
        Patient patientWithNotes = PatientBuilder.withMultipleNotes().build();

        model.addPerson(patientWithNotes);

        // Get the index of the added patient
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(2); // Edit second note (1-based)
        descriptor.setNote(new Note("Updated second note"));

        EditNoteCommand editNoteCommand = new EditNoteCommand(patientIndex, descriptor);

        Patient editedPatient = patientWithNotes.editNote(1, new Note("Updated second note")); // 0-based internally

        String expectedMessage = String.format(EditNoteCommand.MESSAGE_EDIT_NOTE_SUCCESS,
                Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithNotes, editedPatient);

        assertCommandSuccess(editNoteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_invalidPersonIndex_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(1);
        descriptor.setNote(new Note("Some note"));

        EditNoteCommand editNoteCommand = new EditNoteCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editNoteCommand, model,
                String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, model.getSize()));
    }

    @Test
    public void execute_notAPatient_throwsCommandException() {
        // Create a model with a regular Person (not Patient)
        Model modelWithPerson = new ModelManager(new AddressBook(), new UserPrefs());
        Person person = new Caretaker(new Name("John Doe"), new Phone("12345678"),
                new Address("123 Street"), new Relationship("Parent"));
        modelWithPerson.addPerson(person);

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(1);
        descriptor.setNote(new Note("Some note"));

        EditNoteCommand editNoteCommand = new EditNoteCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editNoteCommand, modelWithPerson,
                String.format(EditNoteCommand.MESSAGE_NOT_PATIENT, INDEX_FIRST_PERSON.getOneBased()));
    }

    @Test
    public void execute_patientWithNoNotes_throwsCommandException() {
        Patient patientWithoutNotes = PatientBuilder.withoutNotes().build();

        model.addPerson(patientWithoutNotes);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(1);
        descriptor.setNote(new Note("Some note"));

        EditNoteCommand editNoteCommand = new EditNoteCommand(patientIndex, descriptor);

        assertCommandFailure(editNoteCommand, model, EditNoteCommand.MESSAGE_NO_NOTES);
    }

    @Test
    public void execute_invalidNoteIndex_throwsCommandException() {
        Patient patientWithOneNote = PatientBuilder.withSingleNote().build();

        model.addPerson(patientWithOneNote);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(2); // Invalid - patient only has 1 note
        descriptor.setNote(new Note("Updated note"));

        EditNoteCommand editNoteCommand = new EditNoteCommand(patientIndex, descriptor);

        assertCommandFailure(editNoteCommand, model,
                String.format(NoteCommand.MESSAGE_INVALID_ITEM_INDEX, 2, 1));
    }

    @Test
    public void equals() {
        EditNoteDescriptor descriptorA = new EditNoteDescriptor();
        descriptorA.setNoteIndex(1);
        descriptorA.setNote(new Note("Note A"));

        EditNoteDescriptor descriptorB = new EditNoteDescriptor();
        descriptorB.setNoteIndex(2);
        descriptorB.setNote(new Note("Note B"));

        EditNoteCommand editNoteFirstCommand = new EditNoteCommand(INDEX_FIRST_PERSON, descriptorA);
        EditNoteCommand editNoteSecondCommand = new EditNoteCommand(INDEX_SECOND_PERSON, descriptorB);

        // same object -> returns true
        assertTrue(editNoteFirstCommand.equals(editNoteFirstCommand));

        // same values -> returns true
        EditNoteCommand editNoteFirstCommandCopy = new EditNoteCommand(INDEX_FIRST_PERSON, descriptorA);
        assertTrue(editNoteFirstCommand.equals(editNoteFirstCommandCopy));

        // different types -> returns false
        assertFalse(editNoteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(editNoteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(editNoteFirstCommand.equals(editNoteSecondCommand));
    }

    @Test
    public void editNoteDescriptor_equals() {
        EditNoteDescriptor descriptor1 = new EditNoteDescriptor();
        descriptor1.setNoteIndex(1);
        descriptor1.setNote(new Note("Test note"));

        EditNoteDescriptor descriptor2 = new EditNoteDescriptor();
        descriptor2.setNoteIndex(1);
        descriptor2.setNote(new Note("Test note"));

        EditNoteDescriptor descriptor3 = new EditNoteDescriptor();
        descriptor3.setNoteIndex(2);
        descriptor3.setNote(new Note("Different note"));

        // same object -> returns true
        assertTrue(descriptor1.equals(descriptor1));

        // same values -> returns true
        assertTrue(descriptor1.equals(descriptor2));

        // different values -> returns false
        assertFalse(descriptor1.equals(descriptor3));

        // different types -> returns false
        assertFalse(descriptor1.equals(1));

        // null -> returns false
        assertFalse(descriptor1.equals(null));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        EditNoteDescriptor editNoteDescriptor = new EditNoteDescriptor();
        editNoteDescriptor.setNoteIndex(1);
        editNoteDescriptor.setNote(new Note("Test note"));

        EditNoteCommand editNoteCommand = new EditNoteCommand(targetIndex, editNoteDescriptor);
        String expected = EditNoteCommand.class.getCanonicalName() + "{index=" + targetIndex
                + ", editNoteDescriptor=" + editNoteDescriptor + "}";
        assertEquals(expected, editNoteCommand.toString());
    }
}
