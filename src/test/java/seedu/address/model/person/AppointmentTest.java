package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.jupiter.api.Test;


public class AppointmentTest {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @Test
    public void constructor_validDateTime_success() {
        String futureDate = LocalDate.now().plusYears(1).format(DATE_FORMATTER);
        String time = "10:00";
        Appointment appointment = new Appointment(futureDate, time);
        assertEquals(futureDate + " " + time, appointment.toString());
    }

    @Test
    public void constructor_invalidFormat_throwsIllegalArgumentException() {
        assertThrows(IllegalArgumentException.class, () -> new Appointment("2023-12-12", "1000"));
    }

    @Test
    public void constructor_pastDate_throwsIllegalArgumentException() {
        String pastDate = LocalDate.now().minusDays(1).format(DATE_FORMATTER);
        String time = "10:00";
        assertThrows(IllegalArgumentException.class, () -> new Appointment(pastDate, time));
    }

    @Test
    public void equals_sameDetails_returnsTrue() {
        String futureDate = LocalDate.now().plusYears(1).format(DATE_FORMATTER);
        String time = "12:00";
        Appointment first = new Appointment(futureDate, time);
        Appointment second = new Appointment(futureDate, time);
        assertTrue(first.equals(second));
    }
}
