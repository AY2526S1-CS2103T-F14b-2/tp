package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.*;
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
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.testutil.CaretakerBuilder;
import seedu.address.testutil.PatientBuilder;

import java.util.Optional;

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

        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker.getName(),
                caretaker.getPhone(), caretaker.getRelationship(), Optional.of(caretaker.getAddress()));

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

        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker.getName(),
                caretaker.getPhone(), caretaker.getRelationship(), Optional.of(caretaker.getAddress()));

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

        CaretakerCommand command = new CaretakerCommand(INDEX_SECOND_PERSON, caretaker.getName(),
                caretaker.getPhone(), caretaker.getRelationship(), Optional.of(caretaker.getAddress()));

        assertThrows(CommandException.class,
                String.format(MESSAGE_CARETAKER_ALREADY_EXISTS), () -> command.execute(model));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Caretaker caretaker = new CaretakerBuilder().build();
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        CaretakerCommand command = new CaretakerCommand(outOfBoundIndex, caretaker.getName(),
                caretaker.getPhone(), caretaker.getRelationship(), Optional.of(caretaker.getAddress()));

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
        CaretakerCommand command = new CaretakerCommand(INDEX_FIRST_PERSON, caretaker.getName(),
                caretaker.getPhone(), caretaker.getRelationship(), Optional.of(caretaker.getAddress()));

        assertThrows(CommandException.class,
                Messages.MESSAGE_REQUIRE_PATIENT, () -> command.execute(model));
    }

    @Test
    public void equals() {
        Caretaker caretakerA = new CaretakerBuilder().withName("Alice").build();
        Caretaker caretakerB = new CaretakerBuilder().withName("Bob").build();

        CaretakerCommand addCaretakerA = new CaretakerCommand(INDEX_FIRST_PERSON, caretakerA.getName(),
                caretakerA.getPhone(), caretakerA.getRelationship(), Optional.of(caretakerA.getAddress()));
        CaretakerCommand addCaretakerB = new CaretakerCommand(INDEX_SECOND_PERSON, caretakerB.getName(),
                caretakerB.getPhone(), caretakerB.getRelationship(), Optional.of(caretakerB.getAddress()));

        // same object -> true
        assertTrue(addCaretakerA.equals(addCaretakerA));

        // same values -> true
        CaretakerCommand addCaretakerACopy = new CaretakerCommand(INDEX_FIRST_PERSON, caretakerA.getName(),
                caretakerA.getPhone(), caretakerA.getRelationship(), Optional.of(caretakerA.getAddress()));
        assertTrue(addCaretakerA.equals(addCaretakerACopy));

        // different types -> false
        assertFalse(addCaretakerA.equals(1));

        // null -> false
        assertFalse(addCaretakerA.equals(null));

        // different index -> false
        assertFalse(addCaretakerA.equals(addCaretakerB));

        // different caretaker -> false
        CaretakerCommand differentCaretaker = new CaretakerCommand(INDEX_FIRST_PERSON, caretakerB.getName(),
                caretakerB.getPhone(), caretakerB.getRelationship(), Optional.of(caretakerB.getAddress()));
        assertFalse(addCaretakerA.equals(differentCaretaker));
    }



    @Test
    public void execute_addCaretakerWithoutAddress_usesPatientAddress() throws Exception {
        // Arrange: patient has an address; no caretaker yet
        Patient patient = new PatientBuilder()
                .withAddress("123 Clementi Road")
                .withCaretaker(null)
                .build();
        model.setPerson(model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased()), patient);

        Name name = new Name("TestName");
        Phone phone = new Phone("12345678");
        Relationship relationship = new Relationship("TestRelationship");

        // Pass Optional.empty() so the command resolves to patient's address
        CaretakerCommand command = new CaretakerCommand(
                INDEX_FIRST_PERSON, name, phone, relationship, Optional.empty());

        // Act
        CommandResult result = command.execute(model);

        // Assert state: patient now has a caretaker whose address == patient's address
        Patient updatedPatient =
                (Patient) model.getFilteredPersonList().get(INDEX_FIRST_PERSON.getZeroBased());
        assertNotNull(updatedPatient.getCaretaker());
        Caretaker savedCaretaker = updatedPatient.getCaretaker();

        assertEquals(name, savedCaretaker.getName());
        assertEquals(phone, savedCaretaker.getPhone());
        assertEquals(relationship, savedCaretaker.getRelationship());
        assertEquals(patient.getAddress(), savedCaretaker.getAddress());

        // Assert message matches what the command reports
        String expectedMessage = String.format(CaretakerCommand.MESSAGE_SUCCESS,
                Messages.format(savedCaretaker), Messages.shortFormat(updatedPatient));
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }
}

