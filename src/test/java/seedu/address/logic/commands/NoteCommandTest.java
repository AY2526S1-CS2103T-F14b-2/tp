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

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;



/**
 * Contains integration tests (interaction with the Model) and unit tests for
 * {@code NoteCommand}.
 */
public class NoteCommandTest {

    private static final String NOTE_STUB = "Some note";
    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_addNoteUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Skip test if first person is not a patient
        if (!(firstPerson instanceof Patient)) {
            return;
        }

        Patient firstPatient = (Patient) firstPerson;

        // Calculate expected combined note
        Note expectedNote;
        if (firstPatient.getNote() != null) {
            String combinedNoteValue = firstPatient.getNote().value + " | " + NOTE_STUB;
            expectedNote = new Note(combinedNoteValue);
        } else {
            expectedNote = new Note(NOTE_STUB);
        }

        List<Note> notesList = new ArrayList<>(firstPatient.getNotes());
        if (expectedNote != null) {
            notesList.add(expectedNote);
        }
        Patient editedPatient = new Patient(firstPatient.getName(), firstPatient.getPhone(),
                firstPatient.getAddress(), firstPatient.getTag().orElse(null),
                notesList, firstPatient.getAppointment(), firstPatient.getCaretaker());

        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(NOTE_STUB));

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, Messages.format(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(noteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_appendNoteToExistingNote_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        // Skip test if first person is not a patient
        if (!(firstPerson instanceof Patient)) {
            return;
        }

        List<Note> initialNotes = new ArrayList<>();

        initialNotes.add(new Note("Initial note"));

        Patient firstPatient = (Patient) firstPerson;


        // First, add an initial note to ensure we have an existing note
        Patient patientWithInitialNote = new Patient(firstPatient.getName(), firstPatient.getPhone(),
                firstPatient.getAddress(), firstPatient.getTag().orElse(null),
                initialNotes, firstPatient.getAppointment());

        model.setPerson(firstPatient, patientWithInitialNote);


        // Now add a second note which should be appended

        String secondNoteText = "Second note";
        List<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(new Note("Initial note"));
        expectedNotes.add(new Note("Second note"));

        Patient expectedPatient = new Patient(patientWithInitialNote.getName(), patientWithInitialNote.getPhone(),
                patientWithInitialNote.getAddress(), patientWithInitialNote.getTag().orElse(null),
                expectedNotes,
                patientWithInitialNote.getAppointment());

        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(secondNoteText));

        String expectedMessage = String.format(NoteCommand.MESSAGE_SUCCESS, Messages.format(expectedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithInitialNote, expectedPatient);

        assertCommandSuccess(noteCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_addNoteToNonPatient_throwsCommandException() {
        // Find a non-patient person or create one
        Person nonPatient = null;
        Index nonPatientIndex = null;

        for (int i = 0; i < model.getFilteredPersonList().size(); i++) {
            Person person = model.getFilteredPersonList().get(i);
            if (!(person instanceof Patient)) {
                nonPatient = person;
                nonPatientIndex = Index.fromZeroBased(i);
                break;
            }
        }

        // If no non-patient found, skip this test
        if (nonPatient == null) {
            return;
        }

        NoteCommand noteCommand = new NoteCommand(nonPatientIndex, new Note(NOTE_STUB));
        String expectedMessage = String.format(NoteCommand.MESSAGE_NOT_PATIENT, nonPatientIndex.getOneBased());
        assertCommandFailure(noteCommand, model, expectedMessage);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex, new Note(NOTE_STUB));

        assertCommandFailure(noteCommand, model,
            String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, model.getSize()));
    }

    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        NoteCommand noteCommand = new NoteCommand(outOfBoundIndex, new Note(NOTE_STUB));

        assertCommandFailure(noteCommand, model,
            String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, model.getSize()));
    }

    @Test
    public void equals() {
        Note note = new Note("Some note");
        NoteCommand noteFirstCommand = new NoteCommand(INDEX_FIRST_PERSON, note);
        NoteCommand noteSecondCommand = new NoteCommand(INDEX_SECOND_PERSON, note);

        // same object -> returns true
        assertTrue(noteFirstCommand.equals(noteFirstCommand));

        // same values -> returns true
        NoteCommand noteFirstCommandCopy = new NoteCommand(INDEX_FIRST_PERSON, note);
        assertTrue(noteFirstCommand.equals(noteFirstCommandCopy));

        // different types -> returns false
        assertFalse(noteFirstCommand.equals(1));

        // null -> returns false
        assertFalse(noteFirstCommand.equals(null));

        // different person -> returns false
        assertFalse(noteFirstCommand.equals(noteSecondCommand));
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        Note note = new Note("Some note");
        NoteCommand noteCommand = new NoteCommand(targetIndex, note);
        String expected = NoteCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex + ", note=" + note + "}";
        assertEquals(expected, noteCommand.toString());
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        Note note = new Note("Some note");
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
                new NoteCommand(null, note));
    }

    @Test
    public void constructor_nullNote_throwsNullPointerException() {
        org.junit.jupiter.api.Assertions.assertThrows(NullPointerException.class, () ->
            new NoteCommand(INDEX_FIRST_PERSON, null));
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        Note note = new Note("Some note");
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, note);
        org.junit.jupiter.api.Assertions
                .assertThrows(NullPointerException.class, () -> noteCommand.execute(null));
    }

    @Test
    public void equals_differentNote_returnsFalse() {
        Note note1 = new Note("First note");
        Note note2 = new Note("Second note");
        NoteCommand noteCommand1 = new NoteCommand(INDEX_FIRST_PERSON, note1);
        NoteCommand noteCommand2 = new NoteCommand(INDEX_FIRST_PERSON, note2);

        assertFalse(noteCommand1.equals(noteCommand2));
    }

    @Test
    public void execute_emptyFilteredList_throwsCommandException() {
        // Create a model with an empty filtered list
        Model emptyModel = new ModelManager(new AddressBook(), new UserPrefs());
        NoteCommand noteCommand = new NoteCommand(INDEX_FIRST_PERSON, new Note(NOTE_STUB));

        assertCommandFailure(noteCommand, emptyModel,
                String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, emptyModel.getSize()));
    }

    @Test
    public void execute_indexExactlyAtBoundaryFiltered_throwsCommandException() {
        // Filter to show only first person, then try to access second
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index indexOutOfBounds = INDEX_SECOND_PERSON;

        NoteCommand noteCommand = new NoteCommand(indexOutOfBounds, new Note(NOTE_STUB));
        assertCommandFailure(noteCommand, model,
            String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, model.getSize()));
    }
}
