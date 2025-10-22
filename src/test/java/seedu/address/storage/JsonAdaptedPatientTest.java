package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Phone;
import seedu.address.testutil.PatientBuilder;

public class JsonAdaptedPatientTest {

    private static final String VALID_NAME = "Alice";
    private static final String VALID_PHONE = "12345678";
    private static final String VALID_ADDRESS = "123 Main Street";
    private static final List<List<String>> VALID_APPOINTMENTS = Arrays.asList(
        Arrays.asList("31-12-2025", "14:30"));
    private static final String VALID_NOTE = "Allergic to peanuts";
    private static final JsonAdaptedTag VALID_TAG = new JsonAdaptedTag("medium");

    @Test
    public void toModelType_validPatientDetails_returnsPatient() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            VALID_APPOINTMENTS, VALID_NOTE, null, VALID_TAG);
        List<Appointment> expectedAppointments = new ArrayList<>();
        expectedAppointments.add(new Appointment("31-12-2025", "14:30"));
        List<Note> expectedNotes = new ArrayList<>();
        expectedNotes.add(new Note(VALID_NOTE));
        Patient expectedPatient = new Patient(new Name(VALID_NAME), new Phone(VALID_PHONE),
            new Address(VALID_ADDRESS), VALID_TAG.toModelType(),
            expectedNotes, expectedAppointments);
        assertEquals(expectedPatient, patient.toModelType());
    }

    @Test
    public void toModelType_nullAppointment_returnsPatientWithNoAppointment() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                null, VALID_NOTE, null, VALID_TAG);
        Patient result = patient.toModelType();
        assertTrue(result.getAppointment().isEmpty());
    }

    @Test
    public void toModelType_nullNote_returnsPatientWithDefaultNote() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            VALID_APPOINTMENTS, null, null, VALID_TAG);
        Patient result = patient.toModelType();
        assertNull(result.getNote());
    }

    @Test
    public void toModelType_emptyAppointment_throwsIllegalValueException() {
        List<List<String>> invalidAppointments = Arrays.asList(
            Arrays.asList("   ", "   "));
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            invalidAppointments, VALID_NOTE, null, VALID_TAG);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_invalidAppointmentFormat_throwsIllegalValueException() {
        List<List<String>> invalidAppointments = Arrays.asList(
            Arrays.asList("invalid-date", "invalid-time"));
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            invalidAppointments, VALID_NOTE, null, VALID_TAG);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_appointmentOnlyDate_throwsIllegalValueException() {
        List<List<String>> invalidAppointments = Arrays.asList(
            Arrays.asList("31-12-2025"));
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            invalidAppointments, VALID_NOTE, null, VALID_TAG);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_invalidAppointmentDateTime_throwsIllegalValueException() {
        List<List<String>> invalidAppointments = Arrays.asList(
            Arrays.asList("invalid-date", "14:30"),
            Arrays.asList("31-12-2025", "invalid-time"));
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            invalidAppointments, VALID_NOTE, null, VALID_TAG);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void constructor_patientWithAppointment_success() throws Exception {
        Patient patient = new PatientBuilder().withAppointment("31-12-2025", "14:30").build();
        JsonAdaptedPatient jsonPatient = new JsonAdaptedPatient(patient);

        // Test by converting back to Patient and checking the appointment
        Patient converted = jsonPatient.toModelType();
        assertEquals(patient.getAppointment(), converted.getAppointment());
    }

    @Test
    public void constructor_patientWithoutAppointment_resultsInEmptyAppointmentList() throws Exception {
        Patient patient = new PatientBuilder().build();
        JsonAdaptedPatient jsonPatient = new JsonAdaptedPatient(patient);

        // Test by converting back to Patient and checking the appointment
        Patient converted = jsonPatient.toModelType();
        assertTrue(converted.getAppointment().isEmpty());
    }

    @Test
    public void constructor_patientWithNote_success() throws Exception {
        Patient patient = new PatientBuilder().withNote("Test note").build();
        JsonAdaptedPatient jsonPatient = new JsonAdaptedPatient(patient);

        // Test by converting back to Patient and checking the note
        Patient converted = jsonPatient.toModelType();
        assertEquals(patient.getNote(), converted.getNote());
    }

    @Test
    public void constructor_patientWithDefaultNote_success() throws Exception {
        Patient patient = new PatientBuilder().build();
        JsonAdaptedPatient jsonPatient = new JsonAdaptedPatient(patient);

        // Test by converting back to Patient
        Patient converted = jsonPatient.toModelType();
        assertEquals(patient.getNote(), converted.getNote());
    }

    @Test
    public void toModelType_multipleNotes_returnsPatientWithMultipleNotes() throws Exception {
        List<String> notes = new ArrayList<>();
        notes.add("First note");
        notes.add("Second note");
        notes.add("Third note");

        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            VALID_APPOINTMENTS, null, notes, VALID_TAG);
        Patient result = patient.toModelType();

        assertEquals(3, result.getNotes().size());
        assertEquals("First note", result.getNotes().get(0).value);
        assertEquals("Second note", result.getNotes().get(1).value);
        assertEquals("Third note", result.getNotes().get(2).value);
    }

    @Test
    public void toModelType_notesWithValidValues_createsAllNotes() throws Exception {
        List<String> notes = new ArrayList<>();
        notes.add("Valid note");
        notes.add("NIL");
        notes.add("Another valid note");

        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            VALID_APPOINTMENTS, null, notes, VALID_TAG);
        Patient result = patient.toModelType();

        assertEquals(3, result.getNotes().size());
        assertEquals("Valid note", result.getNotes().get(0).value);
        assertEquals("NIL", result.getNotes().get(1).value);
        assertEquals("Another valid note", result.getNotes().get(2).value);
    }

    @Test
    public void toModelType_notesWithNullValues_filtersOutNullNotes() throws Exception {
        List<String> notes = new ArrayList<>();
        notes.add("Valid note");
        notes.add(null);
        notes.add("Another valid note");

        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            VALID_APPOINTMENTS, null, notes, VALID_TAG);
        Patient result = patient.toModelType();

        assertEquals(2, result.getNotes().size());
        assertEquals("Valid note", result.getNotes().get(0).value);
        assertEquals("Another valid note", result.getNotes().get(1).value);
    }

    @Test
    public void constructor_patientWithMultipleNotes_preservesAllNotes() throws Exception {
        // Create a patient with multiple notes
        Patient patient = new PatientBuilder().build();
        patient = patient.addNote(new Note("First note"));
        patient = patient.addNote(new Note("Second note"));
        patient = patient.addNote(new Note("Third note"));

        JsonAdaptedPatient jsonPatient = new JsonAdaptedPatient(patient);
        Patient converted = jsonPatient.toModelType();

        assertEquals(3, converted.getNotes().size());
        assertEquals("First note", converted.getNotes().get(0).value);
        assertEquals("Second note", converted.getNotes().get(1).value);
        assertEquals("Third note", converted.getNotes().get(2).value);
    }

    @Test
    public void toModelType_backwardCompatibility_singleNoteToList() throws Exception {
        // Test backward compatibility: single note parameter should be converted to notes list
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
            VALID_APPOINTMENTS, VALID_NOTE, null, VALID_TAG);
        Patient result = patient.toModelType();

        assertEquals(1, result.getNotes().size());
        assertEquals(VALID_NOTE, result.getNotes().get(0).value);
    }

    @Test
    public void toModelType_notesListTakesPrecedence_overSingleNote() throws Exception {
        // When both single note and notes list are provided, notes list should take precedence
        List<String> notes = new ArrayList<>();
        notes.add("Note from list");

        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                VALID_APPOINTMENTS, "Single note", notes, VALID_TAG);
        Patient result = patient.toModelType();

        assertEquals(1, result.getNotes().size());
        assertEquals("Note from list", result.getNotes().get(0).value);
    }
}
