package com.campusbooking.managers;

import com.campusbooking.controllers.*;
import com.campusbooking.models.*;
import com.campusbooking.views.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

//create BookingManager class
public class BookingManager {

    //master list of all bookings in system
    private ArrayList<Booking> allBookings;
    //creates booking IDs
    private int bookingCounter;

    //constructor
    //if bookings are available, use them. otherwise start with empty list
    public BookingManager(List<Booking> bookingList) {
        if (bookingList != null) {
            this.allBookings = new ArrayList<>(bookingList);
        } else {
            this.allBookings = new ArrayList<>();
        }
        //start counter after current number of bookings
        this.bookingCounter = allBookings.size() + 1;
    }

    //creates a new booking for a user and event
    public Booking bookEvent(User user, Event event){
        //safety checks to prevent crashes
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (event == null) throw new IllegalArgumentException("Event cannot be null");
        //prevents booking if event was cancelled
        if (!event.isActive()){
            throw new IllegalArgumentException("Cannot book a cancelled event. ");
        }
        //get user ID and event ID
        String userId = user.getuserID();
        String eventId = event.getEventId();

        //stop the same user from booking the same event twice
        if (hasActiveBooking(userId, eventId)){
            throw new IllegalArgumentException("Duplicate booking blocked: user already booked this event. ");
        }
        //get max number of bookings allowed for this role
        int limit = getConfirmedLimit(user.getrole());
        //get how many booking user already has
        int confirmed = countUserConfirmed(userId);

        //if they are already at the limit, block booking
        if (confirmed >= limit){
            throw new IllegalArgumentException("Booking limit reached for role" + user.getrole() + "(max confirmed =" + limit + ").");
        }
        //create unique booking ID
        String bookingId = nextBookingId();
        //create booking object
        Booking booking = new Booking(
        bookingId,
        userId,
        eventId,
        LocalDateTime.now()
);
        //decide if booking is confirmed or waitlisted based on seats available
        event.addBooking(booking);
        //add booking to all bookings list
        allBookings.add(booking);
        //return the booking so controller can display it
        return booking;
    }

    //create a cancel booking option
    public void cancelBooking(Event event, String bookingId){
        //prevent crash
        if (event == null) throw new IllegalArgumentException("Event cannot be null");
        //find booking in master list
        Booking booking = findBookingById(bookingId);
        //show error if bookingisnt found
        if (booking == null) throw new IllegalArgumentException("Booking not found: " + bookingId);

        //refers to event logic for logic
        event.cancelBooking(booking);
    }
    //return all bookings for user
    public List<Booking> getBookingsForUser(int userId){
        //make new list for this users bookings
        ArrayList<Booking> bookings = new ArrayList<>();
        String uid = String.valueOf(userId);

        //go through every booking in the system
        for (int i = 0; i < allBookings.size(); i++) {
            Booking b = allBookings.get(i);
            //if the booking belongs to user add it to list
            if (b.getUserId().equals(uid)){ //change getBookingId to getUserId
                bookings.add(b);
            }
        }
        //return users booking history
        return bookings;
    }
    //returns confirmed roster for event
    public List<Booking> getConfirmedRoster(Event event){
        return event.getConfirmedBookings();
    }
    //returns waitlist roster for event
    public List<Booking> getWaitlistRoster(Event event){
        return event.getWaitlist();
    }

    //creates next booking ID
    private String nextBookingId(){
        String n = String.valueOf(bookingCounter);
        //ensure number is 4 digits
        while (n.length() <4) n = "0" + n;
        bookingCounter++;
        return "B" + n;
    }
    //helps search for booking using booking ID
    private Booking findBookingById(String bookingId){
        for (int i = 0; i < allBookings.size(); i++) {
            if (allBookings.get(i).getBookingId().equals(bookingId)){
                return allBookings.get(i);
            }
        }
        return null;
    }
    //check if user has active booking for event
    private boolean hasActiveBooking(String userId, String eventId){

        for (int i  = 0; i < allBookings.size(); i++) {
            Booking b = allBookings.get(i);

            boolean sameUser = b.getUserId().equals(userId);
            boolean sameEvent = b.getEventId().equals(eventId);
            boolean active = !b.isCancelled();
            //if all 3 are true then its a duplicate active booking
            if (sameUser && sameEvent && active) return true;
        }
        return false;
    }

    //count how many active bookings user has
    private int countUserConfirmed(String userId){
    int count = 0;

    for (int i = 0; i < allBookings.size(); i++) {
        Booking b = allBookings.get(i);
        if (b.getUserId().equals(userId) && b.isConfirmed()){
            count++;
        }
    }
    return count;
}
    //decides booking limit based on user role
    private int getConfirmedLimit(String role){
        if (role == null) return 1;

        String r = role.trim().toLowerCase();
        if (r.equals("student")) return 3;
        if (r.equals("staff")) return 5; //change to staff
        if (r.equals("guest")) return 1; //add quest
        return 1;
    }

    public ArrayList<Booking> getAllBookings() {
        return allBookings;
    }

}
