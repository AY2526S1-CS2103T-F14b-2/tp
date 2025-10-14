package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new NoteCommand object
 */
public class NoteCommandParser implements Parser<NoteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the NoteCommand
     * and returns a NoteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public NoteCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NOTE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE), pe);
        }

        if (!argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE));
        }

        String noteValue = argMultimap.getValue(PREFIX_NOTE).get();

        if (noteValue.trim().isEmpty()) {
            throw new ParseException(NoteCommand.MESSAGE_EMPTY_NOTE);
        }

        Note note;
        try {
            note = ParserUtil.parseNote(noteValue);
        } catch (ParseException pe) {
            throw pe; // Let the specific error message bubble up
        }

        return new NoteCommand(index, note);
    }
}
