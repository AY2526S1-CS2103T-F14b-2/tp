package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.StudentInClassGroupPredicatePredicate;

/**
 * Finds and lists all persons in address book whose name contains any of the argument keywords.
 * Keyword matching is case insensitive.
 */
public class FilterByClassCommand extends Command {

    public static final String COMMAND_WORD = "filter";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Finds all persons who are in a certain class group"
            + "and displays them as a list with index numbers.\n"
            + "Parameters: " + PREFIX_CLASSGROUP + "CLASS_NAME "
            + "Example: " + COMMAND_WORD + " " + PREFIX_CLASSGROUP + "Math-1000 " ;

    private final StudentInClassGroupPredicatePredicate predicate;

    public FilterByClassCommand(StudentInClassGroupPredicatePredicate predicate) {
        this.predicate = predicate;
    }

    @Override
    public CommandResult execute(Model model) {
        requireNonNull(model);
        model.updateFilteredPersonList(predicate);
        return new CommandResult(
                String.format(Messages.MESSAGE_PERSONS_LISTED_OVERVIEW, model.getFilteredPersonList().size()));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof FilterByClassCommand)) {
            return false;
        }

        FindCommand otherFilterByClassCommand = (FilterByClassCommand) other;
        return predicate.equals(otherFilterByClassCommand.predicate);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("predicate", predicate)
                .toString();
    }
}