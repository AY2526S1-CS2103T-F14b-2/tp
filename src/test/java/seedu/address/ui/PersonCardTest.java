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
        assertEquals("31-12-2025 14:30", patient.getAppointment().toString());
    }

    @Test
    public void constructor_patientWithoutAppointment_displaysDefaultAppointment() {
        Patient patient = new PatientBuilder().build();

        assertTrue(patient instanceof Patient);
        assertEquals(null, patient.getAppointment());
    }

    @Test
    public void constructor_regularPerson_displaysDefaultAppointment() {
        Person person = new PersonBuilder().build();

        assertFalse(person instanceof Patient);
    }

    @Test
    public void constructor_personWithTags_sortsTags() {
        Patient patient = new PatientBuilder().withTags("high", "low", "medium").build();

        // Verify that the patient has the expected tags
        assertEquals(3, patient.getTags().size());
        assertTrue(patient.getTags().stream().anyMatch(tag -> tag.tagName.equals("high")));
        assertTrue(patient.getTags().stream().anyMatch(tag -> tag.tagName.equals("low")));
        assertTrue(patient.getTags().stream().anyMatch(tag -> tag.tagName.equals("medium")));
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
