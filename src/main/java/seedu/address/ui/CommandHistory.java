package seedu.address.ui;


import java.util.ArrayList;
import java.util.List;

public class CommandHistory {
    private final List<String> items = new ArrayList<>();

    private int pointer = 0;

    public void add(String cmd) {
        if (cmd == null) return;
        String s = cmd.strip();
        if (s.isEmpty()) {
            return;
        }
        items.add(s);
        pointer = items.size();
    }

    public boolean canBack() {
        return pointer > 0;
    }

    public boolean canForward() {
        return pointer < items.size();
    }

    public String back() {
        if (!canBack()) {
            return null;
        }
        pointer--;
        return items.get(pointer);
    }

    public String front() {
        if (!canForward()) {
            return null;
        }
        pointer++;
        return pointer == items.size() ? "": items.get(pointer);
    }

    public void movePointerToEnd() {
        pointer = items.size();
    }





}
