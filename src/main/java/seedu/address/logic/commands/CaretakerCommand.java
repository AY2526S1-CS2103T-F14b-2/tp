package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * Adds a caretaker to a specified patient in the address book.
 */
public class CaretakerCommand extends Command {
    public static final String COMMAND_WORD = "caretaker";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a caretaker to the specified patient.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NAME + "NAME "
            + PREFIX_PHONE + "PHONE "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + PREFIX_RELATIONSHIP + "RELATIONSHIP\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NAME + "John Doe "
            + PREFIX_PHONE + "98765432 "
            + PREFIX_ADDRESS + "311, Clementi Ave 2, #02-25 "
            + PREFIX_RELATIONSHIP + "Father";

    public static final String MESSAGE_SUCCESS = "Caretaker created: %1$s\n"
            + "%2$s";
    public static final String MESSAGE_PATIENT_HAS_CARETAKER = "The patient at index %1$s already has a caretaker.";
    public static final String MESSAGE_CARETAKER_ALREADY_EXISTS = "This caretaker already exists as a "
            + "patient in the address book.";

    private final Index targetIndex;
    private final Name name;
    private final Phone phone;
    private final Relationship relationship;
    private final Optional<Address> addressOpt;

    /**
     * Creates a CaretakerCommand with the target index and caretaker fields.
     */
    public CaretakerCommand(Index targetIndex, Name name, Phone phone,
                            Relationship relationship, Optional<Address> addressOpt) {
        this.targetIndex = requireNonNull(targetIndex);
        this.name = requireNonNull(name);
        this.phone = requireNonNull(phone);
        this.relationship = requireNonNull(relationship);
        this.addressOpt = requireNonNull(addressOpt);
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        ensureValidPatientIndex(targetIndex, model);

        Person personToAddCaretaker = lastShownList.get(targetIndex.getZeroBased());
        if (!(personToAddCaretaker instanceof Patient)) {
            throw new CommandException(Messages.MESSAGE_REQUIRE_PATIENT);
        }
        Patient patient = (Patient) personToAddCaretaker;

        if (patient.getCaretaker() != null) {
            String msg = String.format(MESSAGE_PATIENT_HAS_CARETAKER, targetIndex.getOneBased());
            throw new CommandException(msg);
        }

        // Resolve final non-null address (use patient's address if absent in input)
        Address finalAddress = addressOpt.orElse(patient.getAddress());

        // Only now construct a fully valid Caretaker (no nulls)
        Caretaker caretaker = new Caretaker(name, phone, finalAddress, relationship);

        // Prevent duplicate person (caretaker already exists as a patient)
        if (model.hasPerson(caretaker)) {
            throw new CommandException(MESSAGE_CARETAKER_ALREADY_EXISTS);
        }

        try {
            Patient updatedPatient = patient.addCaretaker(caretaker);
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
        if (!(other instanceof CaretakerCommand)) {
            return false;
        }
        CaretakerCommand o = (CaretakerCommand) other;
        return targetIndex.equals(o.targetIndex)
                && name.equals(o.name)
                && phone.equals(o.phone)
                && relationship.equals(o.relationship)
                && addressOpt.equals(o.addressOpt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(targetIndex, name, phone, relationship, addressOpt);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("name", name)
                .add("phone", phone)
                .add("relationship", relationship)
                .add("addressOpt", addressOpt)
                .toString();
    }
}
