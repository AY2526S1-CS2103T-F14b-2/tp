package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Adds a person to the address book.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "appointment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to a patient in the address book. "
            + "Parameters: "
            + PREFIX_DATE + "DATE "
            + PREFIX_TIME + "TIME "
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_DATE + "2023-10-10 "
            + PREFIX_TIME + "14:00 ";

    public static final String MESSAGE_SUCCESS = "New appointment added: %1$s";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book";

    private final Person toAddAppointment;
    private final String date;
    private final String time;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddAppointmentCommand(Person person, String date, String time) {
        requireNonNull(person);
        toAddAppointment = person;
        this.date = date;
        this.time = time;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);

        if (model.hasAppointment(toAddAppointment)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

        model.addAppointment(toAddAppointment, date, time);
        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(toAddAppointment)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AddAppointmentCommand)) {
            return false;
        }

        AddAppointmentCommand otherAddCommand = (AddAppointmentCommand) other;
        return toAddAppointment.equals(otherAddCommand.toAddAppointment)
                && date.equals(otherAddCommand.date)
                && time.equals(otherAddCommand.time);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("toAdd", toAddAppointment)
                .add("date", date)
                .add("time", time)
                .toString();
    }
}

