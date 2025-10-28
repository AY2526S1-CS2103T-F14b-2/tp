package seedu.address.logic.commands;

import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Represents a command with hidden internal logic and the ability to be executed.
 */
public abstract class Command {

    /**
     * Executes the command and returns the result message.
     *
     * @param model {@code Model} which the command should operate on.
     * @return feedback message of the operation result for display
     * @throws CommandException If an error occurs during command execution.
     */
    public abstract CommandResult execute(Model model) throws CommandException;

    /**
     * Gets the error message for invalid patient index.
     *
     * @param model the model to get the patient count from
     * @return the formatted invalid index error message
     */
    protected String getInvalidIndexMessage(Model model) {
        return String.format(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, model.getSize());
    }

}
