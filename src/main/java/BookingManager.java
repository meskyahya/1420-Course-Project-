import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BookingManager {

    private ArrayList<Booking> allBookings;
    private int bookingCounter;

    public BookingManager() {
        this.allBookings = new ArrayList<>();
        this.bookingCounter = 1;
    }

    public Booking bookEvent(User user, Event event){
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (event == null) throw new IllegalArgumentException("Event cannot be null");

        if (!event.isActive()){
            throw new IllegalArgumentException("Cannot book a cancelled event. ");
        }
        int userId = user.getuserID();
        String eventId = event.getEventId();

        if (hasActiveBooking(userId, eventId)){
            throw new IllegalArgumentException("Duplicate booking blocked: user already booked this event. ");
        }
        int limit = getConfirmedLimit(user.getrole());
        int confirmed = countUserConfirmed(userId);

        if (confirmed >= limit){
            throw new IllegalArgumentException("Booking limit reached for role" + user.getrole() + "(max confirmed =" + limit + ").");
        }
        String bookingId = nextBookingId();
        Booking booking = new Booking(
                bookingId,
                String.valueOf(userId),
                eventId,
                LocalDateTime.now()
        );

        event.addBooking(booking);

        allBookings.add(booking);

        return booking;
    }

    public void cancelBooking(Event event, String bookingId){
        if (event == null) throw new IllegalArgumentException("Event cannot be null");

        Booking booking = findBookingById(bookingId);
        if (booking == null) throw new IllegalArgumentException("Booking not found: " + bookingId);


        event.cancelBooking(booking);
    }

    public List<Booking> getBookingsForUser(int userId){
        ArrayList<Booking> bookings = new ArrayList<>();
        String uid = String.valueOf(userId);

        for (int i = 0; i < allBookings.size(); i++) {
            Booking b = allBookings.get(i);

            if (b.getUserId().equals(uid))
                bookings.add(b);
            }
        }
        return bookings;
    }

    public List<Booking> getConfirmedRoster(Event event){
        return event.getConfirmedBookings();
    }

    public List<Booking> getWaitlistRoster(Event event){
        return event.getWaitlist();
    }

    private String nextBookingId(){
        String n = String.valueOf(bookingCounter);
        while (n.length() <4) n = "0" + n;
        bookingCounter++;
        return "B" + n;
    }

    private Booking findBookingById(String bookingId){
        for (int i = 0; i < allBookings.size(); i++) {
            if (allBookings.get(i).getBookingId().equals(bookingId)){
                return allBookings.get(i);
            }
        }
        return null;
    }
    private boolean hasActiveBooking(int userId, String eventId){
        String uid = String.valueOf(userId);

        for (int i  = 0; i < allBookings.size(); i++) {
            Booking b = allBookings.get(i);

            boolean sameUser = b.getUserId().equals(uid);
            boolean sameEvent = b.getEventId().equals(eventId);
            boolean active = !b.isCancelled();

            if (sameUser && sameEvent && active) return true;
        }
        return false;
    }

    private int countUserConfirmed(int userId){
        String uid = String.valueOf(userId);
        int count = 0;

        for (int i = 0; i < allBookings.size(); i++) {
            Booking b = allBookings.get(i);
            if (b.getUserId().equals(uid) && b.isConfirmed()){
                count++;
            }
        }
        return count;
    }

    private int getConfirmedLimit(String role){
        if (role == null) return 1;

        String r = role.trim().toLowerCase();
        if (r.equals("student")) return 3;
        if (r.equals("staff")) return 5;
        return 1;
    }

    public ArrayList<Booking> getAllBookings() {
        return allBookings;
    }

}
