package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_ADDRESS_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_NAME_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_PHONE_AMY;
import static seedu.address.logic.commands.CommandTestUtil.VALID_RELATIONSHIP_AMY;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditCaretakerCommand.EditCaretakerDescriptor;
import seedu.address.logic.commands.EditCaretakerCommand.EditPersonDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.testutil.CaretakerBuilder;
import seedu.address.testutil.EditCaretakerDescriptorBuilder;
import seedu.address.testutil.PatientBuilder;



/**
 * Contains integration tests (interaction with the Model) and unit tests for EditCommand.
 */
public class EditCaretakerCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_allFieldsSpecifiedUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Patient firstPatient = (Patient) firstPerson;
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withRelationship(VALID_RELATIONSHIP_AMY)
                .build();

        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(INDEX_FIRST_PERSON, descriptor);

        Caretaker newCaretaker = new CaretakerBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withRelationship(VALID_RELATIONSHIP_AMY)
                .build();

        Patient editedPatient = new PatientBuilder(firstPatient)
                .withCaretaker(newCaretaker)
                .build();

        String expectedMessage = String.format(EditCaretakerCommand.MESSAGE_EDIT_CARETAKER_SUCCESS,
                Messages.format(newCaretaker), Messages.shortFormat(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(editCaretakerCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_someFieldsSpecifiedUnfilteredList_success() {
        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        Patient firstPatient = (Patient) firstPerson;
        Caretaker newCaretaker = new CaretakerBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withRelationship("Son")
                .withAddress("123, Jurong West Ave 6, #08-111")
                .build();
        Patient editedPatient = new PatientBuilder(firstPatient)
                .withCaretaker(newCaretaker)
                .build();

        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder().withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY).build();
        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(INDEX_FIRST_PERSON, descriptor);

        String expectedMessage = String.format(EditCaretakerCommand.MESSAGE_EDIT_CARETAKER_SUCCESS,
                Messages.format(newCaretaker), Messages.shortFormat(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(editCaretakerCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_filteredList_success() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        Person firstPerson = model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        Patient firstPatient = (Patient) firstPerson;
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withRelationship(VALID_RELATIONSHIP_AMY)
                .build();

        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(INDEX_FIRST_PERSON, descriptor);

        Caretaker newCaretaker = new CaretakerBuilder()
                .withName(VALID_NAME_AMY)
                .withPhone(VALID_PHONE_AMY)
                .withAddress(VALID_ADDRESS_AMY)
                .withRelationship(VALID_RELATIONSHIP_AMY)
                .build();

        Patient editedPatient = new PatientBuilder(firstPatient)
                .withCaretaker(newCaretaker)
                .build();

        String expectedMessage = String.format(EditCaretakerCommand.MESSAGE_EDIT_CARETAKER_SUCCESS,
                Messages.format(newCaretaker), Messages.shortFormat(editedPatient));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(firstPatient, editedPatient);

        assertCommandSuccess(editCaretakerCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_duplicatePersonUnfilteredList_failure() {
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withName("Alice Pauline")
                .withPhone("94351253")
                .build();
        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(INDEX_SECOND_PERSON, descriptor);

        assertCommandFailure(editCaretakerCommand, model, EditCaretakerCommand.MESSAGE_CARETAKER_ALREADY_EXISTS);
    }

    @Test
    public void execute_duplicatePatientFilteredList_failure() {
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withName("Alice Pauline")
                .withPhone("94351253")
                .build();
        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editCaretakerCommand, model, EditCaretakerCommand.MESSAGE_CARETAKER_ALREADY_EXISTS);
    }

    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder().withName(VALID_NAME_BOB).build();
        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editCaretakerCommand, model,
                Messages.invalidPatientIndex(model.getFilteredPersonList().size()));
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

        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(outOfBoundIndex,
                new EditCaretakerDescriptorBuilder().withName(VALID_NAME_BOB).build());

        assertCommandFailure(editCaretakerCommand, model,
                Messages.invalidPatientIndex(model.getFilteredPersonList().size()));
    }
    @Test
    public void toStringMethod() {
        Index index = Index.fromOneBased(1);
        EditCaretakerDescriptor editPersonDescriptor = new EditCaretakerDescriptor();
        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(index, editPersonDescriptor);
        String expected = EditCaretakerCommand.class.getCanonicalName() + "{index=" + index
                + ", editPersonDescriptor=" + editPersonDescriptor + "}";
        assertEquals(expected, editCaretakerCommand.toString());
    }

    @Test
    public void execute_noFieldSpecified_throwsCommandException() {
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptor();
        EditCaretakerCommand editCaretakerCommand = new EditCaretakerCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editCaretakerCommand, model,
                "At least one field to edit must be provided.");
    }

    // ---------- EditCaretakerDescriptor tests ----------

    @Test
    public void editCaretakerDescriptor_copyConstructor_success() {
        EditCaretakerDescriptor original = new EditCaretakerDescriptor();
        original.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        original.setPhone(new seedu.address.model.person.Phone(VALID_PHONE_AMY));
        original.setAddress(new seedu.address.model.person.Address(VALID_ADDRESS_AMY));
        original.setRelationship(new seedu.address.model.person.Relationship(VALID_RELATIONSHIP_AMY));

        EditCaretakerDescriptor copy = new EditCaretakerDescriptor(original);

        assertTrue(original.equals(copy));
        assertEquals(original.getName(), copy.getName());
        assertEquals(original.getPhone(), copy.getPhone());
        assertEquals(original.getAddress(), copy.getAddress());
        assertEquals(original.getRelationship(), copy.getRelationship());
    }

    @Test
    public void editCaretakerDescriptor_constructorFromEditPersonDescriptor_success() {
        EditPersonDescriptor personDescriptor = new EditPersonDescriptor();
        personDescriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));

        EditCaretakerDescriptor caretakerDescriptor = new EditCaretakerDescriptor(personDescriptor);

        assertEquals(personDescriptor.getName(), caretakerDescriptor.getName());
        assertEquals(personDescriptor.getPhone(), caretakerDescriptor.getPhone());
        assertEquals(personDescriptor.getAddress(), caretakerDescriptor.getAddress());
    }

    @Test
    public void editCaretakerDescriptor_isAnyFieldEdited_returnsCorrectValue() {
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptor();
        assertFalse(descriptor.isAnyFieldEdited());

        descriptor.setRelationship(new seedu.address.model.person.Relationship("Son"));
        assertTrue(descriptor.isAnyFieldEdited());
    }

    @Test
    public void editCaretakerDescriptor_equals_success() {
        EditCaretakerDescriptor descriptor1 = new EditCaretakerDescriptor();
        EditCaretakerDescriptor descriptor2 = new EditCaretakerDescriptor();

        // same object
        assertTrue(descriptor1.equals(descriptor1));

        // both empty
        assertTrue(descriptor1.equals(descriptor2));

        // null
        assertFalse(descriptor1.equals(null));

        // different class
        assertFalse(descriptor1.equals("string"));

        descriptor1.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        assertFalse(descriptor1.equals(descriptor2));

        descriptor2.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        assertTrue(descriptor1.equals(descriptor2));
    }

    @Test
    public void editCaretakerDescriptor_toString_success() {
        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptor();
        descriptor.setName(new seedu.address.model.person.Name(VALID_NAME_AMY));
        descriptor.setRelationship(new seedu.address.model.person.Relationship(VALID_RELATIONSHIP_AMY));

        String result = descriptor.toString();
        assertTrue(result.contains("name"));
        assertTrue(result.contains("relationship"));
    }

    @Test
    public void execute_copyAddressFromPatient_success() {
        Patient original = (Patient) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());

        EditCaretakerDescriptor descriptor = new EditCaretakerDescriptorBuilder()
                .withCopyAddressFromPatient()
                .build();

        EditCaretakerCommand cmd = new EditCaretakerCommand(INDEX_FIRST_PERSON, descriptor);
        Caretaker expectedCaretaker = new Caretaker(
                original.getCaretaker().getName(),
                original.getCaretaker().getPhone(),
                original.getAddress(),
                original.getCaretaker().getRelationship()
        );

        Patient expectedEdited = new PatientBuilder(original).withCaretaker(expectedCaretaker).build();

        String expectedMessage = String.format(EditCaretakerCommand.MESSAGE_EDIT_CARETAKER_SUCCESS,
                Messages.format(expectedCaretaker), Messages.shortFormat(expectedEdited));

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(original, expectedEdited);

        assertCommandSuccess(cmd, model, expectedMessage, expectedModel);
    }

}
