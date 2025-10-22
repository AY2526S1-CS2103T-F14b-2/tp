package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_BOB;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalCaretakers.ALEXENDRA;
import static seedu.address.testutil.TypicalCaretakers.BRAND;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;
import seedu.address.testutil.CaretakerBuilder;

/**
 * Unit tests for {@link Caretaker}.
 */
public class CaretakerTest {
    @Test
    public void isSameCaretaker() {
        // same object -> returns true
        assertTrue(BRAND.isSamePerson(BRAND));

        // null -> returns false
        assertFalse(BRAND.isSamePerson(null));

        // name differs in case, all other attributes same -> returns false
        Caretaker editedBrand = new CaretakerBuilder(BRAND).withName(VALID_NAME_BOB.toLowerCase()).build();
        assertFalse(BRAND.isSamePerson(editedBrand));

        // different relationship, all other attributes same -> returns true
        editedBrand = new CaretakerBuilder(BRAND).withRelationship("Different").build();
        assertTrue(BRAND.isSamePerson(editedBrand));

        // name has trailing spaces, all other attributes same -> returns false
        String nameWithTrailingSpaces = VALID_NAME_BOB + " ";
        editedBrand = new CaretakerBuilder(BRAND).withName(nameWithTrailingSpaces).build();
        assertFalse(BRAND.isSamePerson(editedBrand));
    }

    @Test
    public void equals() {
        // same values -> returns true
        Caretaker brandCopy = new CaretakerBuilder(BRAND).build();
        assertTrue(BRAND.equals(brandCopy));

        // same object -> returns true
        assertTrue(BRAND.equals(BRAND));

        // null -> returns false
        assertNotEquals(BRAND, null);

        // different type -> returns false
        assertFalse(BRAND.equals(5));

        // different caretaker -> returns false
        assertFalse(BRAND.equals(ALEXENDRA));

        // different name -> returns false
        Caretaker editedBrand = new CaretakerBuilder(BRAND).withName(VALID_NAME_BOB).build();
        assertFalse(BRAND.equals(editedBrand));

        // different phone -> returns false
        editedBrand = new CaretakerBuilder(BRAND).withPhone(VALID_PHONE_BOB).build();
        assertFalse(BRAND.equals(editedBrand));

        // different address -> returns false
        editedBrand = new CaretakerBuilder(BRAND).withAddress(VALID_ADDRESS_BOB).build();
        assertFalse(BRAND.equals(editedBrand));

        // different relationship -> returns false
        editedBrand = new CaretakerBuilder(BRAND).withRelationship("Father-in-law").build();
        assertFalse(BRAND.equals(editedBrand));
    }

    @Test
    public void toStringMethod() {
        String expected = Caretaker.class.getCanonicalName()
                + "{name=" + BRAND.getName()
                + ", phone=" + BRAND.getPhone()
                + ", address=" + BRAND.getAddress()
                + ", relationship=" + BRAND.getRelationship()
                + "}";
        assertEquals(expected, BRAND.toString());
    }

    @Test
    public void hashCode_equalCaretakers_sameHashCode() {
        Caretaker caretaker1 = new CaretakerBuilder(BRAND).build();
        Caretaker caretaker2 = new CaretakerBuilder(BRAND).build();
        assertEquals(caretaker1.hashCode(), caretaker2.hashCode());
    }

    @Test
    public void hashCode_differentCaretakers_differentHashCode() {
        assertNotEquals(BRAND.hashCode(), ALEXENDRA.hashCode());
    }

    @Test
    public void constructor_nullFields_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new Caretaker(null, new Phone("91234567"), new Address("123 Street"),
                        new Relationship("Brother")));
        assertThrows(NullPointerException.class, () ->
                new Caretaker(new Name("John Doe"), null, new Address("123 Street"),
                        new Relationship("Brother")));
        assertThrows(NullPointerException.class, () ->
                new Caretaker(new Name("John Doe"), new Phone("91234567"), null,
                        new Relationship("Brother")));
        assertThrows(NullPointerException.class, () ->
                new Caretaker(new Name("John Doe"), new Phone("91234567"), new Address("123 Street"), null));
    }

    @Test
    public void isSamePerson_withNonCaretaker_returnsFalse() {
        Patient patient = new Patient(new Name("John Doe"), new Phone("91234567"), new Address("123 Street"),
                new Tag("high"));
        assertFalse(BRAND.isSamePerson(patient));
    }

    @Test
    public void isSamePerson_sameNameAndPhoneDifferentCase_returnsTrue() {
        Caretaker caretaker1 = new CaretakerBuilder().withName("John Doe").withPhone("12345678").build();
        Caretaker caretaker2 = new CaretakerBuilder().withName("JOHN DOE").withPhone("12345678").build();
        assertTrue(caretaker1.isSamePerson(caretaker2));
    }

    @Test
    public void isSamePerson_differentPhoneNumbers_returnsFalse() {
        Caretaker caretaker1 = new CaretakerBuilder().withName("John Doe").withPhone("12345678").build();
        Caretaker caretaker2 = new CaretakerBuilder().withName("John Doe").withPhone("87654321").build();
        assertFalse(caretaker1.isSamePerson(caretaker2));
    }
}
