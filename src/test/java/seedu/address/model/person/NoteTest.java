package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;



public class NoteTest {

    @Test
    public void constructor_null_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Note(null));
    }

    @Test
    public void constructor_invalidAddress_throwsIllegalArgumentException() {
        String invalidNote = "";
        assertThrows(IllegalArgumentException.class, () -> new Note(invalidNote));
    }

    @Test
    public void isValidNote() {
        // null note
        assertThrows(NullPointerException.class, () -> Note.isValidNote(null));

        // invalid notes
        assertFalse(Note.isValidNote("")); // empty string
        assertFalse(Note.isValidNote(" ")); // spaces only

        // valid notes
        assertTrue(Note.isValidNote("No seafood"));
        assertTrue(Note.isValidNote("-")); // one character
        assertTrue(Note.isValidNote("Please only give antibiotics, "
                    + "lives with mother and has difficulty swallowing")); // long address
    }

    @Test
    public void equals() {
        Note note = new Note("Valid Note");

        // same values -> returns true
        assertTrue(note.equals(new Note("Valid Note")));

        // same object -> returns true
        assertTrue(note.equals(note));

        // null -> returns false
        assertFalse(note.equals(null));

        // different types -> returns false
        assertFalse(note.equals(5.0f));

        // different values -> returns false
        assertFalse(note.equals(new Note("Other Valid Note")));
    }

    @Test
    public void isValidNote_lengthConstraints() {
        // note at maximum length (200 characters) - should be valid
        String maxLengthNote = "a".repeat(200);
        assertTrue(Note.isValidNote(maxLengthNote));

        // note exceeding maximum length (201 characters) - should be invalid
        String tooLongNote = "a".repeat(201);
        assertFalse(Note.isValidNote(tooLongNote));

        // note exceeding maximum length by a lot - should be invalid
        String wayTooLongNote = "a".repeat(500);
        assertFalse(Note.isValidNote(wayTooLongNote));
    }

    @Test
    public void constructor_tooLongNote_throwsIllegalArgumentException() {
        String tooLongNote = "a".repeat(201);
        assertThrows(IllegalArgumentException.class, () -> new Note(tooLongNote));
    }

    @Test
    public void constructor_whitespaceOnlyNote_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Note("   "));
        assertThrows(IllegalArgumentException.class, () -> new Note("\t"));
        assertThrows(IllegalArgumentException.class, () -> new Note("\n"));
    }

    @Test
    public void isValidNote_edgeCases() {
        // note starting with non-whitespace but containing whitespace - valid
        assertTrue(Note.isValidNote("a b c"));
        assertTrue(Note.isValidNote("Note with spaces"));

        // note with trailing whitespace - valid
        assertTrue(Note.isValidNote("Note with trailing space "));

        // note with special characters - valid
        assertTrue(Note.isValidNote("Note@#$%^&*()"));
    }

    @Test
    public void toString_validNote_returnsValue() {
        String noteText = "Test note content";
        Note note = new Note(noteText);
        assertEquals(noteText, note.toString());
    }

    @Test
    public void hashCode_equalNotes_sameHashCode() {
        Note note1 = new Note("Same content");
        Note note2 = new Note("Same content");
        assertEquals(note1.hashCode(), note2.hashCode());
    }

    @Test
    public void value_fieldAccessible() {
        String noteContent = "Accessible note content";
        Note note = new Note(noteContent);
        assertEquals(noteContent, note.value);
    }
}
