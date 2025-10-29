package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;

import java.util.List;
import java.util.Objects;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;

/**
 * Deletes a note from an existing patient in the address book.
 */
public class DeleteNoteCommand extends AbstractDeleteCommand<Patient> {

    public static final String COMMAND_WORD = "deletenote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Deletes a note of the patient identified "
            + "by the index number used in the displayed person list. "
            + "The note to delete is identified by its note index (1-based).\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ITEM_INDEX + "ITEM_INDEX\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ITEM_INDEX + "2";

    public static final String MESSAGE_DELETE_NOTE_SUCCESS = "Deleted note for patient: %1$s";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
            + "Notes can only be deleted from patients.";
    public static final String MESSAGE_INVALID_ITEM_INDEX = "The note index %1$s is invalid. "
            + "Patient has %2$s note(s).";

    private final int noteIndex;

    private Patient editedPatient;

    /**
     * Creates a DeleteNoteCommand to delete the specified note of the patient at the specified index.
     */
    public DeleteNoteCommand(Index targetIndex, int noteIndex) {
        super(targetIndex);
        this.noteIndex = noteIndex;
    }

    @Override
    protected List<Patient> getTargetList(Model model) {
        // Cast the person list to patients for the abstract class
        // We'll validate the actual type in validateDeletion if needed
        @SuppressWarnings("unchecked")
        List<Patient> patientList = (List<Patient>) (List<?>) model.getFilteredPersonList();
        return patientList;
    }

    @Override
    protected void validateDeletion(Model model, Patient patientToDelete) throws CommandException {
        // Check if the person is actually a patient
        if (!(patientToDelete instanceof Patient)) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, targetIndex.getOneBased()));
        }

        // Check if note index is valid
        List<Note> notes = patientToDelete.getNotes();
        if (noteIndex < 1 || noteIndex > notes.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_ITEM_INDEX,
                    noteIndex, notes.size()));
        }
    }

    @Override
    protected void deleteItem(Model model, Patient patient) {
        int zeroBasedNoteIndex = noteIndex - 1; // Convert to 0-based index
        editedPatient = patient.deleteNote(zeroBasedNoteIndex);
        model.setPerson(patient, editedPatient);
    }

    @Override
    protected String formatSuccessMessage(Patient deletedFromPatient) {
        return String.format(MESSAGE_DELETE_NOTE_SUCCESS, Messages.format(editedPatient));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeleteNoteCommand)) {
            return false;
        }

        DeleteNoteCommand otherCommand = (DeleteNoteCommand) other;
        return super.equals(other) && noteIndex == otherCommand.noteIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), noteIndex);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", getTargetIndex())
                .add("noteIndex", noteIndex)
                .toString();
    }
}
