package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.testutil.Assert.assertThrows;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.collections.ObservableList;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;
import seedu.address.testutil.PatientBuilder;

public class SortAppointmentCommandTest {

    //helpers
    private static Model makeModel(Person... persons) {
        AddressBook ab = new AddressBook();
        for (Person p: persons) {
            ab.addPerson(p);
        }
        return new ModelManager(ab, new UserPrefs());
    }

    private static LocalDateTime dateTime(String isoLocalString) {
        return LocalDateTime.parse(isoLocalString);
    }


    @Test
    public void execute_noPatients_throwsCommandException() {
        AddressBook ab = new AddressBook();

        Model model = new ModelManager(ab, new UserPrefs());
        SortAppointmentCommand cmd = new SortAppointmentCommand();

        assertThrows(CommandException.class,
                SortAppointmentCommand.MESSAGE_NO_PATIENTS, () -> cmd.execute(model));

    }

    @Test
    public void execute_gotPatientsbutNoAppointments_throwsCommandException() {
        Patient alice = new PatientBuilder().withName("Alice").build();
        Patient bob = new PatientBuilder().withName("Bob").build();

        Model model = makeModel(alice, bob);
        SortAppointmentCommand cmd = new SortAppointmentCommand();

        assertThrows(CommandException.class,
                SortAppointmentCommand.MESSAGE_NO_APPOINTMENTS, () -> cmd.execute(model));

    }

    @Test
    public void execute_sorting_givenPatientsWithZeroOrOneAppointment() throws CommandException {
        //alice has the later appointment
        Patient alice = new PatientBuilder()
                .withName("Alice")
                .withAppointment("10-03-2026", "09:00")
                .build();

        //bob has the earlier appointment
        Patient bob = new PatientBuilder()
                .withName("Bob")
                .withAppointment("02-01-2026", "12:00")
                .build();

        //charlie has no appointments so he should be put after the three
        Patient charlie = new PatientBuilder()
                .withName("Charlie")
                .build();

        Model model = makeModel(alice, bob, charlie);

        SortAppointmentCommand cmd = new SortAppointmentCommand();
        CommandResult result = cmd.execute(model);
        assertEquals(SortAppointmentCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());

        ObservableList<Person> list = model.getFilteredPersonList();
        assertEquals(List.of(bob, alice, charlie), list);


    }

    @Test
    public void execute_sorting_givenPatientsWithMultipleAppointments() throws CommandException {
        //alice has the later appointment
        Patient alice = new PatientBuilder()
                .withName("Alice")
                .withAppointment("10-03-2026", "09:00")
                .withAppointment("11-04-2026", "12:00")
                .build();

        //bob has the earlier appointment
        Patient bob = new PatientBuilder()
                .withName("Bob")
                .withAppointment("12-02-2026", "12:00")
                .withAppointment("15-05-2026", "14:00")
                .build();

        Model model = makeModel(alice, bob);

        SortAppointmentCommand cmd = new SortAppointmentCommand();
        CommandResult result = cmd.execute(model);
        assertEquals(SortAppointmentCommand.MESSAGE_SUCCESS, result.getFeedbackToUser());

        ObservableList<Person> list = model.getFilteredPersonList();
        assertEquals(List.of(bob, alice), list);

    }

    @Test
    public void equals() {
        SortAppointmentCommand a = new SortAppointmentCommand();
        SortAppointmentCommand b = new SortAppointmentCommand();

        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
        assertFalse(a.equals(null));
    }


}
