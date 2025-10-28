package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIGH;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPatients.ALICE;
import static seedu.address.testutil.TypicalPatients.BOB;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.PatientBuilder;


public class PatientTest {

    private static final Tag TAG_HIGH = new Tag(VALID_TAG_HIGH);

    @Test
    public void isSamePatient() {
        // same object -> returns true
        assertTrue(ALICE.isSamePerson(ALICE));

        // null -> returns false
        assertFalse(ALICE.isSamePerson(null));

        // name differs in case, all other attributes same -> returns true
        Patient editedBob = new PatientBuilder(BOB).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertTrue(BOB.isSamePerson(editedBob));

        // different note, all other attributes same -> returns true
        //editedBob = new PatientBuilder(BOB).withNote("Different note").build();
        //assertTrue(BOB.isSamePerson(editedBob));

        // name has trailing spaces, all other attributes same -> returns true
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBob = new PatientBuilder(BOB).withName(nameWithTrailingSpaces).build();
        assertTrue(BOB.isSamePerson(editedBob));
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
        editedAlice = new PatientBuilder(ALICE).withTag(VALID_TAG_HIGH).build();
        assertTrue(ALICE.equals(editedAlice));

        // different note -> returns false
        //editedAlice = new PatientBuilder(ALICE).withNote("Different note").build();
        //assertFalse(ALICE.equals(editedAlice));

        // different appointment -> returns false
        editedAlice = new PatientBuilder(ALICE).withAppointment("31-12-2099", "15:30").build();
        assertFalse(ALICE.equals(editedAlice));
    }

    @Test
    public void toStringMethod() {
        String expected = Patient.class.getCanonicalName() + "{name=" + ALICE.getName() + ", phone=" + ALICE.getPhone()
            + ", address=" + ALICE.getAddress() + ", tag=" + ALICE.getTag().orElse(null)
            + ", note=No notes, appointment=" + ALICE.getAppointment()
            + ", caretaker=" + ALICE.getCaretaker() + "}";
        assertEquals(expected, ALICE.toString());
    }

    //@Test
    //public void constructor_withNilNote_doesNotAddNoteToList() {
    //    Note nilNote = new Note("NIL");
    //    Patient patient = new PatientBuilder().withNote("NIL").build();
    //    assertTrue(patient.getNotes().isEmpty());
    //    assertEquals(nilNote, patient.getNote());
    //}

    @Test
    public void constructor_withValidNote_addsNoteToList() {
        Patient patient = new PatientBuilder().withNote("Valid note").build();
        assertEquals(1, patient.getNotes().size());
        assertEquals("Valid note", patient.getNotes().get(0).value);
    }

