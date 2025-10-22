package seedu.address.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Note;
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
    private VBox notesContainer;
    @FXML
    private VBox appointmentContainer;
    @FXML
    private VBox caretakerBox;
    @FXML
    private VBox caretakerContainer;
    @FXML
    private Label caretakerName;
    @FXML
    private Label caretakerPhone;
    @FXML
    private Label caretakerAddress;
    @FXML
    private Label caretakerRelationship;

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
            // Set notes for patients
            if (!patient.getNotes().isEmpty()) {
                notesContainer.setVisible(true);
                notesContainer.setManaged(true);
                for (Note note : patient.getNotes()) {
                    Label noteLabel = new Label("• " + note.value);
                    noteLabel.setWrapText(true);
                    noteLabel.getStyleClass().add("cell_small_label");
                    notesContainer.getChildren().add(noteLabel);
                }
            } else {
                notesContainer.setVisible(false);
                notesContainer.setManaged(false);
            }
            // Set appointment for patients
            if (!patient.getAppointment().isEmpty()) {
                appointmentContainer.setVisible(true);
                appointmentContainer.setManaged(true);
                appointmentContainer.getChildren().clear();
                for (Appointment appt : patient.getAppointment()) {
                    Label apptLabel = new Label("• " + appt.toString());
                    apptLabel.getStyleClass().add("cell_small_label");
                    appointmentContainer.getChildren().add(apptLabel);
                }
            } else {
                appointmentContainer.setVisible(false);
                appointmentContainer.setManaged(false);
            }

            if (patient.getCaretaker() != null) {
                caretakerBox.setVisible(true);
                caretakerBox.setManaged(true);

                var caretaker = patient.getCaretaker();

                caretakerName.setText(caretaker.getName().fullName);
                caretakerPhone.setText(caretaker.getPhone().value);
                caretakerAddress.setText(caretaker.getAddress().value);
                caretakerRelationship.setText(caretaker.getRelationship().value);
            } else {
                caretakerBox.setVisible(false);
                caretakerBox.setManaged(false);
            }

            tags.getChildren().clear();

            patient.getTag().ifPresent(tag ->
                    tags.getChildren().add(new Label(tag.tagName)));
        } else {
            // For non-patients, hide notes and appointment
            notesContainer.setVisible(false);
            notesContainer.setManaged(false);
            appointmentContainer.setVisible(false);
            appointmentContainer.setManaged(false);
        }
    }
}
