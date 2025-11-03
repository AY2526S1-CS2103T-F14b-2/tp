package seedu.address.logic.commands;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Adds a note to a patient in the address book.
 */
public class NoteCommand extends Command {

    public static final String COMMAND_WORD = "note";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a note to the patient identified "
            + "by the index number used in the displayed patient list.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_NOTE + "NOTE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_NOTE + "Patient shows improved blood sugar levels today.";

    public static final String MESSAGE_SUCCESS = "Note added: %1$s\n%2$s";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
            + "Notes can only be added to patients.";
    public static final String MESSAGE_EMPTY_NOTE = "Note cannot be empty.";

    // Shared constants for note operations
    public static final String MESSAGE_INVALID_ITEM_INDEX = "The note at index %1$s is invalid. "
            + "Patient has %2$s note(s).";

    private final Index targetIndex;
    private final Note note;

    /**
     * Creates a NoteCommand to add the specified {@code Note} to the patient at the specified {@code Index}.
     */
    public NoteCommand(Index targetIndex, Note note) {
        requireNonNull(targetIndex);
        requireNonNull(note);
        this.targetIndex = targetIndex;
        this.note = note;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Person> lastShownList = model.getFilteredPersonList();

        ensureValidPatientIndex(targetIndex, model);

        Person personToAddNote = lastShownList.get(targetIndex.getZeroBased());

        if (!(personToAddNote instanceof Patient)) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, targetIndex.getOneBased()));
        }

        Patient patientToAddNote = (Patient) personToAddNote;
        Patient updatedPatient = patientToAddNote.addNote(note);

        model.setPerson(patientToAddNote, updatedPatient);
        return new CommandResult(String.format(MESSAGE_SUCCESS,
                Messages.format(note),
                Messages.shortFormat(updatedPatient)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof NoteCommand)) {
            return false;
        }

        NoteCommand otherNoteCommand = (NoteCommand) other;
        return targetIndex.equals(otherNoteCommand.targetIndex)
                && note.equals(otherNoteCommand.note);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("note", note)
                .toString();
    }
}
