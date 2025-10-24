package seedu.address.logic.commands;

import seedu.address.model.Model;
import seedu.address.model.tag.Tag;

import static java.util.Objects.requireNonNull;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

/**
 * Lists all persons in the address book to the user.
 */
public class ListTagCommand extends ListCommand {

    public static final String MESSAGE_SUCCESS = "Listed all patients with Tag: %s";

    private Tag tag;

    public ListTagCommand(Tag tag) {
        this.tag = tag;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(String.format(MESSAGE_SUCCESS, this.tag));
    }
}
