package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.ArrayList;
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
    private static final String VALID_APPOINTMENT = "31-12-2025 14:30";
    private static final String VALID_NOTE = "Allergic to peanuts";
    private static final List<JsonAdaptedTag> VALID_TAGS = new ArrayList<>();

    @Test
    public void toModelType_validPatientDetails_returnsPatient() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                VALID_APPOINTMENT, VALID_NOTE, null, VALID_TAGS);
        Patient expectedPatient = new Patient(new Name(VALID_NAME), new Phone(VALID_PHONE),
                new Address(VALID_ADDRESS), patient.toModelType().getTags(),
                new Note(VALID_NOTE), new Appointment("31-12-2025", "14:30"));
        assertEquals(expectedPatient, patient.toModelType());
    }

    @Test
    public void toModelType_nullAppointment_returnsPatientWithNullAppointment() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                null, VALID_NOTE, null, VALID_TAGS);
        Patient result = patient.toModelType();
        assertNull(result.getAppointment());
    }

    @Test
    public void toModelType_nullNote_returnsPatientWithDefaultNote() throws Exception {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                VALID_APPOINTMENT, null, null, VALID_TAGS);
        Patient result = patient.toModelType();
        assertEquals(new Note("NIL"), result.getNote());
    }

    @Test
    public void toModelType_emptyAppointment_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                "   ", VALID_NOTE, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_invalidAppointmentFormat_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                "invalid-appointment", VALID_NOTE, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_appointmentOnlyDate_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                "31-12-2025", VALID_NOTE, null, VALID_TAGS);
        assertThrows(IllegalValueException.class, patient::toModelType);
    }

    @Test
    public void toModelType_invalidAppointmentDateTime_throwsIllegalValueException() {
        JsonAdaptedPatient patient = new JsonAdaptedPatient(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                "invalid-date invalid-time", VALID_NOTE, null, VALID_TAGS);
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
    public void constructor_patientWithoutAppointment_nullAppointment() throws Exception {
        Patient patient = new PatientBuilder().build();
        JsonAdaptedPatient jsonPatient = new JsonAdaptedPatient(patient);

        // Test by converting back to Patient and checking the appointment
        Patient converted = jsonPatient.toModelType();
        assertNull(converted.getAppointment());
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
}
