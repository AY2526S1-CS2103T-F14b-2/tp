package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.DeleteAppointmentCommand;
import seedu.address.logic.parser.exceptions.ParseException;

/**
 * Tests for {@link DeleteAppointmentCommandParser}.
 */
public class DeleteAppointmentCommandParserTest {

    private final DeleteAppointmentCommandParser parser = new DeleteAppointmentCommandParser();

    @Test
    public void parse_validInput_success() {
        String userInput = "2 " + PREFIX_ITEM_INDEX + "1";
        DeleteAppointmentCommand expectedCommand =
                new DeleteAppointmentCommand(Index.fromOneBased(2), 1);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingAppointmentIndex_failure() {
        String userInput = "1";
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAppointmentCommand.MESSAGE_USAGE), ()
                -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidPatientIndex_failure() {
        String userInput = "a " + PREFIX_ITEM_INDEX + "1";
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAppointmentCommand.MESSAGE_USAGE), ()
                -> parser.parse(userInput));
    }

    @Test
    public void parse_invalidAppointmentIndex_failure() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "0";
        assertThrows(ParseException.class,
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteAppointmentCommand.MESSAGE_USAGE), ()
                -> parser.parse(userInput));
    }

    @Test
    public void parse_duplicateAppointmentPrefix_failure() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "1 " + PREFIX_ITEM_INDEX + "2";
        assertThrows(ParseException.class,
            Messages.getErrorMessageForDuplicatePrefixes(PREFIX_ITEM_INDEX), ()
                -> parser.parse(userInput));
    }
}