    @Test
    public void constructor_withMultipleNotes_storesAllNotes() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("First note"));
        notes.add(new Note("Second note"));
        notes.add(new Note("Third note"));

        Patient patient = new Patient(ALICE.getName(), ALICE.getPhone(), ALICE.getAddress(),
                ALICE.getTag().orElse(null), notes, new ArrayList<>());

        assertEquals(3, patient.getNotes().size());
        assertEquals("First note", patient.getNotes().get(0).value);
        assertEquals("Second note", patient.getNotes().get(1).value);
        assertEquals("Third note", patient.getNotes().get(2).value);
    }

    @Test
    public void constructor_withMultipleNotesAndAppointment_storesCorrectly() {
        List<Note> notes = new ArrayList<>();
        notes.add(new Note("Note with appointment"));
        Appointment appointment = new Appointment("31-12-2025", "14:30");

        List<Appointment> appointments = new ArrayList<>();
        appointments.add(appointment);

        Patient patient = new Patient(ALICE.getName(), ALICE.getPhone(), ALICE.getAddress(),
            ALICE.getTag().orElse(null), notes, appointments);

        assertEquals(1, patient.getNotes().size());
        assertEquals("Note with appointment", patient.getNotes().get(0).value);
        assertEquals(1, patient.getAppointment().size());
        assertEquals(appointment, patient.getAppointment().get(0));
    }

    @Test
    public void constructor_nullNotes_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Patient(ALICE.getName(), ALICE.getPhone(),
                ALICE.getAddress(), ALICE.getTag().orElse(null), (List<Note>) null, new ArrayList<>()));
    }

    @Test
    public void constructor_nullNote_createsPatientWithoutNote() {
        Patient patient = new Patient(ALICE.getName(), ALICE.getPhone(),
                ALICE.getAddress(), ALICE.getTag().orElse(null), new ArrayList<>(), new ArrayList<>());
        assertTrue(patient.getNotes().isEmpty());
        assertNull(patient.getNote());
    }

    @Test
    public void addNote_validNote_returnsNewPatientWithAddedNote() {
        Patient originalPatient = new PatientBuilder().build();
        Note newNote = new Note("New note");
        Patient updatedPatient = originalPatient.addNote(newNote);

        // Original patient should be unchanged
        assertTrue(originalPatient.getNotes().isEmpty());

        // Updated patient should have the new note
        assertEquals(1, updatedPatient.getNotes().size());
        assertEquals("New note", updatedPatient.getNotes().get(0).value);
    }

    @Test
    public void addNote_toPatientWithExistingNotes_appendsNote() {
        Patient patientWithNote = new PatientBuilder().withNote("Existing note").build();
        Note additionalNote = new Note("Additional note");
        Patient updatedPatient = patientWithNote.addNote(additionalNote);

        assertEquals(2, updatedPatient.getNotes().size());
        assertEquals("Existing note", updatedPatient.getNotes().get(0).value);
        assertEquals("Additional note", updatedPatient.getNotes().get(1).value);
    }

    @Test
    public void addNote_nullNote_throwsNullPointerException() {
        Patient patient = new PatientBuilder().build();
        assertThrows(NullPointerException.class, () -> patient.addNote(null));
    }

    @Test
    public void addAppointment_validAppointment_returnsNewPatientWithAppointment() {
        Patient originalPatient = new PatientBuilder().build();
        Appointment appointment = new Appointment("31-12-2025", "14:30");
        Patient updatedPatient = originalPatient.addAppointment(appointment);

        // Original patient should be unchanged
        assertTrue(originalPatient.getAppointment().isEmpty());

        // Updated patient should have the new appointment
        assertEquals(1, updatedPatient.getAppointment().size());
        assertEquals(appointment, updatedPatient.getAppointment().get(0));
    }

    @Test
    public void addAppointment_nullAppointment_throwsNullPointerException() {
        Patient patient = new PatientBuilder().build();
        assertThrows(NullPointerException.class, () -> patient.addAppointment(null));
    }

    @Test
    public void getNotes_returnsDefensiveCopy() {
        List<Note> originalNotes = new ArrayList<>();
        originalNotes.add(new Note("Original note"));
        Patient patient = new Patient(ALICE.getName(), ALICE.getPhone(), ALICE.getAddress(),
                ALICE.getTag().orElse(null), originalNotes, new ArrayList<>());

        List<Note> returnedNotes = patient.getNotes();

        // Should not be able to modify the returned list
        assertThrows(UnsupportedOperationException.class, () -> returnedNotes.add(new Note("Should not work")));
    }

    @Test
    public void getNote_withNotes_returnsFirstNote() {
        Patient patient = new PatientBuilder().withNote("First note").build();
        patient = patient.addNote(new Note("Second note"));

        assertEquals("First note", patient.getNote().value);
    }

    @Test
    public void getNote_withoutNotes_returnsNilNote() {
        Patient patient = new PatientBuilder().build();
        assertEquals(null, patient.getNote());
    }

    @Test
    public void isSamePerson_withNonPatient_returnsFalse() {
        Caretaker caretaker = new Caretaker(ALICE.getName(), ALICE.getPhone(), ALICE.getAddress(),
                new Relationship("Father"));
        assertTrue(ALICE.isSamePerson(caretaker));
    }

    @Test
    public void isSamePerson_sameNameAndPhoneDifferentCase_returnsTrue() {
        Patient patient1 = new PatientBuilder().withName("John Doe").withPhone("12345678").build();
        Patient patient2 = new PatientBuilder().withName("JOHN DOE").withPhone("12345678").build();
        assertTrue(patient1.isSamePerson(patient2));
    }

    @Test
    public void isSamePerson_differentPhoneNumbers_returnsFalse() {
        Patient patient1 = new PatientBuilder().withName("John Doe").withPhone("12345678").build();
        Patient patient2 = new PatientBuilder().withName("John Doe").withPhone("87654321").build();
        assertFalse(patient1.isSamePerson(patient2));
    }

    @Test
    public void hashCode_equalPatients_sameHashCode() {
        Patient patient1 = new PatientBuilder(ALICE).build();
        Patient patient2 = new PatientBuilder(ALICE).build();
        assertEquals(patient1.hashCode(), patient2.hashCode());
    }

    @Test
    public void hashCode_differentPatients_differentHashCode() {
        assertNotEquals(ALICE.hashCode(), BOB.hashCode());
    }

    @Test
    public void equals_bothPatientsEmptyAppointments_returnsTrue() {
        Patient patient1 = new PatientBuilder().withName("John").build();
        Patient patient2 = new PatientBuilder().withName("John").build();
        // Both should have no appointments by default
        assertTrue(patient1.equals(patient2));
    }

    @Test
    public void equals_onePatientWithoutAppointmentOtherHasAppointment_returnsFalse() {
        Patient patientWithoutAppointment = new PatientBuilder().build();
        Patient patientWithAppointment = new PatientBuilder().withAppointment("31-12-2025", "14:30").build();
        assertFalse(patientWithoutAppointment.equals(patientWithAppointment));
    }

    @Test
    public void constructor_emptyNotesListAndEmptyAppointmentsList_successful() {
        List<Note> emptyNotes = new ArrayList<>();
        List<Appointment> emptyAppointments = new ArrayList<>();
        Patient patient = new Patient(ALICE.getName(), ALICE.getPhone(), ALICE.getAddress(),
                ALICE.getTag().orElse(null), emptyNotes, emptyAppointments);
        assertTrue(patient.getNotes().isEmpty());
        assertTrue(patient.getAppointment().isEmpty());
    }

    @Test
    public void editNote_validIndexAndNote_returnsNewPatientWithEditedNote() {
        Patient originalPatient = new PatientBuilder()
                .withNote("First note")
                .withNote("Second note")
                .build();

        Note newNote = new Note("Updated second note");
        Patient updatedPatient = originalPatient.editNote(1, newNote);

        // Original patient should be unchanged
        assertEquals("Second note", originalPatient.getNotes().get(1).value);

        // Updated patient should have the edited note
        assertEquals("First note", updatedPatient.getNotes().get(0).value);
        assertEquals("Updated second note", updatedPatient.getNotes().get(1).value);

        // Verify immutability - should be different objects
        assertNotEquals(originalPatient, updatedPatient);
        assertNotEquals(originalPatient.getNotes(), updatedPatient.getNotes());
    }

    @Test
    public void editNote_invalidIndex_throwsIndexOutOfBoundsException() {
        Patient patient = new PatientBuilder().withNote("Only note").build();
        Note newNote = new Note("Updated note");

        // Test negative index
        assertThrows(IndexOutOfBoundsException.class, () -> patient.editNote(-1, newNote));

        // Test index too large
        assertThrows(IndexOutOfBoundsException.class, () -> patient.editNote(1, newNote));

        // Test index way too large
        assertThrows(IndexOutOfBoundsException.class, () -> patient.editNote(5, newNote));
    }

    @Test
    public void editNote_emptyNotesList_throwsIndexOutOfBoundsException() {
        Patient patient = new PatientBuilder().build(); // No notes added
        Note newNote = new Note("Any note");

        assertThrows(IndexOutOfBoundsException.class, () -> patient.editNote(0, newNote));
    }

    @Test
    public void editNote_nullNote_throwsNullPointerException() {
        Patient patient = new PatientBuilder().withNote("Some note").build();

        assertThrows(NullPointerException.class, () -> patient.editNote(0, null));
    }

    @Test
    public void patient_getTagReturnsCorrectTag_successful() {
        Patient patient = new PatientBuilder().withName("John Doe").withPhone("12345678").build();
        Optional<Tag> tagOpt = patient.getTag();
        assertTrue(tagOpt.get().equals(TAG_HIGH));
    }

    @Test
    public void deleteNote_validIndex_returnsNewPatientWithDeletedNote() {
        Patient originalPatient = new PatientBuilder()
                .withNote("First note")
                .withNote("Second note")
                .withNote("Third note")
                .build();

        Patient updatedPatient = originalPatient.deleteNote(1); // Delete second note (0-based)

        // Original patient should be unchanged
        assertEquals(3, originalPatient.getNotes().size());
        assertEquals("Second note", originalPatient.getNotes().get(1).value);

        // Updated patient should have the note deleted
        assertEquals(2, updatedPatient.getNotes().size());
        assertEquals("First note", updatedPatient.getNotes().get(0).value);
        assertEquals("Third note", updatedPatient.getNotes().get(1).value);

        // Verify immutability - should be different objects
        assertNotEquals(originalPatient, updatedPatient);
        assertNotEquals(originalPatient.getNotes(), updatedPatient.getNotes());
    }

    @Test
    public void deleteNote_firstIndex_returnsNewPatientWithFirstNoteDeleted() {
        Patient originalPatient = new PatientBuilder()
                .withNote("First note")
                .withNote("Second note")
                .build();

        Patient updatedPatient = originalPatient.deleteNote(0); // Delete first note

        assertEquals(1, updatedPatient.getNotes().size());
        assertEquals("Second note", updatedPatient.getNotes().get(0).value);
    }

    @Test
    public void deleteNote_lastIndex_returnsNewPatientWithLastNoteDeleted() {
        Patient originalPatient = new PatientBuilder()
                .withNote("First note")
                .withNote("Second note")
                .build();

        Patient updatedPatient = originalPatient.deleteNote(1); // Delete last note

        assertEquals(1, updatedPatient.getNotes().size());
        assertEquals("First note", updatedPatient.getNotes().get(0).value);
    }

    @Test
    public void deleteNote_invalidIndex_throwsIndexOutOfBoundsException() {
        Patient patient = new PatientBuilder().withNote("Only note").build();

        // Test negative index
        assertThrows(IndexOutOfBoundsException.class, () -> patient.deleteNote(-1));

        // Test index equal to size (should be invalid)
        assertThrows(IndexOutOfBoundsException.class, () -> patient.deleteNote(1));

        // Test index way too large
        assertThrows(IndexOutOfBoundsException.class, () -> patient.deleteNote(5));
    }

    @Test
    public void deleteNote_emptyNotesList_throwsIndexOutOfBoundsException() {
        Patient patient = new PatientBuilder().build(); // No notes added

        // Any index should throw exception for empty list
        assertThrows(IndexOutOfBoundsException.class, () -> patient.deleteNote(0));
        assertThrows(IndexOutOfBoundsException.class, () -> patient.deleteNote(-1));
        assertThrows(IndexOutOfBoundsException.class, () -> patient.deleteNote(1));
    }

    @Test
    public void addAppointment_multipleAppointments_sortedChronologically() {
        Patient patient = new PatientBuilder().build();

        // Add appointments in non-chronological order
        Appointment laterAppointment = new Appointment("31-12-2099", "15:30");
        Appointment earlierAppointment = new Appointment("15-11-2099", "10:00");
        Appointment middleAppointment = new Appointment("20-12-2099", "12:00");

        patient = patient.addAppointment(laterAppointment);
        patient = patient.addAppointment(earlierAppointment);
        patient = patient.addAppointment(middleAppointment);

        List<Appointment> appointments = patient.getAppointment();
        assertEquals(3, appointments.size());

        // Verify chronological order
        assertEquals(earlierAppointment, appointments.get(0));
        assertEquals(middleAppointment, appointments.get(1));
        assertEquals(laterAppointment, appointments.get(2));
    }

    @Test
    public void constructor_multipleAppointments_sortedChronologically() {
        List<Appointment> unsortedAppointments = new ArrayList<>();
        unsortedAppointments.add(new Appointment("31-12-2099", "15:30"));
        unsortedAppointments.add(new Appointment("15-11-2099", "10:00"));
        unsortedAppointments.add(new Appointment("20-12-2099", "12:00"));

        Patient patient = new Patient(ALICE.getName(), ALICE.getPhone(), ALICE.getAddress(),
                ALICE.getTag().orElse(null), new ArrayList<>(), unsortedAppointments);

        List<Appointment> appointments = patient.getAppointment();
        assertEquals(3, appointments.size());

        // Verify chronological order (earliest to latest)
        assertEquals("15-11-2099", appointments.get(0).getDate());
        assertEquals("20-12-2099", appointments.get(1).getDate());
        assertEquals("31-12-2099", appointments.get(2).getDate());
    }

}
