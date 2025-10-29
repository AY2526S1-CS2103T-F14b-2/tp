package seedu.address.logic.commands;

import java.util.List;

import seedu.address.commons.core.index.Index;
import seedu.address.commons.util.ToStringBuilder;
import seedu.address.logic.Messages;
import seedu.address.model.Model;
import seedu.address.model.person.Patient;

/**
 * Deletes a patient identified using it's displayed index from the address book.
 */
public class DeletePatientCommand extends AbstractDeleteCommand<Patient> {

    public static final String COMMAND_WORD = "deletepatient";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the patient identified by the index number used in the displayed patient list.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_PATIENT_SUCCESS = "Deleted Patient: %1$s";

    public DeletePatientCommand(Index targetIndex) {
        super(targetIndex);
    }

    @Override
    protected List<Patient> getTargetList(Model model) {
        // Cast the person list to patients for the abstract class
        // We'll validate the actual type in validateEdit if needed
        @SuppressWarnings("unchecked")
        List<Patient> patientList = (List<Patient>) (List<?>) model.getFilteredPersonList();
        return patientList;
    }

    @Override
    protected void deleteItem(Model model, Patient patient) {
        model.deletePerson(patient);
    }

    @Override
    protected String formatSuccessMessage(Patient deletedPatient) {
        return String.format(MESSAGE_DELETE_PATIENT_SUCCESS, Messages.format(deletedPatient));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof DeletePatientCommand)) {
            return false;
        }

        return super.equals(other);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", getTargetIndex())
                .toString();
    }
}
