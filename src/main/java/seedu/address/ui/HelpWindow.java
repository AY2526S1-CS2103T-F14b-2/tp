package seedu.address.ui;

import java.util.logging.Logger;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.stage.Stage;
import seedu.address.commons.core.LogsCenter;

/**
 * Controller for a help page
 */
public class HelpWindow extends UiPart<Stage> {

    public static final String HELP_MESSAGE = "1. Add Patient\n" +
            "   Purpose: Add a new patient with personal details\n" +
            "   Format: add n/NAME p/PHONE a/ADDRESS [t/TAG]\n" +
            "   Notes: TAG optional, values = high/medium/low (case-insensitive)\n\n" +
            "2. Find Patient\n" +
            "   Purpose: Search for patients by keyword(s)\n" +
            "   Format: find KEYWORD [MORE_KEYWORDS]\n" +
            "   Notes: Keywords must be alphabetic, case-insensitive\n\n" +
            "3. Delete Patient\n" +
            "   Purpose: Delete a patient by index\n" +
            "   Format: delete INDEX\n" +
            "   Notes: INDEX must exist, positive integer\n\n" +
            "4. List Patients\n" +
            "   Purpose: Show all patients in the list\n" +
            "   Format: list\n" +
            "   Example: list\n\n" +
            "5. Add Appointment\n" +
            "   Purpose: Schedule an appointment for a patient\n" +
            "   Format: appointment INDEX d/DATE t/TIME\n" +
            "   Notes: DATE = DD-MM-YYYY, TIME = HH:MM 24-hour, cannot be in past\n\n" +
            "6. Add Medical Notes\n" +
            "   Purpose: Add notes to a patient’s record\n" +
            "   Format: note INDEX n/NOTES\n" +
            "   Notes: NOTES max 200 characters\n\n" +
            "7. View Patient Details\n" +
            "    Purpose: View a patient’s details quickly\n" +
            "    Format: view INDEX\n\n";

    private static final Logger logger = LogsCenter.getLogger(HelpWindow.class);
    private static final String FXML = "HelpWindow.fxml";

    @FXML
    private Label helpMessage;

    /**
     * Creates a new HelpWindow.
     *
     * @param root Stage to use as the root of the HelpWindow.
     */
    public HelpWindow(Stage root) {
        super(FXML, root);
        helpMessage.setText(HELP_MESSAGE);
    }

    /**
     * Creates a new HelpWindow.
     */
    public HelpWindow() {
        this(new Stage());
    }

    /**
     * Shows the help window.
     * @throws IllegalStateException
     *     <ul>
     *         <li>
     *             if this method is called on a thread other than the JavaFX Application Thread.
     *         </li>
     *         <li>
     *             if this method is called during animation or layout processing.
     *         </li>
     *         <li>
     *             if this method is called on the primary stage.
     *         </li>
     *         <li>
     *             if {@code dialogStage} is already showing.
     *         </li>
     *     </ul>
     */
    public void show() {
        logger.fine("Showing help page about the application.");
        getRoot().show();
        getRoot().centerOnScreen();
    }

    /**
     * Returns true if the help window is currently being shown.
     */
    public boolean isShowing() {
        return getRoot().isShowing();
    }

    /**
     * Hides the help window.
     */
    public void hide() {
        getRoot().hide();
    }

    /**
     * Focuses on the help window.
     */
    public void focus() {
        getRoot().requestFocus();
    }

}
