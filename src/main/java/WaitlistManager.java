import java.util.List;

public class WaitlistManager {

    //Get the ordered waitlist for an event
    public List<Booking> getWaitlist(Event event) {
        return event.getWaitlist();
    }

    //Get the confirmed bookings list for an event.

    public List<Booking> getConfirmed(Event event) {
        return event.getConfirmedBookings();
    }

    //Add a booking to the event 
    public String addBooking(Event event, Booking booking) {
        event.addBooking(booking);
        return booking.getStatus();
    }

    //Cancel a booking. If it was confirmed, automatically promotes the first waitlisted user (handled inside Event.cancelBooking). Returns the promoted booking if one was promoted, or null.

    public Booking cancelBooking(Event event, Booking booking) {
        List<Booking> beforeCancel = event.getWaitlist();
        String firstInLineId = beforeCancel.isEmpty()
                ? null
                : beforeCancel.get(0).getBookingId();

        event.cancelBooking(booking);

        if (firstInLineId != null) {
            List<Booking> confirmed = event.getConfirmedBookings();
            for (Booking b : confirmed) {
                if (b.getBookingId().equals(firstInLineId)) {
                    return b; // this booking was promoted
                }
            }
        }
        return null;
    }

    //Get the waitlist position of a user for an event (1-based). Returns -1 if the user is not on the waitlist.
    public int getPosition(Event event, String userId) {
        List<Booking> waitlist = event.getWaitlist();
        for (int i = 0; i < waitlist.size(); i++) {
            if (waitlist.get(i).getUserId().equals(userId)) {
                return i + 1;
            }
        }
        return -1;
    }

    //Check if a user is currently on the waitlist for an event.
    public boolean isWaitlisted(Event event, String userId) {
        return getPosition(event, userId) != -1;
    }

    //Return how many users are on the waitlist for an event.
    public int getWaitlistSize(Event event) {
        return event.getWaitlist().size();
    }
}
