package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAppointmentCommand;
import seedu.address.logic.commands.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new {@code EditAppointmentCommand} object.
 */
public class EditAppointmentCommandParser implements Parser<EditAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of {@code EditAppointmentCommand}
     * and returns an {@code EditAppointmentCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public EditAppointmentCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ITEM_INDEX, PREFIX_DATE, PREFIX_TIME, PREFIX_NOTE);

        Index patientIndex;
        try {
            patientIndex = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditAppointmentCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ITEM_INDEX, PREFIX_DATE, PREFIX_TIME, PREFIX_NOTE);

        if (!argMultimap.getValue(PREFIX_ITEM_INDEX).isPresent()
                || !argMultimap.getValue(PREFIX_DATE).isPresent()
                || !argMultimap.getValue(PREFIX_TIME).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditAppointmentCommand.MESSAGE_USAGE));
        }

        Index appointmentIndex;
        try {
            appointmentIndex = ParserUtil.parseIndex(argMultimap.getValue(PREFIX_ITEM_INDEX).get());
        } catch (ParseException pe) {
            throw new ParseException("Appointment index must be a positive integer starting from 1.", pe);
        }

        String date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        String time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get());

        Note note = null;
        if (argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            note = ParserUtil.parseNote(argMultimap.getValue(PREFIX_NOTE).get());
        }

        Appointment appointment;
        try {
            appointment = new Appointment(date, time, note);
        } catch (IllegalArgumentException iae) {
            throw new ParseException(iae.getMessage(), iae);
        }

        EditAppointmentDescriptor editAppointmentDescriptor = new EditAppointmentDescriptor();
        editAppointmentDescriptor.setAppointmentIndex(appointmentIndex.getOneBased());
        editAppointmentDescriptor.setAppointment(appointment);

        return new EditAppointmentCommand(patientIndex, editAppointmentDescriptor);
    }
}
