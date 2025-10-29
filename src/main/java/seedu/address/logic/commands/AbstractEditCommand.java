package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;

/**
 * Abstract base class for edit commands that operate on indexed items in lists.
 * Provides common functionality for index validation, item retrieval, and edit workflow.
 *
 * @param <T> the type of item being edited
 * @param <D> the type of descriptor containing edit details
 */
public abstract class AbstractEditCommand<T, D> extends Command {

    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";

    protected final Index index;
    protected final D editDescriptor;

    /**
     * Creates an AbstractEditCommand with the specified target index and edit descriptor.
     *
     * @param index the index of the item to edit
     * @param editDescriptor the descriptor containing the edit details
     */
    public AbstractEditCommand(Index index, D editDescriptor) {
        requireNonNull(index);
        requireNonNull(editDescriptor);
        this.index = index;
        this.editDescriptor = editDescriptor;
    }

    @Override
    public final CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<T> targetList = getTargetList(model);

        ensureValidPatientIndex(index, targetList, size -> getInvalidIndexMessage(model));

        T itemToEdit = targetList.get(index.getZeroBased());

        // Perform any pre-edit validation
        validateEdit(model, itemToEdit, editDescriptor);

        // Check if any fields are actually being edited
        if (!isAnyFieldEdited(editDescriptor)) {
            throw new CommandException(MESSAGE_NOT_EDITED);
        }

        // Create the edited item
        T editedItem = createEditedItem(itemToEdit, editDescriptor);

        // Check for duplicates if applicable
        validateUniqueItem(model, itemToEdit, editedItem);

        // Update the model
        updateModel(model, itemToEdit, editedItem);

        // Perform any post-edit updates
        updateModelAfterEdit(model);

        return new CommandResult(formatSuccessMessage(editedItem));
    }

    /**
     * Gets the list of items from which to edit.
     *
     * @param model the model containing the data
     * @return the list of items
     */
    protected abstract List<T> getTargetList(Model model);

    /**
     * Gets the error message for invalid index.
     *
     * @return the invalid index error message
     */
    protected String getInvalidIndexMessage(Model model) {
        return String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX,
                model.getFilteredPersonList().size());
    }

    /**
     * Validates whether the edit can proceed.
     * Subclasses can override this to add custom validation logic.
     *
     * @param model the model
     * @param itemToEdit the item to be edited
     * @param editDescriptor the edit descriptor
     * @throws CommandException if validation fails
     */
    protected void validateEdit(Model model, T itemToEdit, D editDescriptor) throws CommandException {
        // Default implementation does nothing - subclasses can override
    }

    /**
     * Checks if any field is being edited in the descriptor.
     *
     * @param editDescriptor the edit descriptor to check
     * @return true if at least one field is being edited
     */
    protected abstract boolean isAnyFieldEdited(D editDescriptor);

    /**
     * Creates the edited item with the specified changes.
     *
     * @param itemToEdit the original item
     * @param editDescriptor the descriptor containing the changes
     * @return the edited item
     */
    protected abstract T createEditedItem(T itemToEdit, D editDescriptor);

    /**
     * Validates that the edited item doesn't create a duplicate.
     * Subclasses can override this to add duplicate checking logic.
     *
     * @param model the model
     * @param originalItem the original item
     * @param editedItem the edited item
     * @throws CommandException if the edited item would create a duplicate
     */
    protected void validateUniqueItem(Model model, T originalItem, T editedItem) throws CommandException {
        // Default implementation does nothing - subclasses can override for duplicate checking
    }

    /**
     * Updates the model with the edited item.
     *
     * @param model the model to update
     * @param originalItem the original item
     * @param editedItem the edited item
     */
    protected abstract void updateModel(Model model, T originalItem, T editedItem);

    /**
     * Updates the model after editing.
     * Subclasses can override this to perform additional updates.
     *
     * @param model the model to update
     */
    protected void updateModelAfterEdit(Model model) {
        // Default implementation does nothing - subclasses can override
    }

    /**
     * Formats the success message for the edited item.
     *
     * @param editedItem the item that was edited
     * @return the formatted success message
     */
    protected abstract String formatSuccessMessage(T editedItem);

    /**
     * Gets the target index.
     *
     * @return the target index
     */
    public Index getIndex() {
        return index;
    }

    /**
     * Gets the edit descriptor.
     *
     * @return the edit descriptor
     */
    public D getEditDescriptor() {
        return editDescriptor;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof AbstractEditCommand)) {
            return false;
        }

        AbstractEditCommand<?, ?> otherCommand = (AbstractEditCommand<?, ?>) other;
        return index.equals(otherCommand.index)
                && editDescriptor.equals(otherCommand.editDescriptor)
                && this.getClass().equals(other.getClass());
    }

    @Override
    public int hashCode() {
        return java.util.Objects.hash(this.getClass(), index.getZeroBased(), editDescriptor);
    }
}
