package seedu.address.model.person;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's appointment in the address book.
 * Guarantees: immutable; is valid as declared in
 * {@link #isValidAppointment(String)}
 */
public class Appointment {

    public static final String MESSAGE_CONSTRAINTS = "Date and time should be in the format dd-MM-yyyy HH:mm";

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    private static final DateTimeFormatter TIME_FORMATTER = DateTimeFormatter.ofPattern("HH:mm");

    private final LocalDate date;
    private final LocalTime time;

    /**
     * Constructs an {@code Appointment}.
     *
     * @param date A valid date.
     * @param time A valid time.
     */
    public Appointment(String date, String time) {
        requireNonNull(date);
        requireNonNull(time);
        if (isValidAppointment(date, time)) {
            this.date = LocalDate.parse(date, DATE_FORMATTER);
            this.time = LocalTime.parse(time, TIME_FORMATTER);
        } else {
            throw new IllegalArgumentException(MESSAGE_CONSTRAINTS);
        }
    }

    /**
     * Returns true if date and time are in valid format.
     */
    public static boolean isValidAppointment(String date, String time) {
        try {
            LocalDate.parse(date, DATE_FORMATTER);
            LocalTime.parse(time, TIME_FORMATTER);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    @Override
    public String toString() {
        return date.format(DATE_FORMATTER) + " " + time.format(TIME_FORMATTER);
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
        return date.equals(otherAppointment.date) && time.equals(otherAppointment.time);
    }

    @Override
    public int hashCode() {
        return date.hashCode() + time.hashCode();
    }

}
