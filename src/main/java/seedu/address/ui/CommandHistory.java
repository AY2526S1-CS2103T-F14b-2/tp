package seedu.address.ui;


import java.util.ArrayList;
import java.util.List;

/**
 * Maintain history of successful commands entered and the draft command input by the user
 */
public class CommandHistory {

    private final List<String> items = new ArrayList<>();

    private int pointer = 0;

    /**
     * Add new command String to the list of past successful commands
     *
     * @param cmd String of the command message
     */
    public void add(String cmd) {
        if (cmd == null) {
            return;
        }
        String s = cmd.strip();
        if (s.isEmpty()) {
            return;
        }
        items.add(s);
        pointer = items.size();
    }

    /**
     * Returns whether navigating backwards is possible
     */
    public boolean canBack() {
        return pointer > 0;
    }

    /**
     * Returns whether navigating forward is possible
     */
    public boolean canForward() {
        return pointer < items.size();
    }

    /**
     * Move pointer back by one, if navigating backwards is possible. Triggered by up arrow.
     */
    public String back() {
        if (!canBack()) {
            return null;
        }
        pointer--;
        return items.get(pointer);
    }

    /**
     * Move pointer forward by one, if navigating forwards is possible. Triggered by down arrow.
     */
    public String front() {
        if (!canForward()) {
            return null;
        }
        pointer++;
        return pointer == items.size() ? "" : items.get(pointer);
    }

    public void movePointerToEnd() {
        pointer = items.size();
    }





}
