package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
/**
 * Adds a person to the address book.
 */
public class AddAppointmentCommand extends Command {

    public static final String COMMAND_WORD = "appt";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds an appointment to a patient in the address book. "
        + "Parameters: INDEX "
        + PREFIX_DATE + "DATE "
        + PREFIX_TIME + "TIME "
        + "[" + PREFIX_NOTE + "NOTE]" + "\n"
        + "Example: " + COMMAND_WORD + " 1 "
        + PREFIX_DATE + "10-10-2026 "
        + PREFIX_TIME + "14:00 "
        + PREFIX_NOTE + "Monthly checkup";

    public static final String MESSAGE_SUCCESS = "Appointment created: %1$s; %2$s%3$s\n"
        + "For %4$s; " + "Phone: %5$s";
    public static final String MESSAGE_DUPLICATE_APPOINTMENT = "This appointment already exists in the address book";

    private final Index targetIndex;
    private final String date;
    private final String time;
    private final Note desc;

    public AddAppointmentCommand(Index targetIndex, String date, String time) {
        this(targetIndex, date, time, null);
    }

    /**
     * Creates an addappointmentcommand object
     * @param targetIndex Index patient index from list
     * @param date Appointment date
     * @param time Appointment time
     * @param desc Appointment description
     */
    public AddAppointmentCommand(Index targetIndex, String date, String time, Note desc) {
        requireNonNull(targetIndex);
        requireNonNull(date);
        requireNonNull(time);
        this.targetIndex = targetIndex;
        this.date = date;
        this.time = time;
        this.desc = desc;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX,
                    model.getSize()));
        }

        Person personToAddAppointment = lastShownList.get(targetIndex.getZeroBased());

        try {
            Patient updatedPatient = model.addAppointment(personToAddAppointment, date, time, desc);
            List<Appointment> appointments = updatedPatient.getAppointment();
            Appointment newestAppointment = appointments.get(appointments.size() - 1);
            String parsedNote = desc == null ? "" : "; Note: " + this.desc;
            String successMessage = String.format(MESSAGE_SUCCESS, this.date,
                this.time, parsedNote, updatedPatient.getName(),
                updatedPatient.getPhone());
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

        AddAppointmentCommand otherAppt = (AddAppointmentCommand) other;
        return targetIndex.equals(otherAppt.targetIndex)
                && date.equals(otherAppt.date)
                && time.equals(otherAppt.time)
                && Objects.equals(desc, otherAppt.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, date, time, desc);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
        .add("targetIndex", targetIndex)
                .add("date", date)
                .add("time", time)
                .add("description", desc)
                .toString();
    }
}
