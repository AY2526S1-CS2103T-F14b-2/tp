package seedu.address.testutil;

import java.util.ArrayList;
import java.util.List;

import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;



/**
 * A utility class containing an empty list of {@code Person} objects to be used in tests.
 */
public class EmptyPersons {

    private EmptyPersons() {} // prevents instantiation

    /**
     * Returns an {@code AddressBook} with zero persons.
     */
    public static AddressBook getEmptyAddressBook() {
        AddressBook ab = new AddressBook();
        for (Person person : getEmptyPersons()) {
            ab.addPerson(person);
        }
        return ab;
    }

    public static List<Person> getEmptyPersons() {
        return new ArrayList<>();
    }
}
