package seedu.address.testutil;

import seedu.address.logic.commands.EditCaretakerCommand;
import seedu.address.logic.commands.EditCaretakerCommand.EditCaretakerDescriptor;
import seedu.address.model.person.Address;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Relationship;
import seedu.address.model.tag.Tag;


/**
 * A utility class to help with building EditPersonDescriptor objects.
 */
public class EditCaretakerDescriptorBuilder {

    private EditCaretakerDescriptor descriptor;

    public EditCaretakerDescriptorBuilder() {
        descriptor = new EditCaretakerDescriptor();
    }

    public EditCaretakerDescriptorBuilder(EditCaretakerDescriptor descriptor) {
        this.descriptor = new EditCaretakerDescriptor(descriptor);
    }

    /**
     * Returns an {@code EditCaretakerDescriptor} with fields containing {@code person}'s details
     */
    public EditCaretakerDescriptorBuilder(Person person) {
        descriptor = new EditCaretakerDescriptor();
        descriptor.setName(person.getName());
        descriptor.setPhone(person.getPhone());
        descriptor.setAddress(person.getAddress());
    }

    /**
     * Sets the {@code Name} of the {@code EditCaretakerDescriptor} that we are building.
     */
    public EditCaretakerDescriptorBuilder withName(String name) {
        descriptor.setName(new Name(name));
        return this;
    }

    /**
     * Sets the {@code Phone} of the {@code EditCaretakerDescriptor} that we are building.
     */
    public EditCaretakerDescriptorBuilder withPhone(String phone) {
        descriptor.setPhone(new Phone(phone));
        return this;
    }

    /**
     * Sets the {@code Address} of the {@code EditCaretakerDescriptor} that we are building.
     */
    public EditCaretakerDescriptorBuilder withAddress(String address) {
        descriptor.setAddress(new Address(address));
        return this;
    }

    /**
     * Sets the {@code Relationship} of the {@code EditCaretakerDescriptor} that we are building.
     */
    public EditCaretakerDescriptorBuilder withRelationship(String relationship) {
        descriptor.setRelationship(new Relationship(relationship));
        return this;
    }

    public EditCaretakerDescriptor build() {
        return descriptor;
    }
}
