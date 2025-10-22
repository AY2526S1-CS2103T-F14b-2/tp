package seedu.address.testutil;

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
            .withPhone("94351253").build();
    public static final Patient BOB = new PatientBuilder().withName("Bob Choo")
            .withAddress("Block 123, Bobby Street 3")
            .withPhone("22222222")
            .withTag("high").build();
    public static final Patient CARL = new PatientBuilder().withName("Carl Junior")
            .withAddress("321, Bedok Ave 2, #02-25")
            .withPhone("98744432")
            .withTag("high").build();
    public static final Patient DANIEL = new PatientBuilder().withName("Daniel Meier")
            .withAddress("10th street")
            .withPhone("87652533").build();

    public static final Patient ELLE = new PatientBuilder().withName("Elle Meyer")
            .withAddress("michegan ave")
            .withPhone("9482224").build();

    public static final Patient FIONA = new PatientBuilder().withName("Fiona Kunz")
            .withAddress("little tokyo")
            .withPhone("9482427").build();

    public static final Patient GEORGE = new PatientBuilder().withName("George Best")
            .withAddress("4th street")
            .withPhone("9482442").build();

    public static final Patient HOON = new PatientBuilder().withName("Hoon Meier").withPhone("8482424")
            .withAddress("little india").build();
    public static final Patient IDA = new PatientBuilder().withName("Ida Mueller").withPhone("8482131")
            .withAddress("chicago ave").build();

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
        return new ArrayList<>(Arrays.asList(ALICE, BOB, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }
}
