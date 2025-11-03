package seedu.address.logic;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import seedu.address.logic.parser.Prefix;
import seedu.address.model.person.Appointment;
import seedu.address.model.person.Caretaker;
import seedu.address.model.person.Note;
import seedu.address.model.person.Patient;
import seedu.address.model.person.Person;

/**
 * Container for user visible messages.
 */
public class Messages {

    public static final String MESSAGE_UNKNOWN_COMMAND = "Unknown command";
    public static final String MESSAGE_INVALID_COMMAND_FORMAT = "Invalid command format! \n%1$s";
    public static final String MESSAGE_DUPLICATE_FIELDS =
                "Multiple values specified for the following single-valued field(s): ";
    public static final String MESSAGE_REQUIRE_PATIENT = "The person index provided is not a patient";

    public static String patientsListedOverview(int count) {
        return count == 1 ? "1 patient listed!" : count + " patients listed!";
    }

    /**
     * error message that accounts for grammar when number of patients is singular and plural in MediSaveContact
     *
     * @param total number of patients in MediSaveContact
     * @return message displayed upon giving invalid patient index
     */
    public static String invalidPatientIndex(int total) {
        String verb = (total <= 1) ? "is" : "are";
        String noun = (total <= 1) ? "patient" : "patients";
        return String.format("The patient index provided is invalid. There %s %d %s.", verb, total, noun);
    }

    /**
     * Returns an error message indicating the duplicate prefixes.
     */
    public static String getErrorMessageForDuplicatePrefixes(Prefix... duplicatePrefixes) {
        assert duplicatePrefixes.length > 0;

        Set<String> duplicateFields =
                Stream.of(duplicatePrefixes).map(Prefix::toString).collect(Collectors.toSet());

        return MESSAGE_DUPLICATE_FIELDS + String.join(" ", duplicateFields);
    }


    /**
     * Formats the {@code person} for display to the user.
     */
    public static String format(Person person) {
        if (person instanceof Patient patient) {
            return Messages.format(patient);
        }
        final StringBuilder builder = new StringBuilder();
        builder.append(person.getName())
                .append("; Phone: ")
                .append(person.getPhone())
                .append("; Address: ")
                .append(person.getAddress());

        return builder.toString();
    }

    /**
     * Formats the {@code patient} for display to the user.
     */
    public static String format(Patient patient) {
        final StringBuilder builder = new StringBuilder();
        String appointment;
        if (patient.getAppointment().isEmpty()) {
            appointment = "-";
        } else {
            appointment = patient.getAppointment().stream()
                    .map(Object::toString)
                    .collect(java.util.stream.Collectors.joining(", "));
        }
        builder.append(patient.getName())
                .append("; Phone: ")
                .append(patient.getPhone())
                .append("; Address: ")
                .append(patient.getAddress());


        patient.getTag().ifPresent(t -> builder.append("; Tag: ").append(capitalise(t.toString()) + " Priority"));

        List<Appointment> appointmentList = patient.getAppointment();

        if (!appointmentList.isEmpty()) {
            builder.append(" ; Appointment: ").append(appointment);
        }

        if (patient.getNotes() != null && !patient.getNotes().isEmpty()) {
            String notesString = patient.getNotes().stream()
                .map(Object::toString)
                .collect(java.util.stream.Collectors.joining(", "));

            builder.append("; Notes: ").append(notesString);
        }

        if (patient.getCaretaker() != null) {
            String caretakerString = format(patient.getCaretaker());
            builder.append("; Caretaker: {").append(caretakerString).append("}");
        }

        return builder.toString();
    }

    /**
     * Formats an {@link Note} for consistent display.
     */
    public static String format(Note note) {
        Objects.requireNonNull(note);
        return note.toString();
    }

    /**
     * Formats an {@link Appointment} for consistent display.
     */
    public static String format(Appointment appointment) {
        Objects.requireNonNull(appointment);
        StringBuilder builder = new StringBuilder()
                .append(appointment.getDate())
                .append("; ")
                .append(appointment.getTime());

        appointment.getNote().ifPresent(note -> builder.append("; Note: ").append(note));

        return builder.toString();
    }

    /**
     * Formats the {@code caretaker} for display to the user.
     */
    public static String format(Caretaker caretaker) {
        final StringBuilder builder = new StringBuilder();
        builder.append(caretaker.getName())
                .append("; Phone: ")
                .append(caretaker.getPhone())
                .append("; Address: ")
                .append(caretaker.getAddress())
                .append("; Relationship: ")
                .append(caretaker.getRelationship());

        return builder.toString();
    }

    /**
     * Formats the {@code patient} for display to the user.
     */
    public static String shortFormat(Patient patient) {
        final StringBuilder builder = new StringBuilder();
        builder.append("For ")
                .append(patient.getName())
                .append("; Phone: ")
                .append(patient.getPhone());

        return builder.toString();
    }

    /**
     * Formats the {@code patient} for display to the user.
     */
    public static String shortFormatWithNoFor(Person patient) {
        final StringBuilder builder = new StringBuilder();
        builder.append(patient.getName())
                .append("; Phone: ")
                .append(patient.getPhone());

        return builder.toString();
    }

    /**
     * Formats the {@code patient} for display to the user for edit command.
     * Should not have Appointment, Notes, and Caretaker
     */
    public static String formatForEdit(Patient patient) {
        final StringBuilder builder = new StringBuilder();
        builder.append(patient.getName())
                .append("; Phone: ")
                .append(patient.getPhone())
                .append("; Address: ")
                .append(patient.getAddress());

        patient.getTag().ifPresent(t -> builder.append("; Tag: ").append(capitalise(t.toString()) + " Priority"));
        return builder.toString();
    }

    private static String capitalise(String s) {
        String lower = s.toLowerCase();
        return Character.toUpperCase(lower.charAt(0)) + lower.substring(1);
    }

}
