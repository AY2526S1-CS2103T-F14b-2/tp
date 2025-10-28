package seedu.address.model.person.comparators;

import java.time.LocalDateTime;
import java.util.Comparator;

import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Contains comparators which orders objects of type Person or a subclass of Person
 */
public class PersonComparators {

    /**
     * Comparator that orders {@Link Person}s by their earliest appointment (earliest first). If two patients
     * have the same date and time for their earliest appointment, their name will be used as a tiebreaker, where
     * they will be arranged in alphabetical order
     */
    public static final Comparator<Person> BY_EARLIEST_APPT =
            Comparator.comparing((Person p) -> (p instanceof Patient)
                    ? ((Patient) p).getEarliestAppointmentDateTime().orElse(LocalDateTime.MAX)
                    : LocalDateTime.MAX
            ).thenComparing(p -> p.getName().fullName, String.CASE_INSENSITIVE_ORDER);

}
