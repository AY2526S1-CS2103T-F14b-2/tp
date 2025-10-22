package seedu.address.storage;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import seedu.address.commons.exceptions.IllegalValueException;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Person;
import seedu.address.model.person.Relationship;

/**
 * Jackson-friendly version of {@link Caretaker}.
 */
public class JsonAdaptedCaretaker extends JsonAdaptedPerson {
    private final String relationship;

    /**
     * Constructs a {@code JsonAdaptedCaretaker} with the given caretaker details.
     */
    @JsonCreator
    public JsonAdaptedCaretaker(@JsonProperty("name") String name,
                              @JsonProperty("phone") String phone,
                              @JsonProperty("address") String address,
                              @JsonProperty("relationship") String relationship) {
        super(name, phone, address);
        this.relationship = relationship;
    }

    /**
     * Converts a given {@code Caretaker} into this class for Jackson use.
     */
    public JsonAdaptedCaretaker(Caretaker source) {
        super(source);
        this.relationship = source.getRelationship().toString();
    }

    @Override
    public Caretaker toModelType() throws IllegalValueException {
        Person base = super.toModelType();
        Relationship relationship = new Relationship(this.relationship);

        return new Caretaker(base.getName(), base.getPhone(), base.getAddress(),
                relationship);
    }
}
