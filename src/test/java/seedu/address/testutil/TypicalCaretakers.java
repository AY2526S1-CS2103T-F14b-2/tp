package seedu.address.testutil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Person;

/**
 * A utility class containing a list of {@code Caretaker} objects to be used in tests.
 */
public class TypicalCaretakers {
    public static final Caretaker ALEXENDRA = new CaretakerBuilder().withName("Alexendra")
            .withAddress("123, Jurong West Ave 6, #08-111")
            .withPhone("94351253")
            .withRelationship("Son")
            .build();
    public static final Caretaker BRAND = new CaretakerBuilder().withName("Brand League")
            .withAddress("311, Clementi Ave 2, #02-25")
            .withPhone("98765432")
            .withRelationship("Father")
            .build();

    private TypicalCaretakers() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with all the typical caretakers.
     */
    public static AddressBook getTypicalAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getTypicalPatients()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getTypicalPatients() {
        return new ArrayList<>(Arrays.asList(ALEXENDRA, BRAND));
    }
}
