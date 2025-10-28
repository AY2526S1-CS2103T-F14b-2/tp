package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.Comparator;

import java.util.Optional;
import javafx.collections.ObservableList;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.comparators.PersonComparators;

public class SortAppointmentCommand extends Command {
    public static final String COMMAND_WORD = "sortappt";
    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Patients sorted by earliest appointmnet (soonest first).\n"
            + "Example: " + COMMAND_WORD + " appt\n";

    public static final String MESSAGE_SUCCESS = "Patients sorted by earliest appointment!";
    public static final String MESSAGE_NO_PATIENTS = "There are no patients in MediSaveBook to sort!";
    public static final String MESSAGE_NO_APPOINTMENTS = "No appointments to sort!";

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        ObservableList<Person> personList = model.getFilteredPersonList();

        boolean hasPatient = personList.stream().anyMatch(p -> p instanceof Patient);
        if (!hasPatient) {
            throw new CommandException(MESSAGE_NO_PATIENTS);
        }

        boolean hasAppointments = personList.stream()
                .filter(p -> p instanceof Patient)
                .map(p -> (Patient) p)
                .map(Patient::getEarliestAppointmentDateTime)
                .anyMatch(Optional::isPresent);
        if (!hasAppointments) {
            throw new CommandException(MESSAGE_NO_APPOINTMENTS);
        }

        Comparator<Person> cmp = PersonComparators.BY_EARLIEST_APPT;
        model.sortPersons(cmp);
        return new CommandResult(MESSAGE_SUCCESS);
    }


    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        return other instanceof SortAppointmentCommand;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).toString();
    }

}



