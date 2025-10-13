package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import static seedu.address.logic.commands.AddAppointmentCommand.MESSAGE_DUPLICATE_APPOINTMENT;
import static seedu.address.logic.commands.AddAppointmentCommand.MESSAGE_SUCCESS;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ReadOnlyAddressBook;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import static seedu.address.testutil.Assert.assertThrows;
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
        assertEquals(String.format(MESSAGE_SUCCESS, Messages.format(updatedPatient)),
                result.getFeedbackToUser());
        assertEquals(new Appointment(FUTURE_DATE, FUTURE_TIME), updatedPatient.getAppointment());
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
                seedu.address.logic.Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, () ->
                command.execute(modelStub));
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
            return person instanceof Patient && ((Patient) person).getAppointment() != null;
        }

        @Override
        public Patient addAppointment(Person person, String date, String time) {
            Patient patient = (Patient) person;
            Patient updated = patient.addAppointment(new Appointment(date, time));
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
    }
}
