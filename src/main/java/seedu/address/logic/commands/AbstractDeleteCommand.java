package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Abstract base class for delete commands that operate on indexed items in lists.
 * Provides common functionality for index validation, item retrieval, and deletion workflow.
 *
 * @param <T> the type of item being deleted
 */
public abstract class AbstractDeleteCommand<T> extends Command {

    protected final Index targetIndex;

    /**
     * Creates an AbstractDeleteCommand with the specified target index.
     *
     * @param targetIndex the index of the item to delete
     */
    public AbstractDeleteCommand(Index targetIndex) {
        requireNonNull(targetIndex);
        this.targetIndex = targetIndex;
    }

    @Override
    public final CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<T> targetList = getTargetList(model);

        if (targetIndex.getZeroBased() >= targetList.size()) {
            throw new CommandException(getInvalidIndexMessage(model));
        }

        T itemToDelete = targetList.get(targetIndex.getZeroBased());

        // Perform any pre-deletion validation
        validateDeletion(model, itemToDelete);

        // Delete the item
        deleteItem(model, itemToDelete);

        // Perform any post-deletion updates
        updateModelAfterDeletion(model);

        return new CommandResult(formatSuccessMessage(itemToDelete));
    }

    /**
     * Gets the list of items from which to delete.
     *
     * @param model the model containing the data
     * @return the list of items
     */
    protected abstract List<T> getTargetList(Model model);

    /**
     * Deletes the specified item from the model.
     *
     * @param model the model from which to delete
     * @param item the item to delete
     */
    protected abstract void deleteItem(Model model, T item);

    /**
     * Gets the error message for invalid index.
     *
     * @return the invalid index error message
     */
    protected String getInvalidIndexMessage(Model model) {
        return String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, model.getSize());
    }

    /**
     * Formats the success message for the deleted item.
     *
     * @param deletedItem the item that was deleted
     * @return the formatted success message
     */
    protected abstract String formatSuccessMessage(T deletedItem);

    /**
     * Validates whether the deletion can proceed.
     * Subclasses can override this to add custom validation logic.
     *
     * @param model the model
     * @param itemToDelete the item to be deleted
     * @throws CommandException if validation fails
     */
    protected void validateDeletion(Model model, T itemToDelete) throws CommandException {
        // Default implementation does nothing - subclasses can override
    }

    /**
     * Updates the model after deletion.
     * Subclasses can override this to perform additional updates.
     *
     * @param model the model to update
     */
    protected void updateModelAfterDeletion(Model model) {
        // Default implementation does nothing - subclasses can override
    }

    /**
     * Gets the target index.
     *
     * @return the target index
     */
    public Index getTargetIndex() {
        return targetIndex;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AbstractDeleteCommand)) {
            return false;
        }

        AbstractDeleteCommand<?> otherCommand = (AbstractDeleteCommand<?>) other;
        return targetIndex.equals(otherCommand.targetIndex)
                && this.getClass().equals(other.getClass());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.getClass(), targetIndex.getZeroBased());
    }
}
