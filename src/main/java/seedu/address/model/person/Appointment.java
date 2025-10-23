package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;
import java.util.Optional;


/**
 * Represents a Person's appointment in the address book.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidAppointment(String)}
 */
public class Appointment {

    public static final String MESSAGE_CONSTRAINTS = "Date and time should be in the format DD-MM-YYYY HH:MM";
    public static final String MESSAGE_PAST_APPOINTMENT = "Appointment must be set in the future.";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDate date;
    private final LocalTime time;
    private final Note desc;

    /**
     * Constructs an {@code Appointment}.
     *
     * @param date A valid date.
     * @param time A valid time.
     * @param desc A valid note.
     */
    public Appointment(String date, String time, Note desc) {
        requireNonNull(date);
        requireNonNull(time);

        try {
            this.date = LocalDate.parse(date, DATE_FORMATTER);
            this.time = LocalTime.parse(time, TIME_FORMATTER);
            this.desc = desc;
            LocalDateTime appointmentDateTime = LocalDateTime.of(this.date, this.time);
            if (appointmentDateTime.isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException(MESSAGE_PAST_APPOINTMENT);
            }
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    public Appointment(String date, String time) {
        this(date, time, null);
    }

    /**
     * Returns true if date and time are in valid format.
     */
    public static boolean isValidAppointment(Appointment appointment) {
        try {
            LocalDateTime appointmentDateTime =
                    LocalDateTime.of(appointment.date, appointment.time);
            LocalDate.parse(appointment.date.format(DATE_FORMATTER), DATE_FORMATTER);
            LocalTime.parse(appointment.time.format(TIME_FORMATTER), TIME_FORMATTER);
            return !appointmentDateTime.isBefore(LocalDateTime.now());
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder()
            .append(date.format(DATE_FORMATTER))
            .append(" ")
            .append(time.format(TIME_FORMATTER));

        if (desc != null) {
            builder.append(" ").append(desc);
        }

        return builder.toString();
    }

    /**
     * Returns appointment date
     * @return appointment date string
     */
    public String getDate() {
        return date.format(DATE_FORMATTER);
    }

    /**
     * Returns appointment time
     * @return appointment time string
     */
    public String getTime() {
        return time.format(TIME_FORMATTER);
    }

    public Optional<Note> getNote() {
        return Optional.ofNullable(desc);
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof Appointment)) {
            return false;
        }

        Appointment otherAppointment = (Appointment) other;
        return date.equals(otherAppointment.date)
            && time.equals(otherAppointment.time)
            && Objects.equals(desc, otherAppointment.desc);
    }

    @Override
    public int hashCode() {
        return Objects.hash(date, time, desc);
    }
}
