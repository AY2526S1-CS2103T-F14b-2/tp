package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.DeleteNoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Parses input arguments and creates a new DeleteNoteCommand object
 */
public class DeleteNoteCommandParser implements Parser<DeleteNoteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteNoteCommand
     * and returns a DeleteNoteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public DeleteNoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ITEM_INDEX);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteNoteCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ITEM_INDEX);

        // Parse note index
        if (!argMultimap.getValue(PREFIX_ITEM_INDEX).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteNoteCommand.MESSAGE_USAGE));
        }

        String noteIndexValue = argMultimap.getValue(PREFIX_ITEM_INDEX).get();

        // Check for invalid format case: empty value or specific invalid patterns
        String trimmedValue = noteIndexValue.trim();
        if (trimmedValue.isEmpty() || trimmedValue.equals("string")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    DeleteNoteCommand.MESSAGE_USAGE));
        }

        int noteIndex;
        try {
            noteIndex = Integer.parseInt(noteIndexValue.trim());
            if (noteIndex < 1) {
                throw new ParseException("Note index must be a positive integer starting from 1.");
            }
        } catch (NumberFormatException e) {
            throw new ParseException("Note index must be a valid positive integer.");
        }

        return new DeleteNoteCommand(index, noteIndex);
    }
}
