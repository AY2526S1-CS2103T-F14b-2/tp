package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;

/**
 * Contains unit tests for AbstractEditCommand.
 * Uses concrete test implementations to verify the abstract class behavior.
 */
public class AbstractEditCommandTest {

    private static final String TEST_ITEM_1 = "Item1";
    private static final String TEST_ITEM_2 = "Item2";
    private static final String TEST_ITEM_3 = "Item3";
    private static final String EDITED_ITEM = "EditedItem";

    private static final TestEditDescriptor VALID_DESCRIPTOR = new TestEditDescriptor(true);
    private static final TestEditDescriptor EMPTY_DESCRIPTOR = new TestEditDescriptor(false);
    private static final TestEditDescriptor DUPLICATE_DESCRIPTOR = new TestEditDescriptor(true, true);

    private Model model = new ModelManager();

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new TestEditCommand(null, VALID_DESCRIPTOR));
    }

    @Test
    public void constructor_nullDescriptor_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
            new TestEditCommand(Index.fromOneBased(1), null));
    }

    @Test
    public void execute_validIndexAndDescriptor_success() throws Exception {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        CommandResult result = command.execute(model);

        assertEquals("Successfully edited: " + EDITED_ITEM, result.getFeedbackToUser());
    }

    @Test
    public void execute_invalidIndex_throwsCommandException() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(10), VALID_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, exception.getMessage());
    }

    @Test
    public void execute_indexAtBoundary_success() throws Exception {
        // Test index at the last valid position
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(3), VALID_DESCRIPTOR);
        CommandResult result = command.execute(model);

        assertEquals("Successfully edited: " + EDITED_ITEM, result.getFeedbackToUser());
    }

    @Test
    public void execute_noFieldsEdited_throwsCommandException() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), EMPTY_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals(AbstractEditCommand.MESSAGE_NOT_EDITED, exception.getMessage());
    }

    @Test
    public void execute_validationFails_throwsCommandException() {
        TestEditCommandWithValidation command = new TestEditCommandWithValidation(
            Index.fromOneBased(1), VALID_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals("Validation failed", exception.getMessage());
    }

    @Test
    public void execute_duplicateItem_throwsCommandException() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), DUPLICATE_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals("Duplicate item detected", exception.getMessage());
    }

    @Test
    public void getIndex_returnsCorrectIndex() {
        Index expectedIndex = Index.fromOneBased(2);
        TestEditCommand command = new TestEditCommand(expectedIndex, VALID_DESCRIPTOR);

        assertEquals(expectedIndex, command.getIndex());
    }

    @Test
    public void getEditDescriptor_returnsCorrectDescriptor() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);

        assertEquals(VALID_DESCRIPTOR, command.getEditDescriptor());
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        assertTrue(command.equals(command));
    }

    @Test
    public void equals_null_returnsFalse() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        assertFalse(command.equals(null));
    }

    @Test
    public void equals_differentClass_returnsFalse() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        assertFalse(command.equals("not a command"));
    }

    @Test
    public void equals_differentCommandType_returnsFalse() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        AlternateTestEditCommand command2 = new AlternateTestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentIndex_returnsFalse() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        TestEditCommand command2 = new TestEditCommand(Index.fromOneBased(2), VALID_DESCRIPTOR);

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_differentDescriptor_returnsFalse() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        TestEditCommand command2 = new TestEditCommand(Index.fromOneBased(1), EMPTY_DESCRIPTOR);

        assertFalse(command1.equals(command2));
    }

    @Test
    public void equals_sameIndexAndDescriptor_returnsTrue() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        TestEditCommand command2 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);

        assertTrue(command1.equals(command2));
    }

    @Test
    public void hashCode_equalObjects_sameHashCode() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        TestEditCommand command2 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);

        assertEquals(command1.hashCode(), command2.hashCode());
    }

    @Test
    public void hashCode_differentObjects_differentHashCode() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        TestEditCommand command2 = new TestEditCommand(Index.fromOneBased(2), VALID_DESCRIPTOR);

        // While not guaranteed, it's highly likely they'll have different hash codes
        // This tests the hash code includes the index
        assertTrue(command1.hashCode() != command2.hashCode());
    }

    @Test
    public void execute_customInvalidIndexMessage_throwsCommandExceptionWithCustomMessage() {
        TestEditCommandWithCustomMessage command = new TestEditCommandWithCustomMessage(
            Index.fromOneBased(10), VALID_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals("Custom invalid index message", exception.getMessage());
    }

    @Test
    public void execute_updateModelAfterEditCalled_success() throws Exception {
        TestEditCommandWithPostUpdate command = new TestEditCommandWithPostUpdate(
            Index.fromOneBased(1), VALID_DESCRIPTOR);
        command.execute(model);

        assertTrue(command.wasPostUpdateCalled());
    }

    @Test
    public void execute_zeroBasedIndexAtBoundary_throwsCommandException() {
        // Test with index that equals list size (should be invalid)
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(4), VALID_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, exception.getMessage());
    }

    @Test
    public void execute_withNullModel_throwsNullPointerException() {
        TestEditCommand command = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);

        assertThrows(NullPointerException.class, () ->
            command.execute(null));
    }

    @Test
    public void equals_withDifferentGenericTypes_returnsFalse() {
        TestEditCommand stringCommand = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        TestIntegerEditCommand intCommand = new TestIntegerEditCommand(Index.fromOneBased(1), 5);

        assertFalse(stringCommand.equals(intCommand));
    }

    @Test
    public void hashCode_includesClassInformation_differentForDifferentClasses() {
        TestEditCommand command1 = new TestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);
        AlternateTestEditCommand command2 = new AlternateTestEditCommand(Index.fromOneBased(1), VALID_DESCRIPTOR);

        // Hash codes should be different because they include class information
        assertTrue(command1.hashCode() != command2.hashCode());
    }

    @Test
    public void execute_emptyTargetList_throwsCommandException() {
        TestEditCommandWithEmptyList command = new TestEditCommandWithEmptyList(
            Index.fromOneBased(1), VALID_DESCRIPTOR);

        CommandException exception = assertThrows(CommandException.class, () ->
            command.execute(model));
        assertEquals(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX, exception.getMessage());
    }

    /**
     * Test descriptor class for testing AbstractEditCommand.
     */
    private static class TestEditDescriptor {
        private final boolean hasChanges;
        private final boolean createsDuplicate;

        public TestEditDescriptor(boolean hasChanges) {
            this(hasChanges, false);
        }

        public TestEditDescriptor(boolean hasChanges, boolean createsDuplicate) {
            this.hasChanges = hasChanges;
            this.createsDuplicate = createsDuplicate;
        }

        public boolean hasChanges() {
            return hasChanges;
        }

        public boolean createsDuplicate() {
            return createsDuplicate;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            if (!(other instanceof TestEditDescriptor)) {
                return false;
            }

            TestEditDescriptor otherDescriptor = (TestEditDescriptor) other;
            return hasChanges == otherDescriptor.hasChanges
                && createsDuplicate == otherDescriptor.createsDuplicate;
        }

        @Override
        public int hashCode() {
            return Objects.hash(hasChanges, createsDuplicate);
        }
    }

    /**
     * Concrete test implementation of AbstractEditCommand.
     */
    private static class TestEditCommand extends AbstractEditCommand<String, TestEditDescriptor> {

        public TestEditCommand(Index index, TestEditDescriptor descriptor) {
            super(index, descriptor);
        }

        @Override
        protected List<String> getTargetList(Model model) {
            return Arrays.asList(TEST_ITEM_1, TEST_ITEM_2, TEST_ITEM_3);
        }

        @Override
        protected boolean isAnyFieldEdited(TestEditDescriptor editDescriptor) {
            return editDescriptor.hasChanges();
        }

        @Override
        protected String createEditedItem(String itemToEdit, TestEditDescriptor editDescriptor) {
            return EDITED_ITEM;
        }

        @Override
        protected void validateUniqueItem(Model model, String originalItem, String editedItem)
                throws CommandException {
            if (editDescriptor.createsDuplicate()) {
                throw new CommandException("Duplicate item detected");
            }
        }

        @Override
        protected void updateModel(Model model, String originalItem, String editedItem) {
            // Mock implementation - no actual model update needed for tests
        }

        @Override
        protected String formatSuccessMessage(String editedItem) {
            return "Successfully edited: " + editedItem;
        }
    }

    /**
     * Test implementation with custom validation that always fails.
     */
    private static class TestEditCommandWithValidation extends TestEditCommand {

        public TestEditCommandWithValidation(Index index, TestEditDescriptor descriptor) {
            super(index, descriptor);
        }

        @Override
        protected void validateEdit(Model model, String itemToEdit, TestEditDescriptor editDescriptor)
                throws CommandException {
            throw new CommandException("Validation failed");
        }
    }

    /**
     * Alternative test command class to test different command type equality.
     */
    private static class AlternateTestEditCommand extends AbstractEditCommand<String, TestEditDescriptor> {

        public AlternateTestEditCommand(Index index, TestEditDescriptor descriptor) {
            super(index, descriptor);
        }

        @Override
        protected List<String> getTargetList(Model model) {
            return Arrays.asList(TEST_ITEM_1, TEST_ITEM_2, TEST_ITEM_3);
        }

        @Override
        protected boolean isAnyFieldEdited(TestEditDescriptor editDescriptor) {
            return editDescriptor.hasChanges();
        }

        @Override
        protected String createEditedItem(String itemToEdit, TestEditDescriptor editDescriptor) {
            return EDITED_ITEM;
        }

        @Override
        protected void updateModel(Model model, String originalItem, String editedItem) {
            // Mock implementation
        }

        @Override
        protected String formatSuccessMessage(String editedItem) {
            return "Successfully edited: " + editedItem;
        }
    }

    /**
     * Test implementation with custom invalid index message.
     */
    private static class TestEditCommandWithCustomMessage extends TestEditCommand {

        public TestEditCommandWithCustomMessage(Index index, TestEditDescriptor descriptor) {
            super(index, descriptor);
        }

        @Override
        protected String getInvalidIndexMessage() {
            return "Custom invalid index message";
        }
    }

    /**
     * Test implementation that tracks if updateModelAfterEdit was called.
     */
    private static class TestEditCommandWithPostUpdate extends TestEditCommand {
        private boolean postUpdateCalled = false;

        public TestEditCommandWithPostUpdate(Index index, TestEditDescriptor descriptor) {
            super(index, descriptor);
        }

        @Override
        protected void updateModelAfterEdit(Model model) {
            postUpdateCalled = true;
        }

        public boolean wasPostUpdateCalled() {
            return postUpdateCalled;
        }
    }

    /**
     * Test implementation with different generic types for testing equals behavior.
     */
    private static class TestIntegerEditCommand extends AbstractEditCommand<Integer, Integer> {

        public TestIntegerEditCommand(Index index, Integer descriptor) {
            super(index, descriptor);
        }

        @Override
        protected List<Integer> getTargetList(Model model) {
            return Arrays.asList(1, 2, 3);
        }

        @Override
        protected boolean isAnyFieldEdited(Integer editDescriptor) {
            return editDescriptor != 0;
        }

        @Override
        protected Integer createEditedItem(Integer itemToEdit, Integer editDescriptor) {
            return editDescriptor;
        }

        @Override
        protected void updateModel(Model model, Integer originalItem, Integer editedItem) {
            // Mock implementation
        }

        @Override
        protected String formatSuccessMessage(Integer editedItem) {
            return "Successfully edited: " + editedItem;
        }
    }

    /**
     * Test implementation that returns an empty target list.
     */
    private static class TestEditCommandWithEmptyList extends TestEditCommand {

        public TestEditCommandWithEmptyList(Index index, TestEditDescriptor descriptor) {
            super(index, descriptor);
        }

        @Override
        protected List<String> getTargetList(Model model) {
            return Arrays.asList(); // Empty list
        }
    }
}
