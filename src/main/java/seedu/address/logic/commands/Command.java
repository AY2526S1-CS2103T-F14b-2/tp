package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.function.IntFunction;

import seedu.address.commons.core.index.Index;
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
     * Ensures that the provided {@code index} refers to a valid patient in the currently displayed list.
     *
     * @param index the index to validate
     * @param displayedList the list currently shown to the user
     * @throws CommandException if the index is out of bounds for the displayed list
     */
    protected void ensureValidPatientIndex(Index index, Model model) throws CommandException {
        // Default behaviour for callers that pass in a Model is to include the current filtered size
        // in the MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX so that end users see the formatted count.
        ensureValidPatientIndex(index, model.getFilteredPersonList());
    }

    /**
     * Ensures that the provided {@code index} refers to a valid element in the given list.
     *
     * @param index the index to validate
     * @param displayedList the list to validate against
     * @throws CommandException if the index is out of bounds for the list
     */
    protected void ensureValidPatientIndex(Index index, java.util.List<?> displayedList) throws CommandException {
        ensureValidPatientIndex(index, displayedList, size ->
            String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, size));
    }

    /**
     * Ensures that the provided {@code index} refers to a valid element in the given list,
     * using a custom error message provider.
     *
     * @param index the index to validate
     * @param displayedList the list to validate against
     * @param invalidIndexMessageProvider supplies the message to use when the index is invalid;
     *                                   the list size is provided for convenience
     * @throws CommandException if the index is out of bounds for the list
     */
    protected void ensureValidPatientIndex(Index index, java.util.List<?> displayedList,
            IntFunction<String> invalidIndexMessageProvider) throws CommandException {
        requireNonNull(index);
        requireNonNull(displayedList);
        requireNonNull(invalidIndexMessageProvider);

        if (index.getZeroBased() >= displayedList.size()) {
            throw new CommandException(invalidIndexMessageProvider.apply(displayedList.size()));
        }
    }
}
