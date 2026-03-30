package com.campusbooking;

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

    @Test
    public void testBookingUnderCapacity() {
        Booking booking = bookingManager.bookEvent(student, workshop);
        String status = booking.getStatus();
        assertEquals("Confirmed", status);
    }

    @Test
    public void testBookingWhenFull() {
        User student2 = new Student("Bob", "bob@uoguelph.ca", "U002");
        User student3 = new Student("Carol", "carol@uoguelph.ca", "U003");

        bookingManager.bookEvent(student, workshop);
        bookingManager.bookEvent(student2, workshop);

        Booking booking = bookingManager.bookEvent(student3, workshop);
        String status = booking.getStatus();
        assertEquals("Waitlisted", status);
    }

    @Test
    public void testCancelPromotesWaitlist() {
        User student2 = new Student("Bob", "bob@uoguelph.ca", "U002");
        User student3 = new Student("Carol", "carol@uoguelph.ca", "U003");

        Booking firstBooking = bookingManager.bookEvent(student, workshop);
        bookingManager.bookEvent(student2, workshop);

        Booking carolBooking = bookingManager.bookEvent(student3, workshop);
        assertEquals("Waitlisted", carolBooking.getStatus());

        bookingManager.cancelBooking(workshop, firstBooking.getBookingId());

        assertEquals("Confirmed", carolBooking.getStatus());
    }

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
