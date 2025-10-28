package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.EditAppointmentCommand.EditAppointmentDescriptor;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains unit tests for {@link EditAppointmentCommand}.
 */
public class EditAppointmentCommandTest {

    private static final String INITIAL_DATE_ONE = "31-12-2099";
    private static final String INITIAL_TIME_ONE = "10:00";
    private static final String INITIAL_DATE_TWO = "01-01-2100";
    private static final String INITIAL_TIME_TWO = "11:00";
    private static final String UPDATED_DATE = "02-01-2100";
    private static final String UPDATED_TIME = "09:30";

    private final Model model = new ModelManager(getTypicalAddressBook(), new UserPrefs());

    @Test
    public void execute_validPatientAndAppointmentIndex_success() {
        Patient patientWithAppointments = new PatientBuilder()
                .withName("Zack Brown")
                .withPhone("98765432")
                .withAddress("789 Example Street")
                .withAppointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE)
                .withAppointment(INITIAL_DATE_TWO, INITIAL_TIME_TWO)
                .build();
        model.addPerson(patientWithAppointments);

        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        Appointment updatedAppointment = new Appointment(UPDATED_DATE, UPDATED_TIME, new Note("Follow up"));

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(2);
        descriptor.setDate(UPDATED_DATE);
        descriptor.setTime(UPDATED_TIME);
        descriptor.setNote(new Note("Follow up"));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(patientIndex, descriptor);

