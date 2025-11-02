package seedu.address.model.person;

import static java.util.Objects.requireNonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;


/**
 * Represents a Person's appointment in the address book.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidAppointment(String)}
 */
public class Appointment implements Comparable<Appointment> {

    public static final String MESSAGE_CONSTRAINTS = "Date and time should be in the format DD-MM-YYYY HH:MM";
    public static final String MESSAGE_INVALID_DATE_TIME = "The specified date or time does not exist.";
    public static final String MESSAGE_PAST_APPOINTMENT = "Appointment must be set in the future.";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu")
        .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm")
        .withResolverStyle(ResolverStyle.STRICT);
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-uuuu HH:mm")
        .withResolverStyle(ResolverStyle.STRICT);
    private static final Pattern DATE_TIME_PATTERN = Pattern.compile("\\d{2}-\\d{2}-\\d{4} \\d{2}:\\d{2}");

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

        LocalDateTime parsedDateTime = parseDateTime(date, time);
        this.date = parsedDateTime.toLocalDate();
        this.time = parsedDateTime.toLocalTime();
        this.desc = desc;
        if (parsedDateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException(MESSAGE_PAST_APPOINTMENT);
        }
    }

    private static LocalDateTime parseDateTime(String date, String time) {
        String candidate = date + " " + time;
        if (!DATE_TIME_PATTERN.matcher(candidate).matches()) {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }

        try {
            return LocalDateTime.parse(candidate, DATE_TIME_FORMATTER);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(MESSAGE_INVALID_DATE_TIME);
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

    public LocalDateTime getDateTime() {
        return LocalDateTime.of(date, time);
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

    /**
     * Compares this appointment with another appointment for chronological ordering.
     * Appointments are ordered by date and time combined (earliest first).
     *
     * @param other the appointment to compare to
     * @return negative if this appointment is earlier, positive if later, zero if equal
     */
    @Override
    public int compareTo(Appointment other) {
        LocalDateTime thisDateTime = LocalDateTime.of(this.date, this.time);
        LocalDateTime otherDateTime = LocalDateTime.of(other.date, other.time);
        return thisDateTime.compareTo(otherDateTime);
    }
}
