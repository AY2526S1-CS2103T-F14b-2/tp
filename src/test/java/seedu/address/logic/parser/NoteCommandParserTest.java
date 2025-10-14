package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.INVALID_NOTE_DESC;
import static seedu.address.logic.commands.CommandTestUtil.NOTE_DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.NOTE_DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NOTE_BOB;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.NoteCommand;
import seedu.address.model.person.Note;

public class NoteCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, NoteCommand.MESSAGE_USAGE);

    private NoteCommandParser parser = new NoteCommandParser();

    @Test
    public void parse_missingParts_failure() {
        // no index specified
        assertParseFailure(parser, NOTE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // no field specified
        assertParseFailure(parser, "1", MESSAGE_INVALID_FORMAT);

        // no index and no field specified
        assertParseFailure(parser, "", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidPreamble_failure() {
        // negative index
        assertParseFailure(parser, "-5" + NOTE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + NOTE_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "1 some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_invalidValue_failure() {
        // invalid note (too long)
        assertParseFailure(parser, "1" + INVALID_NOTE_DESC, Note.MESSAGE_LENGTH_CONSTRAINTS);
    }

    @Test
    public void parse_allFieldsSpecified_success() {
        Index targetIndex = INDEX_SECOND_PERSON;
        String userInput = targetIndex.getOneBased() + NOTE_DESC_BOB;

        NoteCommand expectedCommand = new NoteCommand(targetIndex, new Note(VALID_NOTE_BOB));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_multipleRepeatedFields_acceptsLast() {
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + NOTE_DESC_AMY + NOTE_DESC_BOB;

        NoteCommand expectedCommand = new NoteCommand(targetIndex, new Note(VALID_NOTE_BOB));

        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validArgs_returnsNoteCommand() {
        // normal note
        Index targetIndex = INDEX_FIRST_PERSON;
        String userInput = targetIndex.getOneBased() + NOTE_DESC_AMY;
        NoteCommand expectedCommand = new NoteCommand(targetIndex, new Note(VALID_NOTE_AMY));
        assertParseSuccess(parser, userInput, expectedCommand);

        // note with special characters
        String specialNote = " " + PREFIX_NOTE + "Patient has diabetes and hypertension!";
        userInput = targetIndex.getOneBased() + specialNote;
        expectedCommand = new NoteCommand(targetIndex, new Note("Patient has diabetes and hypertension!"));
        assertParseSuccess(parser, userInput, expectedCommand);

        // note with numbers
        String numberNote = " " + PREFIX_NOTE + "Patient visited 3 times this month";
        userInput = targetIndex.getOneBased() + numberNote;
        expectedCommand = new NoteCommand(targetIndex, new Note("Patient visited 3 times this month"));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptyNote_failure() {
        assertParseFailure(parser, "1 " + PREFIX_NOTE, NoteCommand.MESSAGE_EMPTY_NOTE);
        assertParseFailure(parser, "1 " + PREFIX_NOTE + " ", NoteCommand.MESSAGE_EMPTY_NOTE);
    }

    @Test
    public void parse_whitespaceNote_failure() {
        assertParseFailure(parser, "1 " + PREFIX_NOTE + "   ", NoteCommand.MESSAGE_EMPTY_NOTE);
        assertParseFailure(parser, "1 " + PREFIX_NOTE + "\t\t", NoteCommand.MESSAGE_EMPTY_NOTE);
    }
}
