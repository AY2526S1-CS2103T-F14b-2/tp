package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY_PATIENT;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditPatientCommand.EditPatientDescriptor;
import seedu.address.testutil.EditPatientDescriptorBuilder;


public class EditPatientDescriptorTest {

    @Test
    public void equals() {
        // same values -> returns true
        EditPatientDescriptor descriptorWithSameValues = new EditPatientDescriptor(DESC_AMY_PATIENT);
        assertTrue(DESC_AMY_PATIENT.equals(descriptorWithSameValues));

        // same object -> returns true
        assertTrue(DESC_AMY_PATIENT.equals(DESC_AMY_PATIENT));

        // null -> returns false
        assertFalse(DESC_AMY_PATIENT.equals(null));

        // different types -> returns false
        assertFalse(DESC_AMY_PATIENT.equals(5));

        // different values -> returns false
        assertFalse(DESC_AMY_PATIENT.equals(DESC_BOB));

        // different name -> returns false
        EditPatientDescriptor editedAmy = new EditPatientDescriptorBuilder(DESC_AMY_PATIENT).withName(VALID_NAME_BOB)
                .build();
        assertFalse(DESC_AMY_PATIENT.equals(editedAmy));

        // different phone -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY_PATIENT).withPhone(VALID_PHONE_BOB).build();
        assertFalse(DESC_AMY_PATIENT.equals(editedAmy));

        // different address -> returns false
        editedAmy = new EditPatientDescriptorBuilder(DESC_AMY_PATIENT).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(DESC_AMY.equals(editedAmy));
    }

    @Test
    public void toStringMethod() {
        EditPatientDescriptor editPatientDescriptor = new EditPatientDescriptor();

        String expected =
                EditPatientCommand.EditPatientDescriptor.class.getCanonicalName()
                        + "{name=" + editPatientDescriptor.getName().orElse(null)
                        + ", phone=" + editPatientDescriptor.getPhone().orElse(null)
                        + ", address=" + editPatientDescriptor.getAddress().orElse(null)
                        + ", tag=" + editPatientDescriptor.getTag().orElse(null)
                        + "}";

        assertEquals(expected, editPatientDescriptor.toString());
    }
}
