package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.logic.commands.CommandTestUtil.showPersonAtIndex;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HIGH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_LOW;
import static seedu.address.testutil.EmptyPersons.getEmptyAddressBook;
import static seedu.address.testutil.TypicalIndexes.INDEX_FIRST_PERSON;
import static seedu.address.testutil.TypicalPatients.getTypicalAddressBook;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.tag.Tag;

public class ListTagCommandTest {

    private static final Tag TAG_HIGH = new Tag(VALID_TAG_HIGH);
    private static final Tag TAG_LOW = new Tag(VALID_TAG_LOW);

    private Model model;
    private Model expectedModel;

    @BeforeEach
    public void setUp() {
        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());
    }

    @Test
    public void execute_filtersToMatchingTag_showsOnlyTaggedPatients() {
        // expectedModel should reflect filtering by "high"
        expectedModel.updateFilteredPersonList(p ->
                p.getTag().map(TAG_HIGH::equals).orElse(false));

        ListTagCommand cmd = new ListTagCommand(TAG_HIGH);
        String expectedMsg = String.format(ListTagCommand.MESSAGE_SUCCESS, TAG_HIGH);

        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

    @Test
    public void execute_whenModelAlreadyFiltered_stillFiltersByTag() {
        // Pre-filter the model arbitrarily
        showPersonAtIndex(model, INDEX_FIRST_PERSON);

        // expectedModel = address book filtered by the tag, regardless of prior filter
        expectedModel.updateFilteredPersonList(p ->
                p.getTag().map(TAG_HIGH::equals).orElse(false));

        ListTagCommand cmd = new ListTagCommand(TAG_HIGH);
        String expectedMsg = String.format(ListTagCommand.MESSAGE_SUCCESS, TAG_HIGH);

        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

    @Test
    public void execute_emptyAddressBook_resultsInEmptyList() {
        model = new ModelManager(getEmptyAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

        // Filtering on empty model remains empty
        expectedModel.updateFilteredPersonList(p ->
                p.getTag().map(TAG_LOW::equals).orElse(false));

        ListTagCommand cmd = new ListTagCommand(TAG_LOW);
        String expectedMsg = String.format(ListTagCommand.MESSAGE_SUCCESS, TAG_LOW);

        assertCommandSuccess(cmd, model, expectedMsg, expectedModel);
    }

    // ---------------- equals() tests ----------------

    @Test
    public void equals_sameTag_returnsTrue() {
        ListTagCommand a = new ListTagCommand(TAG_HIGH);
        ListTagCommand b = new ListTagCommand(new Tag("high")); // equal by value
        assertTrue(a.equals(b));
        assertTrue(a.equals(a));
    }

    @Test
    public void equals_differentTag_returnsFalse() {
        ListTagCommand a = new ListTagCommand(TAG_HIGH);
        ListTagCommand b = new ListTagCommand(TAG_LOW);
        assertFalse(a.equals(b));
    }

    @Test
    public void equals_nullOrDifferentType_returnsFalse() {
        ListTagCommand a = new ListTagCommand(TAG_HIGH);
        assertFalse(a.equals(null));
        assertFalse(a.equals(new ListCommand())); // different class
    }
}
