package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.address.logic.parser.CliSyntax.PREFIX_TAG;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

import java.util.ArrayList;
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
import seedu.address.model.person.Name;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Phone;
import seedu.address.model.tag.Tag;

/**
 * Edits the details of an existing patient in the address book.
 */
public class EditPatientCommand extends AbstractEditCommand<Patient, EditPatientCommand.EditPersonDescriptor> {

    public static final String COMMAND_WORD = "editpatient";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the person identified "
            + "by the index number used in the displayed person list. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_NAME + "NAME] "
            + "[" + PREFIX_PHONE + "PHONE] "
            + "[" + PREFIX_ADDRESS + "ADDRESS] "
            + "[" + PREFIX_TAG + "TAG]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_PHONE + "91234567 ";

    public static final String MESSAGE_EDIT_PERSON_SUCCESS = "Patient Edited: %s%n%s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
            + "edit can only be done on Patients.";

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPatientDescriptor details to edit the person with
     */
    public EditPatientCommand(Index index, EditPatientDescriptor editPatientDescriptor) {
        super(index, editPatientDescriptor);
    }

    /**
     * @param index of the person in the filtered person list to edit
     * @param editPersonDescriptor details to edit the person with
     */
    public EditPatientCommand(Index index, EditPersonDescriptor editPersonDescriptor) {
        super(index, editPersonDescriptor);
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
    protected void validateEdit(Model model, Patient patientToEdit, EditPersonDescriptor editDescriptor)
            throws CommandException {
        // Check if the item at the index is actually a patient
        // Since we're casting above, we need to check the original person
        Object originalPerson = model.getFilteredPersonList().get(index.getZeroBased());
        if (!(originalPerson instanceof Patient)) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, index.getOneBased()));
        }
    }

    @Override
    protected boolean isAnyFieldEdited(EditPersonDescriptor editDescriptor) {
        return editDescriptor.isAnyFieldEdited();
    }

    @Override
    protected Patient createEditedItem(Patient patientToEdit, EditPersonDescriptor editPersonDescriptor) {
        assert patientToEdit != null;

        Name updatedName = editPersonDescriptor.getName().orElse(patientToEdit.getName());
        Phone updatedPhone = editPersonDescriptor.getPhone().orElse(patientToEdit.getPhone());
        Address updatedAddress = editPersonDescriptor.getAddress().orElse(patientToEdit.getAddress());

        // Handle tag field - only available if this is an EditPatientDescriptor
        Tag updatedTag;
        if (editPersonDescriptor instanceof EditPatientDescriptor) {
            EditPatientDescriptor editPatientDescriptor = (EditPatientDescriptor) editPersonDescriptor;
            updatedTag = editPatientDescriptor.isTagEdited()
                    ? editPatientDescriptor.getTag().orElse(null)
                    : patientToEdit.getTag().orElse(null);
        } else {
            // Keep existing tag if using base EditPersonDescriptor
            updatedTag = patientToEdit.getTag().orElse(null);
        }

        return new Patient(updatedName, updatedPhone, updatedAddress, updatedTag, patientToEdit.getNotes(),
                patientToEdit.getAppointment(), patientToEdit.getCaretaker());
    }

    @Override
    protected void validateUniqueItem(Model model, Patient originalPatient, Patient editedPatient)
            throws CommandException {
        if (!originalPatient.isSamePerson(editedPatient) && model.hasPerson(editedPatient)) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        }
    }

    @Override
    protected void updateModel(Model model, Patient originalPatient, Patient editedPatient) {
        model.setPerson(originalPatient, editedPatient);
    }

    @Override
    protected void updateModelAfterEdit(Model model) {
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
    }

    @Override
    protected String formatSuccessMessage(Patient editedPatient) {
        String details = formatEditedFields(editDescriptor);
        return String.format(MESSAGE_EDIT_PERSON_SUCCESS, details, Messages.shortFormat(editedPatient));
    }

    /**
     * Builds a human-readable summary of the fields supplied in an {@link EditPersonDescriptor}.
     * <p>
     * The output includes only values that are present in the descriptor (i.e., fields the user
     * provided during edit). It does <b>not</b> compare against the original patient to check if
     * the value actually changed.
     * </p>
     * @param d the edit descriptor containing any user-supplied fields
     * @return a formatted string describing only the supplied fields, or {@code "no fields edited"}
     *         if none were provided
     */
    public static String formatEditedFields(EditPersonDescriptor d) {
        List<String> parts = new ArrayList<>();

        d.getName().ifPresent(n -> parts.add(" New Name: " + n));
        d.getPhone().ifPresent(p -> parts.add(" New Phone: " + p));
        d.getAddress().ifPresent(a -> parts.add(" New Address: " + a));

        if (d instanceof EditPatientDescriptor epd && epd.isTagEdited()) {
            parts.add(" New Tag: " + epd.getTag().map(t -> t.toString()).orElse("(removed)"));
        }

        return parts.isEmpty() ? "no fields edited" : String.join(", ", parts);
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
    public static class EditPatientDescriptor extends EditPersonDescriptor {
        private Tag tag;
        private boolean tagEdited;

        public EditPatientDescriptor() {}

        /**
         * Copy constructor.
         * A defensive copy of {@code tags} is used internally.
         */
        public EditPatientDescriptor(EditPatientDescriptor toCopy) {
            super(toCopy);
            this.tag = toCopy.tag;
            this.tagEdited = toCopy.tagEdited;
        }

        public EditPatientDescriptor(EditPersonDescriptor toCopyPerson) {
            super(toCopyPerson);
        }

        public void setTag(Tag tag) {
            this.tag = tag;
        }

        public void setTagEdited() {
            this.tagEdited = true;
        }

        public Optional<Tag> getTag() {
            return Optional.ofNullable(tag);
        }

        public boolean isTagEdited() {
            return this.tagEdited;
        }

        /**
         * Returns true if at least one field is edited.
         */
        @Override
        public boolean isAnyFieldEdited() {
            return super.isAnyFieldEdited() || tagEdited;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditPatientDescriptor otherEditPatientDescriptor)) {
                return false;
            }

            return super.equals(otherEditPatientDescriptor)
                    && Objects.equals(tag, otherEditPatientDescriptor.tag);
        }

        @Override
        public String toString() {

            ToStringBuilder sb = super.getStringBuilder();
            sb.add("tag", tag);

            return sb.toString();
        }
    }
}
