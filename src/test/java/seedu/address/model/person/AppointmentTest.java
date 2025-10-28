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

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Appointment(null, "14:30"));
    }

    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> new Appointment("31-12-2025", null));
    }

    @Test
    public void isValidAppointment_validFutureAppointment_returnsTrue() {
        String futureDate = LocalDate.now().plusYears(1).format(DATE_FORMATTER);
        String time = "14:30";
        Appointment appointment = new Appointment(futureDate, time);

        assertTrue(Appointment.isValidAppointment(appointment));
    }

    @Test
    public void equals_differentTypes_returnsFalse() {
        String futureDate = LocalDate.now().plusYears(1).format(DATE_FORMATTER);
        String time = "14:30";
        Appointment appointment = new Appointment(futureDate, time);

        assertEquals(false, appointment.equals("not an appointment"));
        assertEquals(false, appointment.equals(null));
    }

    @Test
    public void equals_sameObject_returnsTrue() {
        String futureDate = LocalDate.now().plusYears(1).format(DATE_FORMATTER);
        String time = "14:30";
        Appointment appointment = new Appointment(futureDate, time);

        assertEquals(appointment, appointment);
    }

    @Test
    public void hashCode_sameAppointments_sameHashCode() {
        String futureDate = LocalDate.now().plusYears(1).format(DATE_FORMATTER);
        String time = "14:30";

        Appointment appointment1 = new Appointment(futureDate, time);
        Appointment appointment2 = new Appointment(futureDate, time);

        assertEquals(appointment1.hashCode(), appointment2.hashCode());
    }

    @Test
    public void compareTo_earlierDate_returnsNegative() {
        Appointment earlier = new Appointment("15-11-2099", "10:00");
        Appointment later = new Appointment("20-12-2099", "10:00");

        assertTrue(earlier.compareTo(later) < 0);
    }

    @Test
    public void compareTo_laterDate_returnsPositive() {
        Appointment earlier = new Appointment("15-11-2099", "10:00");
        Appointment later = new Appointment("20-12-2099", "10:00");

        assertTrue(later.compareTo(earlier) > 0);
    }

    @Test
    public void compareTo_sameDateEarlierTime_returnsNegative() {
        Appointment earlier = new Appointment("15-11-2099", "09:00");
        Appointment later = new Appointment("15-11-2099", "15:00");

        assertTrue(earlier.compareTo(later) < 0);
    }

    @Test
    public void compareTo_sameDateLaterTime_returnsPositive() {
        Appointment earlier = new Appointment("15-11-2099", "09:00");
        Appointment later = new Appointment("15-11-2099", "15:00");

        assertTrue(later.compareTo(earlier) > 0);
    }

    @Test
    public void compareTo_sameDateAndTime_returnsZero() {
        Appointment appt1 = new Appointment("15-11-2099", "10:00");
        Appointment appt2 = new Appointment("15-11-2099", "10:00");

        assertEquals(0, appt1.compareTo(appt2));
    }

    @Test
    public void compareTo_withNotes_comparesByDateTimeOnly() {
        Note note1 = new Note("First appointment");
        Note note2 = new Note("Second appointment");

        Appointment earlier = new Appointment("15-11-2099", "10:00", note1);
        Appointment later = new Appointment("20-12-2099", "10:00", note2);

        assertTrue(earlier.compareTo(later) < 0);
    }
}
