package seedu.address.logic.commands;

import seedu.address.model.Model;

/**
 * Format full help instructions for every command for display.
 */
public class HelpCommand extends Command {

    public static final String COMMAND_WORD = "help";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Shows program usage instructions.\n"
            + "Example: " + COMMAND_WORD;

    public static final String SHOWING_HELP_MESSAGE = "Opened help window.";

    public static final String HELP_MESSAGE = "1. Add Patient\n"
            + "   Purpose: Add a new patient with personal details\n"
            + "   Format: add n/NAME p/PHONE a/ADDRESS [tag/TAG]\n"
            + "   Notes: TAG optional, values = high/medium/low (case-insensitive)\n\n"
            + "2. Find Patient\n"
            + "   Purpose: Search for patients by keyword(s)\n"
            + "   Format: find KEYWORD [MORE_KEYWORDS]\n"
            + "   Notes: Keywords must be alphabetic, case-insensitive\n\n"
            + "3. Delete Patient\n"
            + "   Purpose: Delete a patient by index\n"
            + "   Format: delete INDEX\n"
            + "   Notes: INDEX must exist, positive integer\n\n"
            + "4. List Patients\n"
            + "   Purpose: Show all patients in the list\n"
            + "   Format: list\n"
            + "   Example: list\n\n"
            + "5. Add Appointment\n"
            + "   Purpose: Schedule an appointment for a patient\n"
            + "   Format: appointment INDEX d/DATE t/TIME\n"
            + "   Notes: DATE = DD-MM-YYYY, TIME = HH:MM 24-hour, cannot be in past\n\n"
            + "6. Add Medical Notes\n"
            + "   Purpose: Add notes to a patient’s record\n"
            + "   Format: note INDEX note/NOTES\n"
            + "   Notes: NOTES max 200 characters\n\n";

    @Override
    public CommandResult execute(Model model) {
        return new CommandResult(SHOWING_HELP_MESSAGE, true, false);
    }
}
