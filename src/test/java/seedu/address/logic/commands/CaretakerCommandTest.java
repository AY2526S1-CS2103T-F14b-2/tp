package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX;
import static seedu.address.logic.Messages.shortFormat;
import static seedu.address.logic.commands.CaretakerCommand.MESSAGE_CARETAKER_ALREADY_EXISTS;
import static seedu.address.logic.commands.CaretakerCommand.MESSAGE_PATIENT_HAS_CARETAKER;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalIndexes.INDEX_SECOND_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.*;
import seedu.address.testutil.CaretakerBuilder;
import seedu.address.testutil.PatientBuilder;

/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code CaretakerCommand}.
 */
public class CaretakerCommandTest {

    private Model model;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_addCaretaker_success() throws Exception {
        Patient patient = new PatientBuilder().withCaretaker(null).build();
        Caretaker caretaker = new CaretakerBuilder().build();

        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), patient);

        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker);

        Patient updatedPatient = patient.addCaretaker(caretaker);
        String expectedMessage = String.format(CaretakerCommand.MESSAGE_SUCCESS, Messages.format(caretaker),
                Messages.shortFormat(updatedPatient));

        Model expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
        expectedModel.setPerson(patient, updatedPatient);

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(expectedModel.getFilteredPersonList(), model.getFilteredPersonList());
    }

    @Test
    public void execute_patientHasCaretaker_throwsCommandException() throws Exception {
        Patient patient = new PatientBuilder().build();
        Caretaker caretaker = new CaretakerBuilder().build();

        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), patient);

        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker);

        assertThrows(CommandException.class,
                String.format(MESSAGE_PATIENT_HAS_CARETAKER,
                        shortFormat(patient)), () -> command.execute(model));
    }

    @Test
    public void execute_caretakerExistsAsPatient_throwsCommandException() throws Exception {
        Patient patient1 = new PatientBuilder().build();
        Patient patient2 = new PatientBuilder().withName("Bob Ross").withPhone("81234567").withCaretaker(null).build();
        Caretaker caretaker = new CaretakerBuilder().withName("Betty Bee").withPhone("85355255").build();

        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), patient1);
        model.setPerson(model.getFilteredPersonList().get(INDEX_SECOND_PERSON.getZeroBased()), patient2);

        CaretakerCommand command = new CaretakerCommand(INDEX_SECOND_PERSON, caretaker);

        assertThrows(CommandException.class,
                String.format(MESSAGE_CARETAKER_ALREADY_EXISTS), () -> command.execute(model));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Caretaker caretaker = new CaretakerBuilder().build();
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CaretakerCommand command = new CaretakerCommand(outOfBoundIndex, caretaker);

        assertThrows(CommandException.class,
            String.format(MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX, model.getSize()), ()
                -> command.execute(model));
    }

    @Test
    public void execute_nonPatientTarget_throwsCommandException() {
        // replace first person with a non-patient
        Caretaker nonPatient = new CaretakerBuilder().build();
        model.setPerson(model.getFilteredPersonList().get(0), nonPatient);

        Caretaker caretaker = new CaretakerBuilder().build();
        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker);

        assertThrows(CommandException.class,
                Messages.MESSAGE_REQUIRE_PATIENT, () -> command.execute(model));
    }

    @Test
    public void equals() {
        Caretaker caretakerA = new CaretakerBuilder().withName("Alice").build();
        Caretaker caretakerB = new CaretakerBuilder().withName("Bob").build();

        CaretakerCommand addCaretakerA = new CaretakerCommand(INDEX_FIRST_PERSON, caretakerA);
        CaretakerCommand addCaretakerB = new CaretakerCommand(INDEX_SECOND_PERSON, caretakerB);

        // same object -> true
        assertTrue(addCaretakerA.equals(addCaretakerA));

        // same values -> true
        CaretakerCommand addCaretakerACopy = new CaretakerCommand(INDEX_FIRST_PERSON, caretakerA);
        assertTrue(addCaretakerA.equals(addCaretakerACopy));

        // different types -> false
        assertFalse(addCaretakerA.equals(1));

        // null -> false
        assertFalse(addCaretakerA.equals(null));

        // different index -> false
        assertFalse(addCaretakerA.equals(addCaretakerB));

        // different caretaker -> false
        CaretakerCommand differentCaretaker = new CaretakerCommand(INDEX_FIRST_PERSON, caretakerB);
        assertFalse(addCaretakerA.equals(differentCaretaker));
    }


    @Test
    public void execute_addCaretakerWithoutAddress_successUsesPatientAddress() throws Exception {
        // Patient has an address, caretaker has none
        Patient patient = new PatientBuilder().withCaretaker(null)
                .withAddress("123 Clementi Road").build();
        Caretaker caretaker = new Caretaker(new Name("TestName"), new Phone("12345678"),
                null, new Relationship("TestRelationship"));

        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), patient);
        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker);

        Patient updatedPatient = patient.addCaretaker(
                new Caretaker(caretaker.getName(), caretaker.getPhone(),
                        patient.getAddress(), caretaker.getRelationship()));

        String expectedMessage = String.format(CaretakerCommand.MESSAGE_SUCCESS,
                Messages.format(updatedPatient.getCaretaker()),
                Messages.shortFormat(updatedPatient));

        CommandResult result = command.execute(model);

        assertEquals(expectedMessage, result.getFeedbackToUser());
        assertEquals(updatedPatient.getCaretaker().getAddress(), patient.getAddress());
    }
}

