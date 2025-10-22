package seedu.address.model;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

import seedu.address.testutil.PatientBuilder;
import seedu.address.testutil.TypicalPatients;

public class VersionedAddressBookTest {

    private AddressBook makeAddressBook(Person... persons) {
        AddressBook ab = new AddressBook();
        for (Person p: persons) {
            ab.addPerson(p);
        }
        return ab;
    }

    @Test
    void constructor_null_throwsNpe() {
        assertThrows(NullPointerException.class, () -> new VersionedAddressBook(null));
    }

    @Test
    void constructor_initialState_copiedIntoCurrent() {
        AddressBook initial = makeAddressBook();
        VersionedAddressBook vab = new VersionedAddressBook(initial);

        assertEquals(initial, vab.getAddressBook());
        assertNotSame(initial, vab.getAddressBook(), "Should create a copy of the address book and not poiunt to the same instance");
    }

    @Test
    void hasHistory_works() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        assertFalse(vab.hasHistory());

        vab.update();
        assertTrue(vab.hasHistory());
    }

    @Test
    void undo_noHistory_throwsError() {
        VersionedAddressBook vab = new VersionedAddressBook(new AddressBook());
        assertFalse(vab.hasHistory());
        assertThrows(IllegalArgumentException.class, vab::undo);
    }

    @Test
    void undo_backToSnapshotSuccessfully() {
        Patient alice = TypicalPatients.ALICE;
        Patient bob = TypicalPatients.BOB;

        AddressBook firstVersion = makeAddressBook(alice);
        AddressBook secondVersion = makeAddressBook(alice, bob);

        VersionedAddressBook vab = new VersionedAddressBook(firstVersion);

        //store first version as snapshot
        vab.update();

        //roll to second version
        vab.getAddressBook().resetData(secondVersion);
        assertEquals(secondVersion, vab.getAddressBook());

        //back to first version after undoing
        vab.undo();
        assertEquals(firstVersion, vab.getAddressBook());


    }


    @Test
    void update_withPatientBuilder() {
        Patient john = new PatientBuilder()
                .withName("John Tang")
                .withAddress("Six Seven Ohio Street")
                .withPhone("67676767")
                .withTag("low")
                .build();

        Patient william = new PatientBuilder()
                .withName("William Low")
                .withAddress("Abekeke Street")
                .withPhone("48502244")
                .withTag("medium")
                .withNote("vegan")
                .build();

        AddressBook firstVersion = makeAddressBook(john);
        VersionedAddressBook vab = new VersionedAddressBook(firstVersion);

        vab.update();
        //add william
        vab.getAddressBook().addPerson(william);


        vab.undo();
        assertEquals(makeAddressBook(john), vab.getAddressBook());

    }

    @Test
    void equals_ifSameState_true() {
        AddressBook base = new AddressBook();
        VersionedAddressBook v0 = new VersionedAddressBook(base);
        VersionedAddressBook v1 = new VersionedAddressBook(base);
        assertEquals(v0, v1);
        assertEquals(v0.hashCode(), v1.hashCode());
    }

    @Test
    void equals_ifSameState_false() {
        Patient bob = TypicalPatients.BOB;
        VersionedAddressBook v0 = new VersionedAddressBook(makeAddressBook());
        VersionedAddressBook v1 = new VersionedAddressBook(makeAddressBook(bob));
        assertNotEquals(v0, v1);
    }









}
