package seedu.address.logic.commands;

import static seedu.address.logic.commands.NoteCommand.MESSAGE_INVALID_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_ITEM_INDEX;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NOTE;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Edits a note of an existing patient in the address book.
 */
public class EditNoteCommand extends AbstractEditCommand<Patient, EditNoteCommand.EditNoteDescriptor> {

    public static final String COMMAND_WORD = "editnote";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits a note of the patient identified "
            + "by the index number used in the displayed person list. "
            + "The note to edit is identified by its note index (1-based).\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_ITEM_INDEX + "ITEM_INDEX "
            + PREFIX_NOTE + "NEW_NOTE\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_ITEM_INDEX + "2 "
            + PREFIX_NOTE + "Updated note content";

    public static final String MESSAGE_EDIT_NOTE_SUCCESS = "Edited note for patient: %1$s";
    public static final String MESSAGE_NOT_PATIENT = "The person at index %1$s is not a patient. "
            + "Notes can only be edited for patients.";

    /**
     * Creates an EditNoteCommand to edit the specified note of the patient at the specified index.
     */
    public EditNoteCommand(Index targetIndex, EditNoteDescriptor editNoteDescriptor) {
        super(targetIndex, editNoteDescriptor);
    }

    @Override
    protected List<Patient> getTargetList(Model model) {
        // Convert the person list to a proper patient list with type checking
        List<Person> personList = model.getFilteredPersonList();
        List<Patient> patientList = new java.util.ArrayList<>();

        for (Person person : personList) {
            if (person instanceof Patient) {
                patientList.add((Patient) person);
            } else {
                // Add a null placeholder to maintain index correspondence
                patientList.add(null);
            }
        }

        return patientList;
    }

    @Override
    protected void validateEdit(Model model, Patient patientToEdit, EditNoteDescriptor editDescriptor)
            throws CommandException {
        // If patientToEdit is null, it means the person at this index is not a Patient
        if (patientToEdit == null) {
            throw new CommandException(String.format(MESSAGE_NOT_PATIENT, super.getIndex().getOneBased()));
        }

        // Check if note index is valid
        List<Note> notes = patientToEdit.getNotes();
        int noteIndex = editDescriptor.getNoteIndex();
        if (noteIndex < 1 || noteIndex > notes.size()) {
            throw new CommandException(String.format(MESSAGE_INVALID_ITEM_INDEX,
                    noteIndex, notes.size()));
        }
    }

    @Override
    protected boolean isAnyFieldEdited(EditNoteDescriptor editDescriptor) {
        return editDescriptor.getNote().isPresent();
    }

    @Override
    protected Patient createEditedItem(Patient patientToEdit, EditNoteDescriptor editNoteDescriptor) {
        // patientToEdit is the correct patient from the filtered list
        assert patientToEdit != null;

        Note newNote = editNoteDescriptor.getNoteValue(); // We know it exists from validation
        int noteIndex = editNoteDescriptor.getNoteIndex() - 1; // Convert to 0-based index

        return patientToEdit.editNote(noteIndex, newNote);
    }

    @Override
    protected void updateModel(Model model, Patient originalPatient, Patient editedPatient) {
        model.setPerson(originalPatient, editedPatient);
    }

    @Override
    protected String formatSuccessMessage(Patient editedPatient) {
        return String.format(MESSAGE_EDIT_NOTE_SUCCESS, Messages.format(editedPatient));
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("index", super.getIndex())
                .add("editNoteDescriptor", super.getEditDescriptor())
                .toString();
    }

    /**
     * Stores the details to edit a note with. The note field will replace the
     * corresponding note at the specified index.
     */
    public static class EditNoteDescriptor {
        private Note note;
        private int noteIndex;

        public EditNoteDescriptor() {}

        /**
         * Copy constructor.
         */
        public EditNoteDescriptor(EditNoteDescriptor toCopy) {
            setNote(toCopy.note);
            setNoteIndex(toCopy.noteIndex);
        }

        /**
         * Sets the note to edit to.
         */
        public void setNote(Note note) {
            this.note = note;
        }

        /**
         * Returns the note to edit to.
         */
        public Optional<Note> getNote() {
            return Optional.ofNullable(note);
        }

        /**
         * Returns the note value directly, assuming it has been validated to exist.
         * Should only be called after validation confirms the note is present.
         */
        public Note getNoteValue() {
            return note;
        }

        /**
         * Sets the note index (1-based) to edit.
         */
        public void setNoteIndex(int noteIndex) {
            this.noteIndex = noteIndex;
        }

        /**
         * Returns the note index (1-based) to edit.
         */
        public int getNoteIndex() {
            return noteIndex;
        }

        @Override
        public boolean equals(Object other) {
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditNoteDescriptor)) {
                return false;
            }

            EditNoteDescriptor otherEditNoteDescriptor = (EditNoteDescriptor) other;
            return Objects.equals(note, otherEditNoteDescriptor.note)
                    && noteIndex == otherEditNoteDescriptor.noteIndex;
        }

        @Override
        public String toString() {
            return new ToStringBuilder(this)
                    .add("note", note)
                    .add("noteIndex", noteIndex)
                    .toString();
        }
    }
}
