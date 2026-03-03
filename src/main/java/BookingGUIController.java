import javafx.scene.control.Alert;

import java.util.ArrayList;
import java.util.List;

public class BookingGUIController {

    private BookingManager bookingManager;
    private BookingView view;
    private List<User> users;
    private List<Event> events;

    public BookingGUIController(BookingManager bookingManager,
                                BookingView view,
                                List<User> users,
                                List<Event> events) {

        this.bookingManager = bookingManager;
        this.view = view;
        this.users = users;
        this.events = events;

        initialize();
    }

    private void initialize() {

        loadUsers();
        loadEvents();

        view.getBookButton().setOnAction(e -> handleBook());
        view.getCancelButton().setOnAction(e -> handleCancel());
    }

    private void loadUsers() {
        List<String> userStrings = new ArrayList<>();

        for (User u : users) {
            userStrings.add(u.getuserID() + " - " + u.getname());
        }

        view.setUsers(userStrings);
    }

    private void loadEvents() {
        List<String> eventStrings = new ArrayList<>();

        for (Event e : events) {
            eventStrings.add(e.getEventId() + " - " + e.getTitle());
        }

        view.setEvents(eventStrings);
    }

    private void handleBook() {
        try {
            String userIdStr = view.getSelectedUserId();
            String eventId = view.getSelectedEventId();

            if (userIdStr == null || eventId == null) {
                showError("Select user and event.");
                return;
            }

            int userId = Integer.parseInt(userIdStr);

            User user = findUser(userId);
            Event event = findEvent(eventId);

            Booking booking = bookingManager.bookEvent(user, event);

            view.updateStatus("Booking created: " + booking.getBookingId(),
                    "-fx-text-fill: green;");

            refreshBookings(userId);

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleCancel() {
        try {
            String userIdStr = view.getSelectedUserId();
            String selected = view.getBookingsList()
                    .getSelectionModel()
                    .getSelectedItem();

            if (userIdStr == null || selected == null) {
                showError("Select user and booking to cancel.");
                return;
            }

            int userId = Integer.parseInt(userIdStr);

            // Extract bookingId from string format
            String bookingId = selected.split(" ")[2];

            Event event = findEventFromBooking(bookingId);

            bookingManager.cancelBooking(event, bookingId);

            view.updateStatus("Booking cancelled.",
                    "-fx-text-fill: green;");

            refreshBookings(userId);

        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void refreshBookings(int userId) {

        List<Booking> bookings =
                bookingManager.getBookingsForUser(userId);

        List<String> bookingStrings = new ArrayList<>();

        for (Booking b : bookings) {
            bookingStrings.add("Booking ID: " + b.getBookingId() +
                    " | Event: " + b.getEventId() +
                    " | Status: " + b.getStatus());
        }

        view.updateBookingsList(bookingStrings);
    }

    private User findUser(int userId) {
        for (User u : users) {
            if (u.getuserID() == userId) {
                return u;
            }
        }
        return null;
    }

    private Event findEvent(String eventId) {
        for (Event e : events) {
            if (e.getEventId().equals(eventId)) {
                return e;
            }
        }
        return null;
    }

    private Event findEventFromBooking(String bookingId) {
        for (Event e : events) {
            for (Booking b : e.getConfirmedBookings()) {
                if (b.getBookingId().equals(bookingId)) {
                    return e;
                }
            }
            for (Booking b : e.getWaitlist()) {
                if (b.getBookingId().equals(bookingId)) {
                    return e;
                }
            }
        }
        return null;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
