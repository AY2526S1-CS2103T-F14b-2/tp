package seedu.address.model.person;

import static seedu.address.commons.util.CollectionUtil.requireAllNonNull;

<<<<<<< HEAD
import java.util.Objects;
import java.util.Optional;
=======
import java.util.*;
>>>>>>> 41f4ddc4 (Convert patient's format to accept only one tag instead of a set of tags)

import seedu.address.commons.util.ToStringBuilder;
import seedu.address.model.tag.Tag;

/**
 * Represents a Person in the address book.
 * Guarantees: details are present and not null, field values are validated, immutable.
 */
public class Person {

    // Identity fields
    private final Name name;
    private final Phone phone;

    // Data fields
    private final Address address;
    private final Tag tag;

    /**
     * Every field must be present and not null.
     */
    public Person(Name name, Phone phone, Address address, Tag tag) {
        requireAllNonNull(name, phone, address);
        this.name = name;
        this.phone = phone;
        this.address = address;
        this.tag = tag;
    }

    public Name getName() {
        return name;
    }

    public Phone getPhone() {
        return phone;
    }

    public Address getAddress() {
        return address;
    }

    /**
     * Returns an {@link Optional} with the tag if present else return an {@link Optional#empty()}
     */
    public Optional<Tag> getTag() {
        return Optional.ofNullable(tag);
    }

    /**
     * Returns true if both persons have the same name.
     * This defines a weaker notion of equality between two persons.
     */
    public boolean isSamePerson(Person other) {
        if (other == this) {
            return true;
        }
        if (other == null) {
            return false;
        }

        return other.getName().equals(getName())
                && other.getPhone().equals(getPhone());
    }

    /**
     * Returns true if both persons have the same identity and data fields.
     * This defines a stronger notion of equality between two persons.
     */
    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Person)) {
            return false;
        }

        Person otherPerson = (Person) other;

        return name.equals(otherPerson.name)
                && phone.equals(otherPerson.phone)
                && address.equals(otherPerson.address)
<<<<<<< HEAD
                && Objects.equals(tag, otherPerson.tag);
=======
                && tag.equals(otherPerson.tag);
>>>>>>> 41f4ddc4 (Convert patient's format to accept only one tag instead of a set of tags)
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(name, phone, address, tag);
    }

    @Override
    public String toString() {
        ToStringBuilder sb = new ToStringBuilder(this)
                .add("name", name)
                .add("phone", phone)
                .add("address", address);

        getTag().ifPresent(tag -> sb.add("tag", tag));
        return sb.toString();
    }

}
