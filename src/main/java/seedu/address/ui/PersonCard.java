package seedu.address.ui;

import java.util.Comparator;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * An UI component that displays information of a {@code Person}.
 */
public class PersonCard extends UiPart<Region> {

    private static final String FXML = "PersonListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final Person person;

    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label note;
    @FXML
    private Label appointment;
    @FXML
    private FlowPane tags;

    /**
     * Creates a {@code PersonCode} with the given {@code Person} and index to display.
     */
    public PersonCard(Person person, int displayedIndex) {
        super(FXML);
        this.person = person;
        id.setText(displayedIndex + ". ");
        name.setText(person.getName().fullName);
        phone.setText(person.getPhone().value);
        address.setText(person.getAddress().value);
        if (person instanceof Patient patient) {
            // Set note for patients
            if (patient.getNote() != null && !patient.getNote().value.equals("NIL")) {
                note.setText("Note: " + patient.getNote().value);
                note.setVisible(true);
                note.setManaged(true);
            } else {
                note.setVisible(false);
                note.setManaged(false);
            }
            // Set appointment for patients
            if (patient.getAppointment() != null && patient.getAppointment().toString().trim().length() > 0) {
                appointment.setText("Appointment: " + patient.getAppointment());
                appointment.setVisible(true);
                appointment.setManaged(true);
            } else {
                appointment.setVisible(false);
                appointment.setManaged(false);
            }
        } else {
            // For non-patients, hide note and appointment
            note.setVisible(false);
            note.setManaged(false);
            appointment.setVisible(false);
            appointment.setManaged(false);
        }
        person.getTags().stream()
                .sorted(Comparator.comparing(tag -> tag.tagName))
                .forEach(tag -> tags.getChildren().add(new Label(tag.tagName)));
    }
}
