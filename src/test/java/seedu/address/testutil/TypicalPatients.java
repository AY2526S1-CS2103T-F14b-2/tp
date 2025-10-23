package seedu.address.testutil;

import static seedu.address.testutil.TypicalCaretakers.ALEXENDRA;
import static seedu.address.testutil.TypicalCaretakers.BRAND;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;


/**
 * A utility class containing a list of {@code Patient} objects to be used in tests.
 */
public class TypicalPatients {

    public static final Patient ALICE = new PatientBuilder().withName("Alice Pauline")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253")
            .withTag("high")
            .withCaretaker(ALEXENDRA)
            .build();
    public static final Patient BOB = new PatientBuilder().withName("Benson Meier")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withPhone("98765432")
            .withTag("low")
            .withNote("No peanuts")
            .withCaretaker(BRAND)
            .build();
    public static final Patient CHARLIE = new PatientBuilder().withName("Charlie Brady")
            .withAddress("311, Hougang Ave 3, #02-25")
            .withPhone("98310912")
            .withTag("low")
            .withNote("No seafood")
            .withCaretaker(null)
            .build();

    private TypicalPatients() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical patients.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPatients()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPatients() {
        return new ArrayList<>(Arrays.asList(ALICE, BOB, CHARLIE));
    }
}
