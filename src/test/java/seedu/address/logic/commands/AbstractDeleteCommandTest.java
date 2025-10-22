package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Tests for AbstractDeleteCommand equals/hashCode behavior.
 */
public class AbstractDeleteCommandTest {

    private static class DeletePersonStub extends AbstractDeleteCommand<Object> {
        DeletePersonStub(Index index) {
            super(index);
        }

        @Override
        protected java.util.List<Object> getTargetList(seedu.address.model.Model model) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void deleteItem(seedu.address.model.Model model, Object item) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String getInvalidIndexMessage() {
            return "invalid";
        }

        @Override
        protected String formatSuccessMessage(Object deletedItem) {
            return "deleted";
        }
    }

    private static class DeleteNoteStub extends AbstractDeleteCommand<Object> {
        DeleteNoteStub(Index index) {
            super(index);
        }

        @Override
        protected java.util.List<Object> getTargetList(seedu.address.model.Model model) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void deleteItem(seedu.address.model.Model model, Object item) {
            throw new UnsupportedOperationException();
        }

        @Override
        protected String getInvalidIndexMessage() {
            return "invalid";
        }

        @Override
        protected String formatSuccessMessage(Object deletedItem) {
            return "deleted";
        }
    }

    @Test
    public void equals_sameClassSameIndex_returnsTrue() {
        DeletePersonStub a = new DeletePersonStub(Index.fromOneBased(1));
        DeletePersonStub b = new DeletePersonStub(Index.fromOneBased(1));
        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
    }

    @Test
    public void equals_sameClassDifferentIndex_returnsFalse() {
        DeletePersonStub a = new DeletePersonStub(Index.fromOneBased(1));
        DeletePersonStub b = new DeletePersonStub(Index.fromOneBased(2));
        assertNotEquals(a, b);
    }

    @Test
    public void equals_differentClassSameIndex_returnsFalse() {
        DeletePersonStub a = new DeletePersonStub(Index.fromOneBased(1));
        DeleteNoteStub b = new DeleteNoteStub(Index.fromOneBased(1));
        assertNotEquals(a, b);
    }

