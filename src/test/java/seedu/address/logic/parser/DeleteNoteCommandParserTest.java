package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.DeleteNoteCommand;

/**
 * As we are only doing white-box testing, our test cases do not cover path variations
 * outside of the DeleteNoteCommand code. For example, inputs "1" and "1 abc" take the
 * same path through the DeleteNoteCommand, and therefore we test only one of them.
 * The path variation for those two cases occur inside the ParserUtil, and
 * therefore should be covered by the ParserUtilTest.
 */
public class DeleteNoteCommandParserTest {

    private DeleteNoteCommandParser parser = new DeleteNoteCommandParser();

    @Test
    public void parse_validArgs_returnsDeleteNoteCommand() {
        assertParseSuccess(parser, "1 i/2", new DeleteNoteCommand(INDEX_FIRST_PERSON, 2));
    }

    @Test
    public void parse_invalidArgs_throwsParseException() {
        // missing patient index
        assertParseFailure(parser, "i/1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteNoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingNoteIndex_throwsParseException() {
        assertParseFailure(parser, "1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteNoteCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidNoteIndex_throwsParseException() {
        // zero note index
        assertParseFailure(parser, "1 i/0", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteNoteCommand.MESSAGE_USAGE));

        // negative note index
        assertParseFailure(parser, "1 i/-1", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteNoteCommand.MESSAGE_USAGE));

        // non-numeric note index
        assertParseFailure(parser, "1 i/abc", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
            DeleteNoteCommand.MESSAGE_USAGE));

        // empty note index
        assertParseFailure(parser, "1 i/", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                DeleteNoteCommand.MESSAGE_USAGE));
    }
}
