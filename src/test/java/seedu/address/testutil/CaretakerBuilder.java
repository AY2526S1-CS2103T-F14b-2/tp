package seedu.address.testutil;

import seedu.address.model.person.Address;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Name;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;

/**
 * A utility class to help with building Caretaker objects.
 */
public class CaretakerBuilder {
    public static final String DEFAULT_NAME = "Smolder Polder";
    public static final String DEFAULT_PHONE = "85355255";
    public static final String DEFAULT_ADDRESS = "123, Jurong West Ave 6, #08-111";
    public static final String DEFAULT_RELATIONSHIP = "Son";

    private Name name;
    private Phone phone;
    private Address address;
    private Relationship relationship;

    /**
     * Creates a {@code CaretakerBuilder} with the default details.
     */
    public CaretakerBuilder() {
        name = new Name(DEFAULT_NAME);
        phone = new Phone(DEFAULT_PHONE);
        address = new Address(DEFAULT_ADDRESS);
        relationship = new Relationship(DEFAULT_RELATIONSHIP);
    }

    /**
     * Initializes the CaretakerBuilder with the data of {@code caretakerToCopy}.
     */
    public CaretakerBuilder(Caretaker caretakerToCopy) {
        name = caretakerToCopy.getName();
        phone = caretakerToCopy.getPhone();
        address = caretakerToCopy.getAddress();
        relationship = caretakerToCopy.getRelationship();
    }

    /**
     * Sets the {@code Name} of the {@code Caretaker} that we are building.
     */
    public CaretakerBuilder withName(String name) {
        this.name = new Name(name);
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code Caretaker} that we are building.
     */
    public CaretakerBuilder withAddress(String address) {
        this.address = new Address(address);
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code Caretaker} that we are building.
     */
    public CaretakerBuilder withPhone(String phone) {
        this.phone = new Phone(phone);
        return this;
    }

    /**
     * Sets the {@code Relationship} of the {@code Caretaker} that we are building.
     */
    public CaretakerBuilder withRelationship(String relationship) {
        this.relationship = new Relationship(relationship);
        return this;
    }


    public Caretaker build() {
        return new Caretaker(name, phone, address, relationship);
    }

}
