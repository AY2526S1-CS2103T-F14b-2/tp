package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIGH;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPatients.ALICE;
import static seedu.address.testutil.TypicalPatients.BOB;

import java.util.List;

import javax.swing.text.html.HTML.Tag;

import org.junit.jupiter.api.Test;

import seedu.address.testutil.PatientBuilder;


public class PatientTest {

    @Test
    public void asObservableList_modifyList_throwsUnsupportedOperationException() {
        Patient patient = new PatientBuilder().build();
        // Try to modify the tags set - should throw exception
        assertThrows(UnsupportedOperationException.class, () -> patient.getTags().add(ALICE.getTags().iterator().next()));
    }

    @Test
    public void isSamePatient() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // name differs in case, all other attributes same -> returns false
        Patient editedBob = new PatientBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BOB.isSamePerson(editedBob));

        // different note, all other attributes same -> returns true
        editedBob = new PatientBuilder(BOB).withNote("Different note").build();
        assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PatientBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertFalse(BOB.isSamePerson(editedBob));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Patient aliceCopy = new PatientBuilder(ALICE).build();
        assertTrue(ALICE.equals(aliceCopy));

        // same object -> returns true
        assertTrue(ALICE.equals(ALICE));

        // null -> returns false
        assertNotEquals(ALICE, null);

        // different type -> returns false
        assertFalse(ALICE.equals(5));

        // different person -> returns false
        assertFalse(ALICE.equals(BOB));

        // different name -> returns true
        Patient editedAlice = new PatientBuilder(ALICE).withName(VALID_NAME_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different phone -> returns false
        editedAlice = new PatientBuilder(ALICE).withPhone(VALID_PHONE_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different address -> returns false
        editedAlice = new PatientBuilder(ALICE).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(ALICE.equals(editedAlice));

        // different tags -> returns false
        editedAlice = new PatientBuilder(ALICE).withTags(VALID_TAG_HIGH).build();
        assertFalse(ALICE.equals(editedAlice));

        // different note -> returns false
        editedAlice = new PatientBuilder(ALICE).withNote("Different note").build();
        assertFalse(ALICE.equals(editedAlice));

        // different appointment -> returns false
        editedAlice = new PatientBuilder(ALICE).withAppointment("31-12-2099", "15:30").build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Patient.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
            + ", address=" + ALICE.getAddress() + ", tags=" + ALICE.getTags()
            + ", note=" + ALICE.getNote() + ", appointment=" + ALICE.getAppointment() + "}";
        assertEquals(expected, ALICE.toString());
    }

    @Test
    public void addNote_validNote_success() {
        Patient patient = new PatientBuilder().build(); // Has 0 notes
        Note note = new Note("Test note");
        
        Patient updatedPatient = patient.addNote(note);
        
        assertEquals(1, updatedPatient.getNotes().size()); // Just the new note
        assertEquals(note, updatedPatient.getNotes().get(0)); // New note
        // Original patient should be unchanged
        assertEquals(0, patient.getNotes().size());
    }        @Test
    public void addNote_multipleNotes_success() {
        Patient patient = new PatientBuilder().build(); // Has 0 notes
        Note note1 = new Note("First note");
        Note note2 = new Note("Second note");
        
        Patient withFirstNote = patient.addNote(note1);
        Patient withBothNotes = withFirstNote.addNote(note2);
        
        assertEquals(2, withBothNotes.getNotes().size());
        assertEquals(note1, withBothNotes.getNotes().get(0));
        assertEquals(note2, withBothNotes.getNotes().get(1));
    }

    @Test
    public void addNote_nullNote_throwsNullPointerException() {
        Patient patient = new PatientBuilder().build();
        assertThrows(NullPointerException.class, () -> patient.addNote(null));
    }

    @Test
    public void getNotes_modifyReturnedList_throwsUnsupportedOperationException() {
        Patient patient = new PatientBuilder().withNote("Test note").build();
        assertThrows(UnsupportedOperationException.class, () -> patient.getNotes().add(new Note("Another note")));
    }

    @Test
    public void getNotes_defensiveCopy_success() {
        Note note1 = new Note("First note");
        Note note2 = new Note("Second note");
        Patient patient = new PatientBuilder().build().addNote(note1).addNote(note2); // Start with 0, add 2
        
        // Getting notes should return a defensive copy
        List<Note> notes = patient.getNotes();
        assertEquals(2, notes.size());
        assertEquals(note1, notes.get(0));
        assertEquals(note2, notes.get(1));
    }    @Test
    public void getNote_singleNote_returnsFirstNote() {
        Note note = new Note("Test note");
        Patient patient = new PatientBuilder().build().addNote(note);

        assertEquals(note, patient.getNote());
    }

    @Test
    public void getNote_multipleNotes_returnsFirstNote() {
        Note note1 = new Note("First note");
        Note note2 = new Note("Second note");
        Patient patient = new PatientBuilder().build().addNote(note1).addNote(note2);

        assertEquals(note1, patient.getNote());
    }

    @Test
    public void getNote_noNotes_returnsNilNote() {
        Patient patient = new PatientBuilder().build();
        assertEquals(new Note("NIL"), patient.getNote());
    }

    @Test
    public void equals_sameNotesOrder_returnsTrue() {
        Note note1 = new Note("First note");
        Note note2 = new Note("Second note");

        Patient patient1 = new PatientBuilder().build().addNote(note1).addNote(note2);
        Patient patient2 = new PatientBuilder().build().addNote(note1).addNote(note2);

        assertEquals(patient1, patient2);
    }

    @Test
    public void equals_differentNotesOrder_returnsFalse() {
        Note note1 = new Note("First note");
        Note note2 = new Note("Second note");

        Patient patient1 = new PatientBuilder().build().addNote(note1).addNote(note2);
        Patient patient2 = new PatientBuilder().build().addNote(note2).addNote(note1);

        assertNotEquals(patient1, patient2);
    }
}
