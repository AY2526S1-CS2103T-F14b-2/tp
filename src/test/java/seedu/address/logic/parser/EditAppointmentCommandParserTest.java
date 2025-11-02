package seedu.address.logic.parser;

import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.parser.CliSyntax.PREFIX_DATE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TIME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseSuccess;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.EditAppointmentCommand;
import seedu.address.logic.commands.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.model.person.Note;

/**
 * Tests for {@link EditAppointmentCommandParser}.
 */
public class EditAppointmentCommandParserTest {

    private static final String FUTURE_DATE = "31-12-2099";
    private static final String FUTURE_TIME = "12:00";

    private final EditAppointmentCommandParser parser = new EditAppointmentCommandParser();

    @Test
    public void parse_allFieldsPresentWithNote_success() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "2 "
                + PREFIX_DATE + FUTURE_DATE + " "
                + PREFIX_TIME + FUTURE_TIME + " "
                + PREFIX_NOTE + "Follow up";

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(2);
        descriptor.setDate(FUTURE_DATE);
        descriptor.setTime(FUTURE_TIME);
        descriptor.setNote(new Note("Follow up"));

        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(Index.fromOneBased(1), descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_allFieldsPresentWithoutNote_success() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "1 "
                + PREFIX_DATE + FUTURE_DATE + " "
                + PREFIX_TIME + FUTURE_TIME;

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setDate(FUTURE_DATE);
        descriptor.setTime(FUTURE_TIME);

        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(Index.fromOneBased(1), descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_missingAppointmentIndex_failure() {
        String userInput = "1 " + PREFIX_DATE + FUTURE_DATE + " " + PREFIX_TIME + FUTURE_TIME;
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_onlyTime_success() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "1 " + PREFIX_TIME + FUTURE_TIME;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setTime(FUTURE_TIME);
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(Index.fromOneBased(1), descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_onlyDate_success() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "1 " + PREFIX_DATE + FUTURE_DATE;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setDate(FUTURE_DATE);
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(Index.fromOneBased(1), descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_onlyNote_success() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "1 " + PREFIX_NOTE + "Updated note";
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setNote(new Note("Updated note"));
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(Index.fromOneBased(1), descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_emptyNote_clearsNote() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "1 " + PREFIX_NOTE;
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.clearNote();
        EditAppointmentCommand expectedCommand = new EditAppointmentCommand(Index.fromOneBased(1), descriptor);
        assertParseSuccess(parser, userInput, expectedCommand);
    }

    @Test
    public void parse_invalidPatientIndex_failure() {
        String userInput = "a " + PREFIX_ITEM_INDEX + "1 "
                + PREFIX_DATE + FUTURE_DATE + " "
                + PREFIX_TIME + FUTURE_TIME;
        assertParseFailure(parser, userInput,
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAppointmentCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_invalidAppointmentIndex_failure() {
        String userInput = "1 " + PREFIX_ITEM_INDEX + "0 "
                + PREFIX_DATE + FUTURE_DATE + " "
                + PREFIX_TIME + FUTURE_TIME;
        assertParseFailure(parser, userInput,
        String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditAppointmentCommand.MESSAGE_USAGE));
    }
}
