package seedu.address.ui;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

public class CommandHistoryTest {
    @Test
    void add_nullOrBlank_ignored() {
        CommandHistory ch = new CommandHistory();

        // add two commands in the ch
        ch.add(null);
        ch.add("    ");
        ch.add("    list   ");
        ch.add("\t add n/Amy \n");

        //navigate back to previous commands
        assertTrue(ch.canBack());
        assertEquals(" add n/Amy ".trim(), ch.back()); // go back to second/latest successful command
        assertTrue(ch.canBack());
        assertEquals("list", ch.back()); //go back to first command
        assertFalse(ch.canBack()); // no more commands to go back to
    }

    @Test
    void add_pointerAtEnd() {
        CommandHistory ch = new CommandHistory();
        ch.add("add n/junwei p/933333 a/Ohio");
        ch.add("list");

        //command always at end after new command is added so cannot go forward anymore
        assertFalse(ch.canForward());
        assertNull(ch.front());
    }



}
