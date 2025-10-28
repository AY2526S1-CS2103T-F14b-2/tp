package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.HelpCommand.SHOWING_HELP_MESSAGE;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;


public class HelpCommandTest {
    private Model model = new ModelManager();
    private Model expectedModel = new ModelManager();

    @Test
    public void execute_help_success() {
        CommandResult expectedCommandResult = new CommandResult(SHOWING_HELP_MESSAGE, true, false);
        assertCommandSuccess(new HelpCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void getFormattedHelpMessages_multipleMessages_success() {
        // Arrange
        List<String> originalList = new ArrayList<>(HelpCommand.HELP_MESSAGES);
        HelpCommand.HELP_MESSAGES.clear();
        HelpCommand.HELP_MESSAGES.addAll(Arrays.asList("Add Patient", "Delete Patient", "List Patients"));

        // Act
        String result = HelpCommand.getFormattedHelpMessages();

        // Assert
        String expected = "1. Add Patient\n2. Delete Patient\n3. List Patients\n";
        assertEquals(expected, result);

        HelpCommand.HELP_MESSAGES.clear();
        HelpCommand.HELP_MESSAGES.addAll(originalList);
    }

    @Test
    public void getFormattedHelpMessages_singleMessage_success() {
        List<String> originalList = new ArrayList<>(HelpCommand.HELP_MESSAGES);
        HelpCommand.HELP_MESSAGES.clear();
        HelpCommand.HELP_MESSAGES.add("Find Patient");

        String result = HelpCommand.getFormattedHelpMessages();
        String expected = "1. Find Patient\n";

        assertEquals(expected, result);

        HelpCommand.HELP_MESSAGES.clear();
        HelpCommand.HELP_MESSAGES.addAll(originalList);
    }

    @Test
    public void getFormattedHelpMessages_emptyList_returnsEmptyString() {
        List<String> originalList = new ArrayList<>(HelpCommand.HELP_MESSAGES);
        HelpCommand.HELP_MESSAGES.clear();

        String result = HelpCommand.getFormattedHelpMessages();

        assertEquals("", result);

        HelpCommand.HELP_MESSAGES.clear();
        HelpCommand.HELP_MESSAGES.addAll(originalList);
    }
}
