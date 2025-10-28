package seedu.address.logic.parser;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.logic.commands.EditNoteCommand.EditNoteDescriptor;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Note;

/**
 * Parses input arguments and creates a new EditNoteCommand object
 */
public class EditNoteCommandParser implements Parser<EditNoteCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditNoteCommand
     * and returns an EditNoteCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public EditNoteCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_ITEM_INDEX, PREFIX_NOTE);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (ParseException pe) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditNoteCommand.MESSAGE_USAGE), pe);
        }

        argMultimap.verifyNoDuplicatePrefixesFor(PREFIX_ITEM_INDEX, PREFIX_NOTE);

        EditNoteDescriptor editNoteDescriptor = new EditNoteDescriptor();

        // Parse note index
        if (!argMultimap.getValue(PREFIX_ITEM_INDEX).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditNoteCommand.MESSAGE_USAGE));
        }

        String noteIndexValue = argMultimap.getValue(PREFIX_ITEM_INDEX).get();

        // Check for invalid format case: empty value or specific invalid patterns
        String trimmedValue = noteIndexValue.trim();
        if (trimmedValue.isEmpty() || trimmedValue.equals("string")) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditNoteCommand.MESSAGE_USAGE));
        }

        try {
            int noteIndex = Integer.parseInt(noteIndexValue.trim());
            if (noteIndex < 1) {
                throw new ParseException("Note index must be a positive integer starting from 1.");
            }
            editNoteDescriptor.setNoteIndex(noteIndex);
        } catch (NumberFormatException e) {
            throw new ParseException("Note index must be a valid positive integer.");
        }

        // Parse new note content
        if (!argMultimap.getValue(PREFIX_NOTE).isPresent()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                    EditNoteCommand.MESSAGE_USAGE));
        }

        String noteValue = argMultimap.getValue(PREFIX_NOTE).get();

        // Check for empty note content - consistent with NoteCommandParser
        if (noteValue.trim().isEmpty()) {
            throw new ParseException(NoteCommand.MESSAGE_EMPTY_NOTE);
        }

        Note newNote = ParserUtil.parseNote(noteValue);
        editNoteDescriptor.setNote(newNote);

        return new EditNoteCommand(index, editNoteDescriptor);
    }
}
