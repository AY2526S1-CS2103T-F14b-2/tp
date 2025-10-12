package seedu.address.logic.parser;

import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import seedu.address.logic.commands.AddAppointmentCommand;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new {@code AddAppointmentCommand} object.
 */
public class AddAppointmentCommandParser implements Parser<AddAppointmentCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the {@code AddAppointmentCommand}
     * and returns an {@code AddAppointmentCommand} object for execution.
     *
     * @throws ParseException if the user input does not conform to the expected format.
     */
    @Override
    public AddAppointmentCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_DATE, PREFIX_TIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_DATE, PREFIX_TIME) || argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE));
        }

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    AddAppointmentCommand.MESSAGE_USAGE), pe);
        }

        String date = ParserUtil.parseDate(argMultimap.getValue(PREFIX_DATE).get());
        String time = ParserUtil.parseTime(argMultimap.getValue(PREFIX_TIME).get());

        return new AddAppointmentCommand(index, date, time);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }
}
