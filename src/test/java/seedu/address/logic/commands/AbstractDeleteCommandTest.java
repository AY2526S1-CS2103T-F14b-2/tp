package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.jupiter.api.Test;

import seedu.address.commons.core.index.Index;

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
}
