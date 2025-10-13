package seedu.address.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashSet;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.GuiSettings;
import seedu.address.model.person.Address;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Name;
import seedu.address.model.person.NameContainsKeywordsPredicate;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.testutil.AddressBookBuilder;
import seedu.address.testutil.PatientBuilder;



public class ModelManagerTest {

    private static final String FUTURE_DATE = "31-12-2099";
    private static final String FUTURE_TIME = "15:30";
    private ModelManager modelManager = new ModelManager();

    @Test
    public void constructor() {
        assertEquals(new UserPrefs(), modelManager.getUserPrefs());
        assertEquals(new GuiSettings(), modelManager.getGuiSettings());
        assertEquals(new AddressBook(), new AddressBook(modelManager.getAddressBook()));
    }

    @Test
    public void setUserPrefs_nullUserPrefs_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setUserPrefs(null));
    }

    @Test
    public void setUserPrefs_validUserPrefs_copiesUserPrefs() {
        UserPrefs userPrefs = new UserPrefs();
        userPrefs.setAddressBookFilePath(Paths.get("address/book/file/path"));
        userPrefs.setGuiSettings(new GuiSettings(1, 2, 3, 4));
        modelManager.setUserPrefs(userPrefs);
        assertEquals(userPrefs, modelManager.getUserPrefs());

        // Modifying userPrefs should not modify modelManager's userPrefs
        UserPrefs oldUserPrefs = new UserPrefs(userPrefs);
        userPrefs.setAddressBookFilePath(Paths.get("new/address/book/file/path"));
        assertEquals(oldUserPrefs, modelManager.getUserPrefs());
    }

    @Test
    public void setGuiSettings_nullGuiSettings_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setGuiSettings(null));
    }

    @Test
    public void setGuiSettings_validGuiSettings_setsGuiSettings() {
        GuiSettings guiSettings = new GuiSettings(1, 2, 3, 4);
        modelManager.setGuiSettings(guiSettings);
        assertEquals(guiSettings, modelManager.getGuiSettings());
    }

    @Test
    public void setAddressBookFilePath_nullPath_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.setAddressBookFilePath(null));
    }

    @Test
    public void setAddressBookFilePath_validPath_setsAddressBookFilePath() {
        Path path = Paths.get("address/book/file/path");
        modelManager.setAddressBookFilePath(path);
        assertEquals(path, modelManager.getAddressBookFilePath());
    }

    @Test
    public void hasPerson_nullPerson_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> modelManager.hasPerson(null));
    }

    @Test
    public void hasPerson_personNotInAddressBook_returnsFalse() {
        assertFalse(modelManager.hasPerson(ALICE));
    }

    @Test
    public void hasPerson_personInAddressBook_returnsTrue() {
        modelManager.addPerson(ALICE);
        assertTrue(modelManager.hasPerson(ALICE));
    }

    @Test
    public void getFilteredPersonList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, () -> modelManager.getFilteredPersonList().remove(0));
    }

    @Test
    public void hasAppointment_patientWithAppointment_returnsTrue() {
        Patient patientWithAppointment = new PatientBuilder()
                .withAppointment(FUTURE_DATE, FUTURE_TIME).build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(patientWithAppointment);
        ModelManager manager = new ModelManager(addressBook, new UserPrefs());

        assertTrue(manager.hasAppointment(patientWithAppointment));
    }

    @Test
    public void hasAppointment_patientWithoutAppointment_returnsFalse() {
        Patient patient = new PatientBuilder().build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(patient);
        ModelManager manager = new ModelManager(addressBook, new UserPrefs());

        assertFalse(manager.hasAppointment(patient));
    }

    @Test
    public void addAppointment_patientWithoutAppointment_appointmentAdded() {
        Patient patient = new PatientBuilder().build();
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(patient);
        ModelManager manager = new ModelManager(addressBook, new UserPrefs());

        Patient updated = manager.addAppointment(patient, FUTURE_DATE, FUTURE_TIME);

        assertEquals(new Appointment(FUTURE_DATE, FUTURE_TIME), updated.getAppointment());
        Patient storedPatient = (Patient) manager.getFilteredPersonList().get(0);
        assertEquals(new Appointment(FUTURE_DATE, FUTURE_TIME), storedPatient.getAppointment());
    }

    @Test
    public void addAppointment_nonPatient_throwsIllegalArgumentException() {
        Person nonPatient = new Person(new Name("Non Patient"), new Phone("91234567"),
                new Address("Somewhere"), new HashSet<>());
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(nonPatient);
        ModelManager manager = new ModelManager(addressBook, new UserPrefs());

        assertThrows(IllegalArgumentException.class, () ->
            manager.addAppointment(nonPatient, FUTURE_DATE, FUTURE_TIME));
    }

    @Test
    public void hasAppointment_nonPatient_returnsFalse() {
        Person nonPatient = new Person(new Name("Non Patient"), new Phone("91234567"),
                new Address("Somewhere"), new HashSet<>());
        AddressBook addressBook = new AddressBook();
        addressBook.addPerson(nonPatient);
        ModelManager manager = new ModelManager(addressBook, new UserPrefs());

        assertFalse(manager.hasAppointment(nonPatient));
    }

    @Test
    public void equals() {
        AddressBook addressBook = new AddressBookBuilder().withPerson(ALICE).withPerson(BENSON).build();
        AddressBook differentAddressBook = new AddressBook();
        UserPrefs userPrefs = new UserPrefs();

        // same values -> returns true
        modelManager = new ModelManager(addressBook, userPrefs);
        ModelManager modelManagerCopy = new ModelManager(addressBook, userPrefs);
        assertTrue(modelManager.equals(modelManagerCopy));

        // same object -> returns true
        assertTrue(modelManager.equals(modelManager));

        // null -> returns false
        assertNotEquals(null, modelManager);

        // different types -> returns false
        assertFalse(modelManager.equals(5));

        // different addressBook -> returns false
        assertFalse(modelManager.equals(new ModelManager(differentAddressBook, userPrefs)));

        // different filteredList -> returns false
        String[] keywords = ALICE.getName().fullName.split("\\s+");
        modelManager.updateFilteredPersonList(new NameContainsKeywordsPredicate(Arrays.asList(keywords)));
        assertFalse(modelManager.equals(new ModelManager(addressBook, userPrefs)));

        // resets modelManager to initial state for upcoming tests
        modelManager.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);

        // different userPrefs -> returns false
        UserPrefs differentUserPrefs = new UserPrefs();
        differentUserPrefs.setAddressBookFilePath(Paths.get("differentFilePath"));
        assertFalse(modelManager.equals(new ModelManager(addressBook, differentUserPrefs)));
    }
}
