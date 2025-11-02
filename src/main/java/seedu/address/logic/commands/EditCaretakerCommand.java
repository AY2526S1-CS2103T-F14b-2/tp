package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_RELATIONSHIP;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.CollectionUtil;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * Edits the caretaker of an existing patient in the address book.
 */
public class EditCaretakerCommand extends AbstractEditCommand<Patient, EditCaretakerCommand.EditCaretakerDescriptor> {

    public static final String COMMAND_WORD = "editcaretaker";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the patient identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_RELATIONSHIP + "RELATIONSHIP]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 ";

    public static final String MESSAGE_EDIT_CARETAKER_SUCCESS = "Caretaker edited: %1$s\n"
            + "%2$s";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
            + "Edit can only be done on Patients.";
    public static final String MESSAGE_NO_CARETAKER = "The patient at index %1$s does not have a caretaker. "
            + "Edit can only be done on patients with a caretaker.";
    public static final String MESSAGE_CARETAKER_ALREADY_EXISTS = "This caretaker already exists as a "
            + "patient in the address book.";

    /**
     * @param index of the patient in the filtered person list to edit
     * @param editCaretakerDescriptor details to edit the caretaker with
     */
    public EditCaretakerCommand(Index index, EditCaretakerDescriptor editCaretakerDescriptor) {
        super(index, editCaretakerDescriptor);
    }

    @Override
    protected List<Patient> getTargetList(Model model) {
        // Cast the person list to patients for the abstract class
        // We'll validate the actual type in validateEdit
        @SuppressWarnings("unchecked")
        List<Patient> patientList = (List<Patient>) (List<?>) model.getFilteredPersonList();
        return patientList;
    }

    @Override
    protected void validateEdit(Model model, Patient patientToEdit, EditCaretakerDescriptor editDescriptor)
            throws CommandException {
        Object originalPerson = model.getFilteredPersonList().get(index.getZeroBased());
        if (!(originalPerson instanceof Patient patient)) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, index.getOneBased()));
        }
        if (patient.getCaretaker() == null) {
            throw new CommandException(String.format(MESSAGE_NO_CARETAKER, index.getOneBased()));
        }
    }

    @Override
    protected boolean isAnyFieldEdited(EditCaretakerDescriptor editDescriptor) {
        return editDescriptor.isAnyFieldEdited();
    }

    @Override
    protected Patient createEditedItem(Patient patientToEdit, EditCaretakerDescriptor d) {
        assert patientToEdit != null;

        Caretaker old = patientToEdit.getCaretaker();

        Name updatedName = d.getName().orElse(old.getName());
        Phone updatedPhone = d.getPhone().orElse(old.getPhone());

        Address updatedAddress;
        if (d.isCopyAddressFromPatient()) {
            updatedAddress = patientToEdit.getAddress();
        } else {
            updatedAddress = d.getAddress().orElse(old.getAddress());
        }

        Relationship updatedRelationship = d.getRelationship().orElse(old.getRelationship());

        Caretaker newCaretaker = new Caretaker(updatedName, updatedPhone, updatedAddress, updatedRelationship);

        return new Patient(
                patientToEdit.getName(),
                patientToEdit.getPhone(),
                patientToEdit.getAddress(),
                patientToEdit.getTag().orElse(null),
                patientToEdit.getNotes(),
                patientToEdit.getAppointment(),
                newCaretaker
        );
    }

    @Override
    protected void validateUniqueItem(Model model, Patient originalPatient, Patient editedPatient)
            throws CommandException {
        if (!originalPatient.isSamePerson(editedPatient) || model.hasPerson(editedPatient.getCaretaker())) {
            throw new CommandException(MESSAGE_CARETAKER_ALREADY_EXISTS);
        }
    }

    @Override
    protected void updateModel(Model model, Patient originalPatient, Patient editedPatient) {
        model.setPerson(originalPatient, editedPatient);
    }

    @Override
    protected String formatSuccessMessage(Patient editedPatient) {
        return String.format(MESSAGE_EDIT_CARETAKER_SUCCESS, Messages.format(editedPatient.getCaretaker()),
                Messages.shortFormat(editedPatient));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", index)
                .add("editPersonDescriptor", editDescriptor)
                .toString();
    }

    /**
     * Stores the details to edit the person with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditPersonDescriptor {
        private Name name;
        private Phone phone;
        private Address address;

        public EditPersonDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPersonDescriptor(EditPersonDescriptor toCopy) {
            setName(toCopy.name);
            setPhone(toCopy.phone);
            setAddress(toCopy.address);
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(name, phone, address);
        }

        public void setName(Name name) {
            this.name = name;
        }

        public Optional<Name> getName() {
            return Optional.ofNullable(name);
        }

        public void setPhone(Phone phone) {
            this.phone = phone;
        }

        public Optional<Phone> getPhone() {
            return Optional.ofNullable(phone);
        }

        public void setAddress(Address address) {
            this.address = address;
        }

        public Optional<Address> getAddress() {
            return Optional.ofNullable(address);
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPersonDescriptor)) {
                return false;
            }

            EditPersonDescriptor otherEditPersonDescriptor = (EditPersonDescriptor) other;
            return Objects.equals(name, otherEditPersonDescriptor.name)
                    && Objects.equals(phone, otherEditPersonDescriptor.phone)
                    && Objects.equals(address, otherEditPersonDescriptor.address);
        }

        @Override
        public String toString() {
            ToStringBuilder sb = new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("address", address);

            return sb.toString();
        }

        public ToStringBuilder getStringBuilder() {
            ToStringBuilder sb = new ToStringBuilder(this)
                    .add("name", name)
                    .add("phone", phone)
                    .add("address", address);

            return sb;
        }
    }

    /**
     * Stores the details to edit the patient with. Each non-empty field value will replace the
     * corresponding field value of the person.
     */
    public static class EditCaretakerDescriptor extends EditPersonDescriptor {
        private Relationship relationship;
        private boolean relationshipEdited;
        private boolean copyAddressFromPatient = false;

        public EditCaretakerDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditCaretakerDescriptor(EditCaretakerDescriptor toCopy) {
            super(toCopy);
            this.relationship = toCopy.relationship;
            this.relationshipEdited = toCopy.relationshipEdited;
            this.copyAddressFromPatient = toCopy.copyAddressFromPatient;
        }

        public EditCaretakerDescriptor(EditPersonDescriptor toCopyPerson) {
            super(toCopyPerson);
        }

        public void setRelationship(Relationship relationship) {
            this.relationship = relationship;
            setRelationshipEdited();
        }

        public void setRelationshipEdited() {
            this.relationshipEdited = true;
        }

        public Optional<Relationship> getRelationship() {
            return Optional.ofNullable(relationship);
        }

        public boolean isRelationshipEdited() {
            return this.relationshipEdited;
        }

        public void markCopyAddressFromPatient() {
            this.copyAddressFromPatient = true;
        }

        public boolean isCopyAddressFromPatient() {
            return copyAddressFromPatient;
        }

        /**
         * Returns true if at least one field is edited.
         */
        @Override
        public boolean isAnyFieldEdited() {
            return super.isAnyFieldEdited() || this.isRelationshipEdited()
                    || this.isCopyAddressFromPatient();
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditCaretakerDescriptor otherEditCaretakerDescriptor)) {
                return false;
            }

            return super.equals(otherEditCaretakerDescriptor)
                    && Objects.equals(relationship, otherEditCaretakerDescriptor.relationship);
        }

        @Override
        public String toString() {

            ToStringBuilder sb = super.getStringBuilder();
            sb.add("relationship", relationship);

            return sb.toString();
        }
    }
}
