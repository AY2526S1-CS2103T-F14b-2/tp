package seedu.address.logic.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    private static final String ADD_PATIENT_MESSAGE = "Add Patient\n"
            + "   Purpose: Add a new patient with personal details\n"
            + "   Format: patient n/NAME p/PHONE a/ADDRESS [tag/TAG]\n"
            + "   Notes: TAG optional, values = high/medium/low (case-insensitive)\n";

    private static final String FIND_PATIENT_MESSAGE = "Find Patient\n"
            + "   Purpose: Search for patients by keyword(s)\n"
            + "   Format: find KEYWORD [MORE_KEYWORDS]\n"
            + "   Notes: Keywords must be alphabetic, case-insensitive\n";

    private static final String EDIT_PATIENT_MESSAGE = "Edit Patient\n"
            + "   Purpose: Edit an existing patient's details\n"
            + "   Format: editpatient INDEX [n/NAME] [p/PHONE] [a/ADDRESS] [tag/TAG]\n"
            + "   Notes: One of NAME/PHONE/ADDRESS/TAG must be supplied\n";

    private static final String DELETE_PATIENT_MESSAGE = "Delete Patient\n"
            + "   Purpose: Delete a patient by index\n"
            + "   Format: deletepatient INDEX\n"
            + "   Notes: INDEX must exist, positive integer\n";

    private static final String LIST_PATIENT_MESSAGE = "List Patients\n"
            + "   Purpose: Show all patients in the list\n"
            + "   Format: list [tag/TAG]\n"
            + "   Notes: TAG optional, values = high/medium/low (case-insensitive)\n";

    private static final String ADD_APPT_MESSAGE = "Add Appointment\n"
            + "   Purpose: Schedule an appointment for a patient\n"
            + "   Format: appt INDEX d/DATE t/TIME [note/NOTE]\n"
            + "   Notes: DATE = DD-MM-YYYY, TIME = HH:MM 24-hour, cannot be in past\n";

    private static final String EDIT_APPT_MESSAGE = "Edit Appointment\n"
            + "   Purpose: Update an existing appointment for a patient\n"
            + "   Format: editappt INDEX i/APPOINTMENT_INDEX [d/DATE] [t/TIME] [note/NOTE]\n"
            + "   Notes: Provide at least one of DATE/TIME/NOTE; APPOINTMENT_INDEX is 1-based per patient\n";

    private static final String DELETE_APPT_MESSAGE = "Delete Appointment\n"
            + "   Purpose: Delete an existing appointment for a patient\n"
            + "   Format: deleteappt INDEX i/APPOINTMENT_INDEX\n"
            + "   Notes: INDEX and APPOINTMENT_INDEX must exist, positive integer\n";

    private static final String ADD_NOTES_MESSAGE = "Add Medical Notes\n"
            + "   Purpose: Add notes to a patient’s record\n"
            + "   Format: note INDEX note/NOTE\n"
            + "   Notes: NOTE max 200 characters & cannot be empty\n";

    private static final String EDIT_NOTES_MESSAGE = "Edit Medical Notes\n"
            + "   Purpose: Edit an existing note in a patient’s record\n"
            + "   Format: editnote INDEX i/ITEM_INDEX note/NOTE\n"
            + "   Notes: NOTE max 200 characters & cannot be empty\n";

    private static final String DELETE_NOTES_MESSAGE = "Delete Medical Notes\n"
            + "   Purpose: Delete an existing note in a patient’s record\n"
            + "   Format: deletenote INDEX i/ITEM_INDEX\n"
            + "   Notes: INDEX and ITEM_INDEX must exist, positive integer\n";

    private static final String ADD_CARETAKER_MESSAGE = "Add Caretaker\n"
            + "   Purpose: Add caretaker to a patient’s record\n"
            + "   Format: caretaker INDEX n/NAME p/PHONE a/ADDRESS r/RELATIONSHIP\n";

    private static final String DELETE_CARETAKER_MESSAGE = "Delete Caretaker\n"
            + "   Purpose: Delete caretaker from a patient’s record\n"
            + "   Format: deletecaretaker INDEX\n"
            + "   Notes: Patient must have a caretaker already assigned\n";

    private static final String CLEAR_MESSAGE = "Clear all entries\n"
            + "   Purpose: Clear all entries from MediSaveContact.\n"
            + "   Format: clear\n";

    public static final List<String> HELP_MESSAGES = new ArrayList<>(Arrays.asList(
            ADD_PATIENT_MESSAGE,
            FIND_PATIENT_MESSAGE,
            EDIT_PATIENT_MESSAGE,
            DELETE_PATIENT_MESSAGE,
            LIST_PATIENT_MESSAGE,
            ADD_APPT_MESSAGE,
            EDIT_APPT_MESSAGE,
            DELETE_APPT_MESSAGE,
            ADD_NOTES_MESSAGE,
            EDIT_NOTES_MESSAGE,
            DELETE_NOTES_MESSAGE,
            ADD_CARETAKER_MESSAGE,
            DELETE_CARETAKER_MESSAGE,
            CLEAR_MESSAGE
    ));

    public static String getFormattedHelpMessages() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < HELP_MESSAGES.size(); i++) {
            sb.append(i + 1).append(". ")
                    .append(HELP_MESSAGES.get(i))
                    .append("\n");
        }
        return sb.toString();
    }


    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
