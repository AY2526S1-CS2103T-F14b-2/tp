package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;



/**
 * Adds a person to the address book.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "appointment";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to a patient in the address book. "
        + "Parameters: INDEX "
        + PREFIX_DATE + "DATE "
        + PREFIX_TIME + "TIME\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_DATE + "10-10-2023 "
        + PREFIX_TIME + "14:00";

    public static final String MESSAGE_SUCCESS = "New appointment added: %1$s";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book";

    private final Index targetIndex;
    private final String date;
    private final String time;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public AddAppointmentCommand(Index targetIndex, String date, String time) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
        this.date = date;
        this.time = time;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAddAppointment = lastShownList.get(targetIndex.getZeroBased());

        if (model.hasAppointment(personToAddAppointment)) {
            throw new CommandException(MESSAGE_DUPLICATE_APPOINTMENT);
        }

        try {
            Patient updatedPatient = model.addAppointment(personToAddAppointment, date, time);
            String successMessage = String.format(MESSAGE_SUCCESS, Messages.format(updatedPatient));
            return new CommandResult(successMessage);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

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
        return targetIndex.equals(otherAddCommand.targetIndex)
            && date.equals(otherAddCommand.date)
                    && time.equals(otherAddCommand.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, date, time);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        .add("targetIndex", targetIndex)
                .add("date", date)
                .add("time", time)
                .toString();
    }
}

