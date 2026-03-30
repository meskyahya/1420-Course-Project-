import com.campusbooking.models.*;
import com.campusbooking.managers.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class BookingSystemTest {

    private EventManager eventManager;
    private BookingManager bookingManager;
    private User student;
    private Event workshop;

    // This runs before each test to set up fresh objects
    @BeforeEach
    public void setUp() {
        eventManager = new EventManager(new ArrayList<>());
        bookingManager = new BookingManager(new ArrayList<>());

        student = new Student("Alice", "alice@uoguelph.ca", "U001");

        workshop = new Workshop(
            "E101",
            "Intro to Git",
            LocalDateTime.of(2027, 6, 1, 10, 0),
            "Library 101",
            2,
            "Version Control"
        );

        eventManager.addEvent(workshop);
    }

    // Test 1: Booking when space is available should be Confirmed
    @Test
    public void testBookingUnderCapacity() {
        Booking booking = bookingManager.bookEvent(student, workshop);
        String status = booking.getStatus();
        assertEquals("Confirmed", status);
    }

    // Test 2: Booking when event is full should be Waitlisted
    @Test
    public void testBookingWhenFull() {
        User student2 = new Student("Bob", "bob@uoguelph.ca", "U002");
        User student3 = new Student("Carol", "carol@uoguelph.ca", "U003");

        // Fill up the 2 spots
        bookingManager.bookEvent(student, workshop);
        bookingManager.bookEvent(student2, workshop);

        // This one should go to waitlist
        Booking booking = bookingManager.bookEvent(student3, workshop);
        String status = booking.getStatus();
        assertEquals("Waitlisted", status);
    }

    // Test 3: Cancelling a confirmed booking should promote the first waitlisted user
    @Test
    public void testCancelPromotesWaitlist() {
        User student2 = new Student("Bob", "bob@uoguelph.ca", "U002");
        User student3 = new Student("Carol", "carol@uoguelph.ca", "U003");

        // Fill the event
        Booking firstBooking = bookingManager.bookEvent(student, workshop);
        bookingManager.bookEvent(student2, workshop);

        // Carol gets waitlisted
        Booking carolBooking = bookingManager.bookEvent(student3, workshop);
        assertEquals("Waitlisted", carolBooking.getStatus());

        // Cancel Alice's booking
        bookingManager.cancelBooking(workshop, firstBooking.getBookingId());

        // Carol should now be Confirmed
        assertEquals("Confirmed", carolBooking.getStatus());
    }

    // Test 4: Booking the same event twice should throw an error
    @Test
    public void testDuplicateBookingIsBlocked() {
        bookingManager.bookEvent(student, workshop);

        boolean exceptionThrown = false;

        try {
            bookingManager.bookEvent(student, workshop);
        } catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }
}