    @Test
    public void equals_nullAndOtherType_behaviour() {
        DeletePersonStub a = new DeletePersonStub(Index.fromOneBased(1));
        assertNotEquals(null, a);
        assertNotEquals(a, new Object());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        DeletePersonStub command = new DeletePersonStub(Index.fromOneBased(1));
        assertEquals(command, command); // Tests the "other == this" branch
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new DeletePersonStub(null));
    }

    @Test
    public void getTargetIndex_returnsCorrectIndex() {
        Index expectedIndex = Index.fromOneBased(2);
        DeletePersonStub command = new DeletePersonStub(expectedIndex);
        assertEquals(expectedIndex, command.getTargetIndex());
    }

    @Test
    public void execute_validIndex_success() throws Exception {
        TestDeleteCommand command = new TestDeleteCommand(Index.fromOneBased(1));
        seedu.address.model.Model model = new seedu.address.model.ModelManager();

        CommandResult result = command.execute(model);
        assertEquals("Successfully deleted: Item1", result.getFeedbackToUser());
        assertTrue(command.wasDeleteItemCalled());
        assertTrue(command.wasUpdateModelCalled());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        TestDeleteCommand command = new TestDeleteCommand(Index.fromOneBased(10));
        seedu.address.model.Model model = new seedu.address.model.ModelManager();

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Invalid index message", exception.getMessage());
    }

    @Test
    public void execute_indexAtBoundary_success() throws Exception {
        TestDeleteCommand command = new TestDeleteCommand(Index.fromOneBased(3));
        seedu.address.model.Model model = new seedu.address.model.ModelManager();

        CommandResult result = command.execute(model);
        assertEquals("Successfully deleted: Item3", result.getFeedbackToUser());
    }

    @Test
    public void execute_validationFails_throwsCommandException() {
        TestDeleteCommandWithValidation command = new TestDeleteCommandWithValidation(Index.fromOneBased(1));
        seedu.address.model.Model model = new seedu.address.model.ModelManager();

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    public void execute_nullModel_throwsNullPointerException() {
        TestDeleteCommand command = new TestDeleteCommand(Index.fromOneBased(1));
        assertThrows(NullPointerException.class, () -> command.execute(null));
    }

    @Test
    public void execute_emptyTargetList_throwsCommandException() {
        TestDeleteCommandWithEmptyList command = new TestDeleteCommandWithEmptyList(Index.fromOneBased(1));
        seedu.address.model.Model model = new seedu.address.model.ModelManager();

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Invalid index message", exception.getMessage());
    }

    @Test
    public void execute_customInvalidIndexMessage_throwsCommandExceptionWithCustomMessage() {
        TestDeleteCommandWithCustomMessage command = new TestDeleteCommandWithCustomMessage(Index.fromOneBased(10));
        seedu.address.model.Model model = new seedu.address.model.ModelManager();

        CommandException exception = assertThrows(CommandException.class, () -> command.execute(model));
        assertEquals("Custom invalid index message", exception.getMessage());
    }

    @Test
    public void hashCode_equalObjects_sameHashCode() {
        DeletePersonStub command1 = new DeletePersonStub(Index.fromOneBased(1));
        DeletePersonStub command2 = new DeletePersonStub(Index.fromOneBased(1));
        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void hashCode_differentObjects_differentHashCode() {
        DeletePersonStub command1 = new DeletePersonStub(Index.fromOneBased(1));
        DeletePersonStub command2 = new DeletePersonStub(Index.fromOneBased(2));
        assertNotEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void hashCode_includesClassInformation() {
        DeletePersonStub command1 = new DeletePersonStub(Index.fromOneBased(1));
        DeleteNoteStub command2 = new DeleteNoteStub(Index.fromOneBased(1));
        assertNotEquals(command1.hashCode(), command2.hashCode());
    }

    /**
     * Concrete test implementation for testing execute method workflow.
     */
    private static class TestDeleteCommand extends AbstractDeleteCommand<String> {
        private static final java.util.List<String> TEST_LIST =
            java.util.Arrays.asList("Item1", "Item2", "Item3");

        private boolean deleteItemCalled = false;
        private boolean updateModelCalled = false;

        TestDeleteCommand(Index index) {
            super(index);
        }

        @Override
        protected java.util.List<String> getTargetList(seedu.address.model.Model model) {
            return TEST_LIST;
        }

        @Override
        protected void deleteItem(seedu.address.model.Model model, String item) {
            deleteItemCalled = true;
        }

        @Override
        protected String getInvalidIndexMessage() {
            return "Invalid index message";
        }

        @Override
        protected String formatSuccessMessage(String deletedItem) {
            return "Successfully deleted: " + deletedItem;
        }

        @Override
        protected void updateModelAfterDeletion(seedu.address.model.Model model) {
            updateModelCalled = true;
        }

        public boolean wasDeleteItemCalled() {
            return deleteItemCalled;
        }

        public boolean wasUpdateModelCalled() {
            return updateModelCalled;
        }
    }

    /**
     * Test command that always fails validation.
     */
    private static class TestDeleteCommandWithValidation extends TestDeleteCommand {
        TestDeleteCommandWithValidation(Index index) {
            super(index);
        }

        @Override
        protected void validateDeletion(seedu.address.model.Model model, String itemToDelete)
                throws CommandException {
            throw new CommandException("Validation failed");
        }
    }

    /**
     * Test command with empty target list.
     */
    private static class TestDeleteCommandWithEmptyList extends AbstractDeleteCommand<String> {
        TestDeleteCommandWithEmptyList(Index index) {
            super(index);
        }

        @Override
        protected java.util.List<String> getTargetList(seedu.address.model.Model model) {
            return java.util.Arrays.asList(); // Empty list
        }

        @Override
        protected void deleteItem(seedu.address.model.Model model, String item) {
            // Mock implementation
        }

        @Override
        protected String getInvalidIndexMessage() {
            return "Invalid index message";
        }

        @Override
        protected String formatSuccessMessage(String deletedItem) {
            return "Successfully deleted: " + deletedItem;
        }
    }

    /**
     * Test command with custom invalid index message.
     */
    private static class TestDeleteCommandWithCustomMessage extends TestDeleteCommand {
        TestDeleteCommandWithCustomMessage(Index index) {
            super(index);
        }

        @Override
        protected String getInvalidIndexMessage() {
            return "Custom invalid index message";
        }
    }
}
