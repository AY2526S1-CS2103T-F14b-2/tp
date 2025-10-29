package seedu.address.model.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import seedu.address.model.AddressBook;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Contains utility methods for populating {@code AddressBook} with sample data.
 */
public class SampleDataUtil {
    public static Person[] getSamplePersons() {
        return new Person[] {
            new Patient(new Name("Alex"), new Phone("87438807"),
                new Address("Blk 30 Geylang Street 29, #06-40"),
                new Tag("low"),
                List.of(new Note("Peanut allergy")),
                List.of(new Appointment("12-11-2025", "09:00"))),
            new Patient(new Name("Bernice Yu"), new Phone("99272758"),
                new Address("Blk 30 Lorong 3 Serangoon Gardens, #07-18"),
                new Tag("medium"),
                List.of(new Note("Requires assistance with eating")),
                List.of(new Appointment("15-01-2026", "11:30"),
                    new Appointment("28-02-2026", "08:45", new Note("Final visit")))),
            new Patient(new Name("Charlotte Oliveiro"), new Phone("93210283"),
                new Address("Blk 11 Ang Mo Kio Street 74, #11-04"),
                new Tag("low"),
                List.of(new Note("Asthma (persistent)")),
                Collections.emptyList()),
            new Patient(new Name("David Li"), new Phone("91031282"),
                new Address("Blk 436 Serangoon Gardens Street 26, #16-43"),
                new Tag("medium"),
                Collections.emptyList(),
                Collections.emptyList()),
            new Patient(new Name("Irfan Ibrahim"), new Phone("92492021"),
                new Address("Blk 47 Tampines Street 20, #17-35"),
                new Tag("high"),
                List.of(new Note("Type 2 Diabetes")),
                List.of(new Appointment("05-03-2026", "14:00"))),
            new Patient(new Name("Roy Balakrishnan"), new Phone("92624417"),
                new Address("Blk 45 Aljunied Street 85, #11-31"),
                null,
                Collections.emptyList(),
                Collections.emptyList())
        };
    }

    public static ReadOnlyAddressBook getSampleAddressBook() {
        AddressBook sampleAb = new AddressBook();
        for (Person samplePerson : getSamplePersons()) {
            sampleAb.addPerson(samplePerson);
        }
        return sampleAb;
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) {
        return Arrays.stream(strings)
                .map(Tag::new)
                .collect(Collectors.toSet());
    }

}
