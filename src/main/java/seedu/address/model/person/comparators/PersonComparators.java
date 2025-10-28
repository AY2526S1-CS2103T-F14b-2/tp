package seedu.address.model.person.comparators;

import java.time.LocalDateTime;
import java.util.Comparator;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

public class PersonComparators {
    public static final Comparator<Person> BY_EARLIEST_APPT =
            Comparator.comparing(
                    (Person p) -> (p instanceof Patient)
                    ? ((Patient) p).getEarliestAppointmentDateTime().orElse(LocalDateTime.MAX)
                    : LocalDateTime.MAX
            ).thenComparing(p -> p.getName().fullName, String.CASE_INSENSITIVE_ORDER);

}
