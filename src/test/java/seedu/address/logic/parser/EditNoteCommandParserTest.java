package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.EditNoteCommand;
import seedu.address.logic.commands.EditNoteCommand.EditNoteDescriptor;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.model.person.Note;

public class EditNoteCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditNoteCommand.MESSAGE_USAGE);

    private EditNoteCommandParser parser = new EditNoteCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, "i/1 note/Updated note", MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5 i/1 note/Updated note", MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0 i/1 note/Updated note", MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid note index - negative
        assertParseFailure(parser, "1 i/-1 note/Updated note",
                "Note index must be a positive integer starting from 1.");

        // invalid note index - zero
        assertParseFailure(parser, "1 i/0 note/Updated note",
                "Note index must be a positive integer starting from 1.");

        // invalid note index - not a number
        assertParseFailure(parser, "1 i/abc note/Updated note", "Note index must be a valid positive integer.");

        // empty note content
    assertParseFailure(parser, "1 i/1 note/", NoteCommand.MESSAGE_EMPTY_NOTE);

        // note content with only whitespace
    assertParseFailure(parser, "1 i/1 note/   ", NoteCommand.MESSAGE_EMPTY_NOTE);
    }

    @Test
    public void parse_missingNoteIndex_failure() {
        assertParseFailure(parser, "1 note/Updated note", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_missingNote_failure() {
        assertParseFailure(parser, "1 i/1", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_duplicatePrefix_failure() {
        assertParseFailure(parser, "1 i/1 i/2 note/Updated note",
                "Multiple values specified for the following single-valued field(s): i/");
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        String userInput = "1 i/2 note/Updated note content";

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(2);
        descriptor.setNote(new Note("Updated note content"));

        EditNoteCommand expectedCommand = new EditNoteCommand(INDEX_FIRST_PERSON, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validNoteWithSpecialCharacters_success() {
        String userInput = "1 i/1 note/Note with special chars: @#$%^&*()!";

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(1);
        descriptor.setNote(new Note("Note with special chars: @#$%^&*()!"));

        EditNoteCommand expectedCommand = new EditNoteCommand(INDEX_FIRST_PERSON, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_largeNoteIndex_success() {
        String userInput = "1 i/999 note/Note for large index";

        EditNoteDescriptor descriptor = new EditNoteDescriptor();
        descriptor.setNoteIndex(999);
        descriptor.setNote(new Note("Note for large index"));

        EditNoteCommand expectedCommand = new EditNoteCommand(INDEX_FIRST_PERSON, descriptor);

        assertParseSuccess(parser, userInput, expectedCommand);
    }
}
