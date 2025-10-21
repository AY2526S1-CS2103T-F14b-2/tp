package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Person;

/**
 * Deletes a person identified using it's displayed index from the address book.
 */
public class DeleteCommand extends AbstractDeleteCommand<Person> {

    public static final String COMMAND_WORD = "delete";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the person identified by the index number used in the displayed person list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PERSON_SUCCESS = "Deleted Person: %1$s";

    public DeleteCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    protected List<Person> getTargetList(Model model) {
        return model.getFilteredPersonList();
    }

    @Override
    protected void deleteItem(Model model, Person person) {
        model.deletePerson(person);
    }

    @Override
    protected String getInvalidIndexMessage() {
        return Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX;
    }

    @Override
    protected String formatSuccessMessage(Person deletedPerson) {
        return String.format(MESSAGE_DELETE_PERSON_SUCCESS, Messages.format(deletedPerson));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteCommand)) {
            return false;
        }

        return super.equals(other);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", getTargetIndex())
                .toString();
    }
}
