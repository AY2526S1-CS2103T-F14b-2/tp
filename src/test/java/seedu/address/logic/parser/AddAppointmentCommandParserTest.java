package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.logic.parser.ParserUtil.MESSAGE_INVALID_INDEX;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.AddAppointmentCommand;
import seedu.address.model.person.Note;




public class AddAppointmentCommandParserTest {

    private static final String FUTURE_DATE = "31-12-2099";
    private static final String FUTURE_TIME = "15:30";

    private final AddAppointmentCommandParser parser = new AddAppointmentCommandParser();

    @Test
    public void parse_validArgs_returnsAddAppointmentCommand() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 " + PREFIX_DATE + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME;
        AddAppointmentCommand expectedCommand = new AddAppointmentCommand(targetIndex, FUTURE_DATE, FUTURE_TIME);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingPrefixes_throwsParseException() {
        String userInput = "1 " + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME;
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidIndex_throwsParseException() {
        String userInput = "0 " + PREFIX_DATE + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME;
        assertParseFailure(parser, userInput, MESSAGE_INVALID_INDEX);
    }

    @Test
    public void parse_missingIndex_throwsParseException() {
        String userInput = PREFIX_DATE + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME;
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgsWithNote_returnsAddAppointmentCommand() {
        Index targetIndex = Index.fromOneBased(1);
        String noteText = "Monthly checkup";
        String userInput = "1 " + PREFIX_DATE + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME 
                + " " + PREFIX_NOTE + noteText;
        AddAppointmentCommand expectedCommand = new AddAppointmentCommand(targetIndex, FUTURE_DATE, FUTURE_TIME, 
                new Note(noteText));
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_validArgsWithoutNote_returnsAddAppointmentCommand() {
        Index targetIndex = Index.fromOneBased(1);
        String userInput = "1 " + PREFIX_DATE + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME;
        AddAppointmentCommand expectedCommand = new AddAppointmentCommand(targetIndex, FUTURE_DATE, FUTURE_TIME, null);
        assertParseSuccess(parser, userInput, expectedCommand);
    }


}
