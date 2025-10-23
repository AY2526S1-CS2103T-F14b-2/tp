package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

public class JsonAdaptedCaretakerTest {

    private static final String VALID_NAME = "Alice";
    private static final String VALID_PHONE = "12345678";
    private static final String VALID_ADDRESS = "123 Main Street";
    private static final String VALID_RELATIONSHIP = "FATHER";

    @Test
    public void toModelType_validPatientDetails_returnsPatient() throws Exception {
        JsonAdaptedCaretaker caretaker = new JsonAdaptedCaretaker(VALID_NAME, VALID_PHONE, VALID_ADDRESS,
                VALID_RELATIONSHIP);
        Caretaker expectedCaretaker = new Caretaker(new Name(VALID_NAME), new Phone(VALID_PHONE),
                new Address(VALID_ADDRESS), new Relationship(VALID_RELATIONSHIP));
        assertEquals(expectedCaretaker, caretaker.toModelType());
    }

}
