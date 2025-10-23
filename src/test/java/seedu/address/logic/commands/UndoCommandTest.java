package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.Assert.assertThrows;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.AddressBook;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.testutil.PatientBuilder;

public class UndoCommandTest {

    @Test
    public void execute_noHistory_throwsCommandHistory() {
        Model model = new ModelManager(new AddressBook(), new UserPrefs());
        UndoCommand undo = new UndoCommand();

        assertThrows(CommandException.class,
                UndoCommand.MESSAGE_NO_COMMANDS_TO_UNDO, () -> undo.execute(model));

    }

    @Test
    public void execute_hasHistory_success() {
        AddressBook ab = new AddressBook();
        Model model = new ModelManager(ab, new UserPrefs());
        Model expectedModel = new ModelManager(new AddressBook(ab), new UserPrefs());

        model.addPerson(new PatientBuilder()
                .withName("Bea")
                .withPhone("785845549")
                .withAddress("82 Grimace Avenue")
                .withTag("medium")
                .build());

        CommandResult expectedCommandResult = new CommandResult(UndoCommand.MESSAGE_SUCCESS);
        assertCommandSuccess(new UndoCommand(), model, expectedCommandResult, expectedModel);
    }

    @Test
    public void equals_hashCode_toString() {
        UndoCommand u1 = new UndoCommand();
        UndoCommand u2 = new UndoCommand();

        assertTrue(u1.equals(u1));
        assertTrue(u2.equals(u2));

        assertFalse(u1.equals(1));

        assertNotEquals(null, u1);

        assertEquals(u1.hashCode(), u2.hashCode());

        String expectedString = UndoCommand.class.getCanonicalName() + "{}";
        assertEquals(expectedString, u1.toString());

    }


}
