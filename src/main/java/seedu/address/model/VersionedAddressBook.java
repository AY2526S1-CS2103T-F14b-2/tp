package seedu.address.model;

import static java.util.Objects.requireNonNull;

import java.util.ArrayDeque;
import java.util.Deque;

import javafx.collections.ObservableList;
import seedu.address.model.person.Person;


/**
 * Current AddressBook instance that the UI binds to and a LIFO stack of
 * snapshot of the AddressBook before every successful command
 */
public class VersionedAddressBook implements ReadOnlyAddressBook {
    private AddressBook current;
    private final Deque<ReadOnlyAddressBook> historyLog =
            new ArrayDeque<>();

    /**
     * Constructs a versioned address book starting from the given ReadOnlyAddressBook instance
     *
     * @param state initial read-only state that the VersionedAddressBook originates from
     */
    public VersionedAddressBook(ReadOnlyAddressBook state) {
        requireNonNull(state);
        this.current = new AddressBook(state);
    }

    /**
     * Pushes a new snapshot into the LIFO stack
     */
    public void update() {
        historyLog.push(new AddressBook(current));
    }

    /**
     * Checks whether LIFO stack has ReadOnlyAddressBook instances
     */
    public boolean hasHistory() {
        return !historyLog.isEmpty();
    }

    /**
     * Undo the AddressBook that UI binds to the most recent prior snapshot
     *
     * @throws IllegalStateException if LIFO stack is empty
     */
    public void undo() {
        if (!hasHistory()) {
            throw new IllegalArgumentException("Nothing to undo");
        }
        ReadOnlyAddressBook pastVersion = new AddressBook(historyLog.pop());
        current.resetData(pastVersion);
    }

    public AddressBook getAddressBook() {
        return current;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        if (other instanceof VersionedAddressBook) {
            VersionedAddressBook o = (VersionedAddressBook) other;
            return this.current.equals(o.current);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return current.hashCode();
    }

    @Override
    public ObservableList<Person> getPersonList() {
        return current.getPersonList();
    }

}