        Patient editedPatient = patientWithAppointments.editAppointment(1, updatedAppointment);
        String expectedMessage = String.format("Appointment %d edited: %s; %s; Note: %s\nFor %s; Phone: %s",
                2, UPDATED_DATE, UPDATED_TIME, "Follow up", editedPatient.getName(), editedPatient.getPhone());

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithAppointments, editedPatient);

        assertCommandSuccess(editAppointmentCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyDateEdited_success() {
        Patient patientWithAppointment = new PatientBuilder()
            .withName("Yara Grey")
            .withPhone("81119999")
            .withAddress("100 Sunset Boulevard")
            .withAppointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE)
            .build();
        model.addPerson(patientWithAppointment);

        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setDate(UPDATED_DATE);

        EditAppointmentCommand command = new EditAppointmentCommand(patientIndex, descriptor);

        Appointment originalAppointment = patientWithAppointment.getAppointment().get(0);
        Note originalNote = originalAppointment.getNote().orElse(null);
        Appointment expectedAppointment = originalNote == null
            ? new Appointment(UPDATED_DATE, originalAppointment.getTime())
            : new Appointment(UPDATED_DATE, originalAppointment.getTime(), originalNote);
        Patient editedPatient = patientWithAppointment.editAppointment(0, expectedAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithAppointment, editedPatient);

        String expectedMessage = String.format("Appointment %d edited: %s; %s\nFor %s; Phone: %s",
                1, UPDATED_DATE, originalAppointment.getTime(), editedPatient.getName(), editedPatient.getPhone());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyTimeEdited_success() {
        Patient patientWithAppointment = new PatientBuilder()
            .withName("Quinn Hale")
            .withPhone("82228888")
            .withAddress("55 Sunrise Way")
            .withAppointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE)
            .build();
        model.addPerson(patientWithAppointment);

        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setTime(UPDATED_TIME);

        EditAppointmentCommand command = new EditAppointmentCommand(patientIndex, descriptor);

        Appointment originalAppointment = patientWithAppointment.getAppointment().get(0);
        Note originalNote = originalAppointment.getNote().orElse(null);
        Appointment expectedAppointment = originalNote == null
            ? new Appointment(originalAppointment.getDate(), UPDATED_TIME)
            : new Appointment(originalAppointment.getDate(), UPDATED_TIME, originalNote);
        Patient editedPatient = patientWithAppointment.editAppointment(0, expectedAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithAppointment, editedPatient);

        String expectedMessage = String.format("Appointment %d edited: %s; %s\nFor %s; Phone: %s",
                1, INITIAL_DATE_ONE, UPDATED_TIME, editedPatient.getName(), editedPatient.getPhone());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_onlyNoteEdited_success() {
        Patient basePatient = new PatientBuilder()
            .withName("Nora Mills")
            .withPhone("83337777")
            .withAddress("77 Crescent Road")
            .build();
        Patient patientWithAppointment = basePatient.addAppointment(
            new Appointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE, new Note("Initial note")));
        model.addPerson(patientWithAppointment);

        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setNote(new Note("Updated note"));

        EditAppointmentCommand command = new EditAppointmentCommand(patientIndex, descriptor);

        Appointment expectedAppointment = new Appointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE, new Note("Updated note"));
        Patient editedPatient = patientWithAppointment.editAppointment(0, expectedAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithAppointment, editedPatient);

        String expectedMessage = String.format("Appointment %d edited: %s; %s; Note: %s\nFor %s; Phone: %s",
            1, INITIAL_DATE_ONE, INITIAL_TIME_ONE, "Updated note",
            editedPatient.getName(), editedPatient.getPhone());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_clearNote_success() {
        Patient basePatient = new PatientBuilder()
            .withName("Oscar Lane")
            .withPhone("84446666")
            .withAddress("88 Maple Street")
            .build();
        Patient patientWithAppointment = basePatient.addAppointment(
            new Appointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE, new Note("Has note")));
        model.addPerson(patientWithAppointment);

        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.clearNote();

        EditAppointmentCommand command = new EditAppointmentCommand(patientIndex, descriptor);

        Appointment expectedAppointment = new Appointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE);
        Patient editedPatient = patientWithAppointment.editAppointment(0, expectedAppointment);

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()), new UserPrefs());
        expectedModel.setPerson(patientWithAppointment, editedPatient);

        String expectedMessage = String.format("Appointment %d edited: %s; %s\nFor %s; Phone: %s",
                1, INITIAL_DATE_ONE, INITIAL_TIME_ONE, editedPatient.getName(), editedPatient.getPhone());

        assertCommandSuccess(command, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_notPatient_throwsCommandException() {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        Appointment updatedAppointment = new Appointment(UPDATED_DATE, UPDATED_TIME, new Note("Updated"));

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setDate(UPDATED_DATE);
        descriptor.setTime(UPDATED_TIME);
        descriptor.setNote(new Note("Updated"));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(outOfBoundIndex, descriptor);

        assertCommandFailure(editAppointmentCommand, model,
                String.format(Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, model.getSize()));
    }

    @Test
    public void execute_notAPatient_throwsCommandException() {
        Model modelWithPerson = new ModelManager(new AddressBook(), new UserPrefs());
        Person caretaker = new Caretaker(new Name("John Doe"), new Phone("12345678"),
                new Address("123 Street"), new Relationship("Parent"));
        modelWithPerson.addPerson(caretaker);

        Appointment updatedAppointment = new Appointment(UPDATED_DATE, UPDATED_TIME, new Note("Updated"));
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setDate(UPDATED_DATE);
        descriptor.setTime(UPDATED_TIME);
        descriptor.setNote(new Note("Updated"));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(INDEX_FIRST_PERSON, descriptor);

        assertCommandFailure(editAppointmentCommand, modelWithPerson,
                String.format(EditAppointmentCommand.MESSAGE_NOT_PATIENT, INDEX_FIRST_PERSON.getOneBased()));
    }

    @Test
    public void execute_patientWithNoAppointments_throwsCommandException() {
        Patient patientWithoutAppointments = new PatientBuilder()
                .withName("Jane Doe")
                .withPhone("87654321")
                .withAddress("456 Oak Street")
                .build();
        model.addPerson(patientWithoutAppointments);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        Appointment updatedAppointment = new Appointment(UPDATED_DATE, UPDATED_TIME, new Note("Updated"));
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);
        descriptor.setDate(UPDATED_DATE);
        descriptor.setTime(UPDATED_TIME);
        descriptor.setNote(new Note("Updated"));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(patientIndex, descriptor);

        assertCommandFailure(editAppointmentCommand, model, EditAppointmentCommand.MESSAGE_NO_APPOINTMENT);
    }

    @Test
    public void execute_invalidAppointmentIndex_throwsCommandException() {
        Patient patientWithOneAppointment = new PatientBuilder()
                .withName("Bob Smith")
                .withPhone("76543210")
                .withAddress("789 Pine Street")
                .withAppointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE)
                .build();
        model.addPerson(patientWithOneAppointment);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        Appointment updatedAppointment = new Appointment(UPDATED_DATE, UPDATED_TIME, new Note("Updated"));
        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(2);
        descriptor.setDate(UPDATED_DATE);
        descriptor.setTime(UPDATED_TIME);
        descriptor.setNote(new Note("Updated"));

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(patientIndex, descriptor);
        assertCommandFailure(editAppointmentCommand, model,
                String.format(EditAppointmentCommand.MESSAGE_INVALID_ITEM_INDEX, 2, 1));
    }

    @Test
    public void execute_noFieldsEdited_throwsCommandException() {
        Patient patientWithAppointment = new PatientBuilder()
                .withName("Carl Jones")
                .withPhone("76543219")
                .withAddress("321 Elm Street")
                .withAppointment(INITIAL_DATE_ONE, INITIAL_TIME_ONE)
                .build();
        model.addPerson(patientWithAppointment);
        Index patientIndex = Index.fromOneBased(model.getFilteredPersonList().size());

        EditAppointmentDescriptor descriptor = new EditAppointmentDescriptor();
        descriptor.setAppointmentIndex(1);

        EditAppointmentCommand editAppointmentCommand = new EditAppointmentCommand(patientIndex, descriptor);

        assertCommandFailure(editAppointmentCommand, model, AbstractEditCommand.MESSAGE_NOT_EDITED);
    }

    @Test
    public void equals() {
        EditAppointmentDescriptor descriptorOne = new EditAppointmentDescriptor();
        descriptorOne.setAppointmentIndex(1);
        descriptorOne.setDate(UPDATED_DATE);
        descriptorOne.setTime(UPDATED_TIME);
        descriptorOne.setNote(new Note("Follow up"));

        EditAppointmentDescriptor descriptorTwo = new EditAppointmentDescriptor(descriptorOne);
        EditAppointmentDescriptor descriptorThree = new EditAppointmentDescriptor();
        descriptorThree.setAppointmentIndex(2);
        descriptorThree.setDate("03-01-2100");
        descriptorThree.setTime("08:30");
        descriptorThree.setNote(new Note("Review"));

        EditAppointmentDescriptor descriptorFour = new EditAppointmentDescriptor();
        descriptorFour.setAppointmentIndex(1);
        descriptorFour.clearNote();

        EditAppointmentCommand commandOne = new EditAppointmentCommand(INDEX_FIRST_PERSON, descriptorOne);
        EditAppointmentCommand commandTwo = new EditAppointmentCommand(INDEX_FIRST_PERSON, descriptorTwo);
        EditAppointmentCommand commandThree = new EditAppointmentCommand(INDEX_SECOND_PERSON, descriptorOne);
        EditAppointmentCommand commandFour = new EditAppointmentCommand(INDEX_FIRST_PERSON, descriptorThree);
        EditAppointmentCommand commandFive = new EditAppointmentCommand(INDEX_FIRST_PERSON, descriptorFour);

        // same values -> returns true
        assertTrue(commandOne.equals(commandTwo));

        // same object -> returns true
        assertTrue(commandOne.equals(commandOne));

        // null -> returns false
        assertFalse(commandOne.equals(null));

        // different type -> returns false
        assertFalse(commandOne.equals(5));

        // different person index -> returns false
        assertFalse(commandOne.equals(commandThree));

        // different descriptor -> returns false
        assertFalse(commandOne.equals(commandFour));

        // descriptor clears note -> returns false compared to descriptor setting values
        assertFalse(commandOne.equals(commandFive));
    }
}
