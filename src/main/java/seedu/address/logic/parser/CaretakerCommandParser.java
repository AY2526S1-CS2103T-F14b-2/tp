package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.*;

import java.util.Optional;
import java.util.stream.Stream;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.CaretakerCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * Parses input arguments and creates a new CaretakerCommand object
 */
public class CaretakerCommandParser implements Parser<CaretakerCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the CaretakerCommand
     * and returns an CaretakerCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public CaretakerCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_RELATIONSHIP);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CaretakerCommand.MESSAGE_USAGE), pe);
        }

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_PHONE, PREFIX_RELATIONSHIP)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, CaretakerCommand.MESSAGE_USAGE));
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_NAME, PREFIX_PHONE, PREFIX_ADDRESS, PREFIX_RELATIONSHIP);

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Relationship relationship = ParserUtil.parseRelationship(argMultimap.getValue(PREFIX_RELATIONSHIP).get());

        Address address = null;
        Optional<String> addressValue = argMultimap.getValue(PREFIX_ADDRESS);
        if (addressValue.isPresent()) {
            address = ParserUtil.parseAddress(addressValue.get());
        }

        Caretaker caretaker = new Caretaker(name, phone, address, relationship);

        return new CaretakerCommand(index, caretaker);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
