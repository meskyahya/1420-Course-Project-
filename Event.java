import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public abstract class Event {

    private String eventId;
    private String title;
    private LocalDateTime dateTime;
    private String location;
    private int capacity;
    private EventStatus status; // ACTIVE or CANCELLED

    protected List<Booking> confirmedBookings;
    protected Queue<Booking> waitlist;

    public Event(String eventId, String title, LocalDateTime dateTime, String location, int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        this.eventId = eventId;
        this.title = title;
        this.dateTime = dateTime;
        this.location = location;
        this.capacity = capacity;
        this.status = EventStatus.ACTIVE;

        this.confirmedBookings = new ArrayList<>();
        this.waitlist = new LinkedList<>();
    }

    //---------------------------------------------------------------
    // Core Event Info
    //---------------------------------------------------------------

    public String getEventId() {
        return eventId;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getLocation() {
        return location;
    }

    public int getCapacity() {
        return capacity;
    }

    public EventStatus getStatus() {
        return status;
    }

    //----------------------------------------------------------------------------
    // Setters
    //----------------------------------------------------------------------------

    public void setTitle(String title) {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be empty.");
        }
        this.title = title;
    }

    public void setDateTime(LocalDateTime dateTime) {
        if (dateTime == null || dateTime.isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Date and Time must be in the future.");
        }
        this.dateTime = dateTime;
    }

    public void setLocation(String location) {
        if (location == null || location.isBlank()) {
            throw new IllegalArgumentException("Location cannot be empty.");
        }
        this.location = location;
    }

    public void setCapacity(int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("Capacity must be greater than zero");
        }
        if (capacity < confirmedBookings.size()) {
            throw new IllegalArgumentException("Capacity cannot be less than current confirmed bookings.");
        }
        this.capacity = capacity;
    }

    public void cancelEvent() {
        this.status = EventStatus.CANCELLED;

        // Cancel all confirmed bookings
        for (Booking b : confirmedBookings) {
            b.setStatus("Cancelled");
        }

        for (Booking b : waitlist) {
            b.setStatus("Cancelled");
        }

        confirmedBookings.clear();
        waitlist.clear();
    }

    public boolean isActive() {
        return status == EventStatus.ACTIVE;
    }

    //---------------------------------------------------------------------------
    // Booking Logic
    //---------------------------------------------------------------------------

    public boolean hasAvailableSeats() {
        return confirmedBookings.size() < capacity;
    }

    public void addBooking(Booking booking) {
        if (!isActive()) {
            throw new IllegalStateException("Cannot book cancelled event.");
        }

        if (hasAvailableSeats()) {
            booking.setStatus("Confirmed");
            confirmedBookings.add(booking);
        } else {
            booking.setStatus("Waitlisted");
            waitlist.add(booking);
        }
    }

    public void cancelBooking(Booking booking) {
        if (confirmedBookings.remove(booking)) {
            booking.setStatus("Cancelled");
            promoteWaitlist();
        } else if (waitlist.remove(booking)) {
            booking.setStatus("Cancelled");
        }
    }

    private void promoteWaitlist() {
        if (!waitlist.isEmpty()) {
            Booking promoted = waitlist.poll();
            promoted.setStatus("Confirmed");
            confirmedBookings.add(promoted);
        }
    }

    public List<Booking> getWaitlist() {
        return new ArrayList<>(waitlist);
    }

    public List<Booking> getConfirmedBookings() {
        return new ArrayList<>(confirmedBookings);
    }

    @Override
    public String toString() {
        return "Event{" +
                "eventId='" + eventId + '\'' +
                ", title='" + title + '\'' +
                ", dateTime=" + dateTime +
                ", location='" + location + '\'' +
                ", capacity=" + capacity +
                ", status=" + status +
                ", confirmedBookings=" + confirmedBookings.size() +
                ", waitlist=" + waitlist.size() +
                '}';
    }
}
