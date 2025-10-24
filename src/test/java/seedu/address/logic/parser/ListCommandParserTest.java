package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_TAG_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NAME_DESC_ALICE;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_HIGH;
import static seedu.address.logic.commands.CommandTestUtil.TAG_DESC_BLANK;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIGH;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.commands.ListTagCommand;
import seedu.address.model.tag.Tag;

public class ListCommandParserTest {

    private final ListCommandParser parser = new ListCommandParser();


    @Test
    public void parse_emptyArgs_returnsListCommand() {
        assertParseSuccess(parser, "", new ListCommand());
    }

    @Test
    public void parse_validTag_returnsListTagCommand() {
        ListTagCommand expected = new ListTagCommand(new Tag(VALID_TAG_HIGH));
        assertParseSuccess(parser, TAG_DESC_HIGH, expected);
    }

    @Test
    public void parse_invalidTagPrefix_failure() {
        assertParseFailure(parser, NAME_DESC_ALICE, String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_missingTagPrefix_nonEmptyArgs_failure() {
        assertParseFailure(parser,
                "high",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_blankTag_failure() {
        // present but blank should fail Tag parsing
        assertParseFailure(parser, TAG_DESC_BLANK, Tag.MESSAGE_CONSTRAINTS);
    }

    @Test
    public void parse_invalidTagSyntax_failure() {
        assertParseFailure(parser, INVALID_TAG_DESC, Tag.MESSAGE_CONSTRAINTS);
    }
}
