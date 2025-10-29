package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.AddAppointmentCommand.MESSAGE_DUPLICATE_APPOINTMENT;
import static seedu.address.logic.commands.AddAppointmentCommand.MESSAGE_SUCCESS;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Comparator;

import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.testutil.PatientBuilder;




public class AddAppointmentCommandTest {

    private static final String FUTURE_DATE = "31-12-2099";
    private static final String FUTURE_TIME = "15:30";

    @Test
    public void execute_patientWithoutAppointment_success() throws Exception {
        Patient patient = new PatientBuilder().withName("Alice").build();
        ModelStubAcceptingAppointment modelStub = new ModelStubAcceptingAppointment(patient);
        AddAppointmentCommand command = new AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME);

        CommandResult result = command.execute(modelStub);

        Patient updatedPatient = (Patient) modelStub.getFilteredPersonList().get(0);
        assertEquals(String.format(MESSAGE_SUCCESS,
            Messages.formatAppointment(updatedPatient.getAppointment().get(0)),
            Messages.shortFormat(updatedPatient)),
            result.getFeedbackToUser());
        assertEquals(1, updatedPatient.getAppointment().size());
        assertEquals(new Appointment(FUTURE_DATE, FUTURE_TIME), updatedPatient.getAppointment().get(0));
    }

    @Test
    public void execute_patientWithExistingAppointment_throwsCommandException() {
        Patient patientWithAppointment = new PatientBuilder()
                .withAppointment(FUTURE_DATE, FUTURE_TIME).build();
        ModelStubAcceptingAppointment modelStub = new ModelStubAcceptingAppointment(patientWithAppointment);
        AddAppointmentCommand command = new AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME);

        assertThrows(CommandException.class, MESSAGE_DUPLICATE_APPOINTMENT, () -> command.execute(modelStub));
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        Patient patient = new PatientBuilder().build();
        ModelStubAcceptingAppointment modelStub = new ModelStubAcceptingAppointment(patient);
        AddAppointmentCommand command = new AddAppointmentCommand(Index.fromOneBased(2), FUTURE_DATE, FUTURE_TIME);

        assertThrows(CommandException.class,
                String.format(seedu.address.logic.Messages.MESSAGE_INVALID_PATIENT_DISPLAYED_INDEX,
                        modelStub.getSize()), () ->
                command.execute(modelStub));
    }

    @Test
    public void execute_invalidDate_throwsCommandException() {
        Patient patient = new PatientBuilder().build();
        ModelStubThrowingIllegalArgument modelStub = new ModelStubThrowingIllegalArgument(patient);
        AddAppointmentCommand command = new AddAppointmentCommand(Index.fromOneBased(1), "invalid-date", FUTURE_TIME);

        assertThrows(CommandException.class, Appointment.MESSAGE_CONSTRAINTS, () ->
                command.execute(modelStub));
    }

    @Test
    public void execute_patientWithAppointmentWithNote_success() throws Exception {
        Patient patient = new PatientBuilder().withName("Bob").build();
        ModelStubAcceptingAppointment modelStub = new ModelStubAcceptingAppointment(patient);
        Note testNote = new Note("Follow-up checkup");
        AddAppointmentCommand command = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME,
            testNote);

        CommandResult result = command.execute(modelStub);

        Patient updatedPatient = (Patient) modelStub.getFilteredPersonList().get(0);
        assertEquals(1, updatedPatient.getAppointment().size());
        assertEquals(new Appointment(FUTURE_DATE, FUTURE_TIME, testNote), updatedPatient.getAppointment().get(0));
    }

    @Test
    public void equals() {
        AddAppointmentCommand addAppointmentFirstCommand = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME);
        AddAppointmentCommand addAppointmentSecondCommand = new
            AddAppointmentCommand(Index.fromOneBased(2), FUTURE_DATE, FUTURE_TIME);

        // same object -> returns true
        assertEquals(addAppointmentFirstCommand, addAppointmentFirstCommand);

        // same values -> returns true
        AddAppointmentCommand addAppointmentFirstCommandCopy = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME);
        assertEquals(addAppointmentFirstCommand, addAppointmentFirstCommandCopy);

        // different types -> returns false
        assertEquals(false, addAppointmentFirstCommand.equals(1));

        // null -> returns false
        assertEquals(false, addAppointmentFirstCommand.equals(null));

        // different index -> returns false
        assertEquals(false, addAppointmentFirstCommand.equals(addAppointmentSecondCommand));

        // different date -> returns false
        AddAppointmentCommand differentDateCommand = new
            AddAppointmentCommand(Index.fromOneBased(1), "15-12-2025", FUTURE_TIME);
        assertEquals(false, addAppointmentFirstCommand.equals(differentDateCommand));

        // different time -> returns false
        AddAppointmentCommand differentTimeCommand = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, "16:00");
        assertEquals(false, addAppointmentFirstCommand.equals(differentTimeCommand));

        // different note -> returns false
        AddAppointmentCommand differentNoteCommand = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME, new Note("Different note"));
        assertEquals(false, addAppointmentFirstCommand.equals(differentNoteCommand));

        // same values with note -> returns true
        Note testNote = new Note("Test note");
        AddAppointmentCommand commandWithNote1 = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME, testNote);
        AddAppointmentCommand commandWithNote2 = new
            AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME, testNote);
        assertEquals(commandWithNote1, commandWithNote2);
    }

    @Test
    public void hashCodeTest() {
        AddAppointmentCommand command1 = new AddAppointmentCommand(Index.fromOneBased(1), FUTURE_DATE, FUTURE_TIME);

        // Test that hashCode can be called without throwing an exception
        int hashCode = command1.hashCode();
        // Hash code should be consistent
        assertEquals(hashCode, command1.hashCode());
    }

    @Test
    public void toStringMethod() {
        Index targetIndex = Index.fromOneBased(1);
        AddAppointmentCommand addAppointmentCommand = new AddAppointmentCommand(targetIndex, FUTURE_DATE, FUTURE_TIME);
        String expected = AddAppointmentCommand.class.getCanonicalName() + "{targetIndex=" + targetIndex
                + ", date=" + FUTURE_DATE + ", time=" + FUTURE_TIME + ", description=null}";
        assertEquals(expected, addAppointmentCommand.toString());
    }

    private static class ModelStubThrowingIllegalArgument implements Model {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private ModelStubThrowingIllegalArgument(Patient patient) {
            this.persons.add(patient);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public boolean hasAppointment(Person person) {
            return false;
        }

        @Override
        public Patient addAppointment(Person person, String date, String time, Note desc) {
            throw new IllegalArgumentException(Appointment.MESSAGE_CONSTRAINTS);
        }

        // The remaining methods are unsupported for this stub
        @Override
        public void setUserPrefs(seedu.address.model.ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError();
        }
        @Override
        public seedu.address.model.ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError();
        }
        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new AssertionError();
        }
        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new AssertionError();
        }
        @Override
        public java.nio.file.Path getAddressBookFilePath() {
            throw new AssertionError();
        }
        @Override
        public void setAddressBookFilePath(java.nio.file.Path addressBookFilePath) {
            throw new AssertionError();
        }
        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError();
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError();
        }
        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError();
        }
        @Override
        public void deletePerson(Person target) {
            throw new AssertionError();
        }
        @Override
        public void addPerson(Person person) {
            throw new AssertionError();
        }
        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError();
        }
        @Override
        public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
            throw new AssertionError();
        }
        @Override
        public boolean canUndo() {
            throw new AssertionError();
        }
        @Override
        public void undo() {
            throw new AssertionError();
        }
        @Override
        public int getSize() {
            return this.persons.size();
        }
        @Override
        public void sortPersons(Comparator<? super Person> c) {
            throw new AssertionError();
        }
    }

    private static class ModelStubAcceptingAppointment implements Model {
        private final ObservableList<Person> persons = FXCollections.observableArrayList();
        private ModelStubAcceptingAppointment(Patient patient) {
            this.persons.add(patient);
        }

        @Override
        public ObservableList<Person> getFilteredPersonList() {
            return persons;
        }

        @Override
        public boolean hasAppointment(Person person) {
            return person instanceof Patient && !((Patient) person).getAppointment().isEmpty();
        }

        @Override
        public Patient addAppointment(Person person, String date, String time, Note desc) {
            Patient patient = (Patient) person;
            Appointment appointment = desc == null
                    ? new Appointment(date, time)
                    : new Appointment(date, time, desc);
            if (patient.getAppointment().contains(appointment)) {
                throw new IllegalArgumentException(MESSAGE_DUPLICATE_APPOINTMENT);
            }
            Patient updated = patient.addAppointment(appointment);
            persons.set(persons.indexOf(person), updated);
            return updated;
        }

        // The remaining methods are unsupported for this stub
        @Override
        public void setUserPrefs(seedu.address.model.ReadOnlyUserPrefs userPrefs) {
            throw new AssertionError();
        }
        @Override
        public seedu.address.model.ReadOnlyUserPrefs getUserPrefs() {
            throw new AssertionError();
        }
        @Override
        public seedu.address.commons.core.GuiSettings getGuiSettings() {
            throw new AssertionError();
        }
        @Override
        public void setGuiSettings(seedu.address.commons.core.GuiSettings guiSettings) {
            throw new AssertionError();
        }
        @Override
        public java.nio.file.Path getAddressBookFilePath() {
            throw new AssertionError();
        }
        @Override
        public void setAddressBookFilePath(java.nio.file.Path addressBookFilePath) {
            throw new AssertionError();
        }
        @Override
        public void setAddressBook(ReadOnlyAddressBook newData) {
            throw new AssertionError();
        }
        @Override
        public ReadOnlyAddressBook getAddressBook() {
            throw new AssertionError();
        }
        @Override
        public boolean hasPerson(Person person) {
            throw new AssertionError();
        }
        @Override
        public void deletePerson(Person target) {
            throw new AssertionError();
        }
        @Override
        public void addPerson(Person person) {
            throw new AssertionError();
        }
        @Override
        public void setPerson(Person target, Person editedPerson) {
            throw new AssertionError();
        }
        @Override
        public void updateFilteredPersonList(java.util.function.Predicate<Person> predicate) {
            throw new AssertionError();
        }
        @Override
        public boolean canUndo() {
            throw new AssertionError();
        }
        @Override
        public void undo() {
            throw new AssertionError();
        }
        @Override
        public int getSize() {
            return this.persons.size();
        }
        public void sortPersons(Comparator<? super Person> c) {
            throw new AssertionError();
        }


    }
}
