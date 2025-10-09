package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Set;

import seedu.address.model.tag.Tag;

public class Patient extends Person {

    private final Note note;

    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags) {
        super(name, phone, email, address, tags);
        this.note = null;
    }

    public Patient(Name name, Phone phone, Email email, Address address, Set<Tag> tags, Note note) {
        super(name, phone, email, address, tags);
        requireAllNonNull(note);
        this.note = note;
    }

    public Note getNote() {
        return note;
    }
}
