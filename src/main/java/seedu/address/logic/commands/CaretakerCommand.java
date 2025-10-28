package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Adds a caretaker to a specified patient in the address book.
 */
public class CaretakerCommand extends Command {
    public static final String COMMAND_WORD = "caretaker";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a caretaker to the specified patient.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + PREFIX_ADDRESS + "ADDRESS "
            + PREFIX_RELATIONSHIP + "RELATIONSHIP\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_RELATIONSHIP + "Father";

    public static final String MESSAGE_SUCCESS = "Caretaker created: %1$s\n"
            + "%2$s";
    public static final String MESSAGE_PATIENT_HAS_CARETAKER = "Patient already has a caretaker.\n"
            + "%1$s";
    public static final String MESSAGE_CARETAKER_ALREADY_EXISTS = "This caretaker already exists as a "
            + "patient in the address book.";

    private final Index targetIndex;
    private final Caretaker caretaker;

    /**
     * Creates an AddCommand to add the specified {@code Person}
     */
    public CaretakerCommand(Index targetIndex, Caretaker caretaker) {
        requireNonNull(targetIndex);
        requireNonNull(caretaker);
        this.targetIndex = targetIndex;
        this.caretaker = caretaker;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        Person personToAddCaretaker = lastShownList.get(targetIndex.getZeroBased());

        if (!(personToAddCaretaker instanceof Patient)) {
            throw new CommandException(Messages.MESSAGE_REQUIRE_PATIENT);
        }

        Patient patientToAddCaretaker = (Patient) personToAddCaretaker;

        if (patientToAddCaretaker.getCaretaker() != null) {
            String caretakerExistsMessage = String.format(MESSAGE_PATIENT_HAS_CARETAKER,
                    Messages.shortFormat(patientToAddCaretaker));
            throw new CommandException(caretakerExistsMessage);
        }

        if (model.hasPerson(caretaker)) {
            throw new CommandException(MESSAGE_CARETAKER_ALREADY_EXISTS);
        }

        try {
            Patient updatedPatient = patientToAddCaretaker.addCaretaker(caretaker);
            model.setPerson(personToAddCaretaker, updatedPatient);
            String successMessage = String.format(MESSAGE_SUCCESS, Messages.format(caretaker),
                    Messages.shortFormat(updatedPatient));
            return new CommandResult(successMessage);
        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }

    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof CaretakerCommand)) {
            return false;
        }

        CaretakerCommand otherCaretakerCommand = (CaretakerCommand) other;
        return targetIndex.equals(otherCaretakerCommand.targetIndex)
                && caretaker.equals(otherCaretakerCommand.caretaker);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, caretaker);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("caretaker", caretaker)
                .toString();
    }
}
