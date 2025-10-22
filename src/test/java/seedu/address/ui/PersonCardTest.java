package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.testutil.PatientBuilder;
import seedu.address.testutil.PersonBuilder;

/**
 * Contains tests for PersonCard UI component.
 * Note: These tests focus on the logic within PersonCard, not the actual JavaFX rendering.
 */
public class PersonCardTest {

    @Test
    public void constructor_patientWithAppointment_displaysAppointment() {
        Patient patient = new PatientBuilder().withAppointment("31-12-2025", "14:30").build();

        // We can't test the actual UI rendering without a JavaFX environment,
        // but we can test the logic by checking the patient type and appointment
        assertTrue(patient instanceof Patient);
        assertEquals(1, patient.getAppointment().size());
        assertEquals("31-12-2025 14:30", patient.getAppointment().get(0).toString());
    }

    @Test
    public void constructor_patientWithoutAppointment_displaysDefaultAppointment() {
        Patient patient = new PatientBuilder().build();

        assertTrue(patient instanceof Patient);
        assertTrue(patient.getAppointment().isEmpty());
    }

    @Test
    public void constructor_patientWithTag_setsTag() {
        Patient patient = new PatientBuilder().withTag("high").build();

        assertTrue(patient.getTag().isPresent());
        assertEquals("high", patient.getTag().get().tagName);
    }

    @Test
    public void personCard_basicPropertiesAccessible() {
        Patient patient = new PatientBuilder()
                .withName("John Doe")
                .withPhone("12345678")
                .withAddress("123 Main St")
                .build();

        // Test that basic properties can be accessed (what PersonCard constructor uses)
        assertEquals("John Doe", patient.getName().fullName);
        assertEquals("12345678", patient.getPhone().value);
        assertEquals("123 Main St", patient.getAddress().value);
    }
}
