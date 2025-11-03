package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.DESC_AMY;
import static seedu.address.logic.commands.CommandTestUtil.DESC_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.EditPatientCommand.MESSAGE_EDIT_PERSON_SUCCESS;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import java.util.Optional;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditPatientCommand.EditPatientDescriptor;
import seedu.address.logic.commands.EditPatientCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.testutil.EditPatientDescriptorBuilder;
import seedu.address.testutil.EditPersonDescriptorBuilder;
import seedu.address.testutil.PatientBuilder;



/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditPatientCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Index indexFirstPerson = INDEX_FIRST_PERSON;

        Person firstPerson = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());

        Patient firstPatient = (Patient) firstPerson;
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .build();

        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_FIRST_PERSON, descriptor);

        Patient editedPatient = new PatientBuilder(firstPatient)
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .build();

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.formatForEdit(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(editPatientCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Index indexLastPerson = Index.fromOneBased(model.getFilteredPersonList().size());
        Person lastPerson = model.getFilteredPersonList().get(indexLastPerson.getZeroBased());

        Patient lastPatient = (Patient) lastPerson;
        PatientBuilder patientInList = new PatientBuilder(lastPatient);
        Patient editedPatient = patientInList.withName(VALID_NAME_AMY).withPhone(VALID_PHONE_AMY)
                                            .build();

        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).build();
        EditPatientCommand editPatientCommand = new EditPatientCommand(indexLastPerson, descriptor);

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.formatForEdit(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(lastPerson, editedPatient);

        assertCommandSuccess(editPatientCommand, model, expectedMessage, expectedModel);
    }

    //@Test
    //    public void execute_noFieldSpecifiedUnfilteredList_success() {
    //        EditCommand editCommand = new EditCommand(INDEX_FIRST_PERSON, new EditPersonDescriptor());
    //        Person editedPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
    //
    //        String expectedMessage = String.format(EditCommand.MESSAGE_EDIT_PERSON_SUCCESS,
    //        Messages.format(editedPerson));
    //
    //        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
    //
    //        assertCommandSuccess(editCommand, model, expectedMessage, expectedModel);
    //    }

    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person personInFilteredList = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Patient patientInFilteredList = (Patient) personInFilteredList;
        Patient editedPatient = new PatientBuilder(patientInFilteredList).withName(VALID_NAME_BOB).build();
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_FIRST_PERSON,
                descriptor);

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.formatForEdit(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(model.getFilteredPersonList().get(0), editedPatient);

        assertCommandSuccess(editPatientCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        EditPatientDescriptor descriptor = new EditPatientDescriptorBuilder(firstPerson).build();
        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editPatientCommand, model, EditPatientCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_duplicatePatientFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // edit patient in filtered list into a duplicate in address book
        Patient patientInList = (Patient) model.getAddressBook().getPersonList()
                .get(INDEX_SECOND_PERSON.getZeroBased());
        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_FIRST_PERSON,
                new EditPatientDescriptorBuilder(patientInList).build());

        assertCommandFailure(editPatientCommand, model, EditPatientCommand.MESSAGE_DUPLICATE_PERSON);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditPersonDescriptor descriptor = new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditPatientCommand editPatientCommand = new EditPatientCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editPatientCommand, model,
                Messages.invalidPatientIndex(model.getSize()));
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);
        Index outOfBoundIndex = INDEX_SECOND_PERSON;
        // ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        EditPatientCommand editPatientCommand = new EditPatientCommand(outOfBoundIndex,
                new EditPersonDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editPatientCommand, model,
                Messages.invalidPatientIndex(model.getSize()));
    }

    @Test
    public void equals() {
        final EditPatientCommand standardCommand = new EditPatientCommand(INDEX_FIRST_PERSON, DESC_AMY);

        // same values -> returns true
        EditPersonDescriptor copyDescriptor = new EditPersonDescriptor(DESC_AMY);
        EditPatientCommand commandWithSameValues = new EditPatientCommand(INDEX_FIRST_PERSON, copyDescriptor);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        // null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> returns false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different index -> returns false
        assertFalse(standardCommand.equals(new EditPatientCommand(INDEX_SECOND_PERSON, DESC_AMY)));

        // different descriptor -> returns false
        assertFalse(standardCommand.equals(new EditPatientCommand(INDEX_FIRST_PERSON, DESC_BOB)));
    }

    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditPersonDescriptor editPersonDescriptor = new EditPersonDescriptor();
        EditPatientCommand editPatientCommand = new EditPatientCommand(index, editPersonDescriptor);
        String expected = EditPatientCommand.class.getCanonicalName() + "{index=" + index + ", editPersonDescriptor="
                + editPersonDescriptor + "}";
        assertEquals(expected, editPatientCommand.toString());
    }

    @Test
    public void execute_noFieldSpecified_throwsCommandException() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editPatientCommand, model,
            "At least one field to edit must be provided.");
    }

    @Test
    public void execute_editPatientDescriptorWithTag_success() throws Exception {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person firstPerson = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());
        Patient firstPatient = (Patient) firstPerson;

        EditPatientDescriptor descriptor = new EditPatientDescriptor();
        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        descriptor.setTag(new seedu.address.model.tag.Tag("high"));
        descriptor.setTagEdited();

        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_FIRST_PERSON, descriptor);

        Patient editedPatient = new PatientBuilder(firstPatient)
                .withName(VALID_NAME_AMY)
                .withTag("high")
                .build();

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.formatForEdit(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(editPatientCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_editPatientDescriptorClearTag_success() throws Exception {
        Index indexFirstPerson = INDEX_FIRST_PERSON;
        Person firstPerson = model.getFilteredPersonList().get(indexFirstPerson.getZeroBased());
        Patient firstPatient = (Patient) firstPerson;

        EditPatientDescriptor descriptor = new EditPatientDescriptor();
        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        descriptor.setTagEdited(); // Clear the tag

        EditPatientCommand editPatientCommand = new EditPatientCommand(INDEX_FIRST_PERSON, descriptor);

        Patient editedPatient = new Patient(
                new seedu.address.model.person.Name(VALID_NAME_AMY),
                firstPatient.getPhone(),
                firstPatient.getAddress(),
                null,
                firstPatient.getNotes(),
                firstPatient.getAppointment(),
                firstPatient.getCaretaker());

        String expectedMessage = String.format(MESSAGE_EDIT_PERSON_SUCCESS, Messages.formatForEdit(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(editPatientCommand, model, expectedMessage, expectedModel);
    }

    // Note: Testing execute_targetPersonIsNotPatient_throwsCommandException is not feasible
    // due to AbstractEditCommand's design which casts to Patient before validation occurs.
    // The validateEdit method in EditPatientCommand handles this validation but it's called
    // after AbstractEditCommand's execute method already attempts the cast, causing ClassCastException.
    // This validation is properly tested in commands like NoteCommand that extend Command directly.



    // Test EditPersonDescriptor methods
    @Test
    public void editPersonDescriptor_copyConstructor_success() {
        EditPersonDescriptor original = new EditPersonDescriptor();
        original.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        original.setPhone(new seedu.address.model.person.Phone(VALID_PHONE_AMY));
        original.setAddress(new seedu.address.model.person.Address(VALID_ADDRESS_AMY));

        EditPersonDescriptor copy = new EditPersonDescriptor(original);

        assertTrue(original.equals(copy));
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getPhone(), copy.getPhone());
        assertEquals(original.getAddress(), copy.getAddress());
    }

    @Test
    public void editPersonDescriptor_isAnyFieldEdited_returnsCorrectValue() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editPersonDescriptor_getStringBuilder_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));

        assertTrue(descriptor.getStringBuilder().toString().contains("name"));
    }

    @Test
    public void editPersonDescriptor_toString_success() {
        EditPersonDescriptor descriptor = new EditPersonDescriptor();
        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));

        assertTrue(descriptor.toString().contains("name"));
    }

    @Test
    public void editPersonDescriptor_equals_success() {
        EditPersonDescriptor descriptor1 = new EditPersonDescriptor();
        EditPersonDescriptor descriptor2 = new EditPersonDescriptor();

        // Same object
        assertTrue(descriptor1.equals(descriptor1));

        // Both empty
        assertTrue(descriptor1.equals(descriptor2));

        // Null
        assertFalse(descriptor1.equals(null));

        // Different class
        assertFalse(descriptor1.equals("string"));

        descriptor1.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        assertFalse(descriptor1.equals(descriptor2));

        descriptor2.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        assertTrue(descriptor1.equals(descriptor2));
    }

    // Test EditPatientDescriptor methods
    @Test
    public void editPatientDescriptor_copyConstructor_success() {
        EditPatientDescriptor original = new EditPatientDescriptor();
        original.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        original.setTag(new seedu.address.model.tag.Tag("high"));
        original.setTagEdited();

        EditPatientDescriptor copy = new EditPatientDescriptor(original);

        assertTrue(original.equals(copy));
        assertEquals(original.getTag(), copy.getTag());
        assertEquals(original.isTagEdited(), copy.isTagEdited());
    }

    @Test
    public void editPatientDescriptor_constructorFromEditPersonDescriptor_success() {
        EditPersonDescriptor personDescriptor = new EditPersonDescriptor();
        personDescriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));

        EditPatientDescriptor patientDescriptor = new EditPatientDescriptor(personDescriptor);

        assertEquals(personDescriptor.getName(), patientDescriptor.getName());
        assertEquals(personDescriptor.getPhone(), patientDescriptor.getPhone());
        assertEquals(personDescriptor.getAddress(), patientDescriptor.getAddress());
    }

    @Test
    public void editPatientDescriptor_isAnyFieldEdited_returnsCorrectValue() {
        EditPatientDescriptor descriptor = new EditPatientDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor.setTagEdited();
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editPatientDescriptor_tagMethods_success() {
        EditPatientDescriptor descriptor = new EditPatientDescriptor();

        assertFalse(descriptor.isTagEdited());
        assertEquals(Optional.empty(), descriptor.getTag());

        descriptor.setTag(new seedu.address.model.tag.Tag("high"));
        assertEquals("high", descriptor.getTag().get().tagName);

        descriptor.setTagEdited();
        assertTrue(descriptor.isTagEdited());
    }

    @Test
    public void editPatientDescriptor_equals_success() {
        EditPatientDescriptor descriptor1 = new EditPatientDescriptor();
        EditPatientDescriptor descriptor2 = new EditPatientDescriptor();

        // Same object
        assertTrue(descriptor1.equals(descriptor1));

        // Both empty
        assertTrue(descriptor1.equals(descriptor2));

        // Null
        assertFalse(descriptor1.equals(null));

        // Different class
        assertFalse(descriptor1.equals(new EditPersonDescriptor()));

        descriptor1.setTag(new seedu.address.model.tag.Tag("high"));
        assertFalse(descriptor1.equals(descriptor2));

        descriptor2.setTag(new seedu.address.model.tag.Tag("high"));
        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void editPatientDescriptor_toString_success() {
        EditPatientDescriptor descriptor = new EditPatientDescriptor();
        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        descriptor.setTag(new seedu.address.model.tag.Tag("high"));

        String result = descriptor.toString();
        assertTrue(result.contains("name"));
        assertTrue(result.contains("tag"));
    }
}
