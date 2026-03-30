package com.campusbooking.controllers;

import com.campusbooking.managers.*;
import com.campusbooking.models.*;
import com.campusbooking.views.*;
import com.campusbooking.views.BookingView;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

public class BookingGUIController {

    private BookingManager bookingManager;
    private BookingView view;
    private List<User> users;
    private EventManager eventManager;
    private Runnable onBookingChanged;

    public BookingGUIController(BookingManager bookingManager,
                                BookingView view,
                                List<User> users,
                                EventManager eventManager) {

        this.bookingManager = bookingManager;
        this.view = view;
        this.users = users;
        this.eventManager = eventManager;

        initialize();
    }

    private void initialize() {
        view.getBookButton().setOnAction(e -> handleBook());
        view.getCancelButton().setOnAction(e -> handleCancel());
        refreshUserDropdown(users);
        refreshEventDropdown();
        refreshBookings();
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

        for (Event e : eventManager.getAllEvents()) {
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

            String userId = userIdStr;
            User user = findUser(userId);
            Event event = findEvent(eventId);

            if (user == null || event == null){
                showError("No Booking or User Found");
                return;
            }

            Booking booking = bookingManager.bookEvent(user, event);

            view.updateStatus("Booking created: " + booking.getBookingId(),
                    "-fx-text-fill: green;");

            refreshBookings();
            refreshEventDropdown();
            if (onBookingChanged != null) onBookingChanged.run(); // update event list in event tab


        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleCancel() {
        try {
            System.out.println("Raw combo value: " + view.getUserCombo().getSelectionModel().getSelectedItem());
            System.out.println("Selected booking: " + view.getBookingsList().getSelectionModel().getSelectedItem());
            System.out.println("userIdStr: " + view.getSelectedUserId());
            System.out.println("selected: " + view.getBookingsList().getSelectionModel().getSelectedItem());
            String userIdStr = view.getSelectedUserId();
            String selected = view.getBookingsList()
                    .getSelectionModel()
                    .getSelectedItem();

            if (userIdStr == null || selected == null) {
                showError("Select user and booking to cancel.");
                return;
            }

            String userId = userIdStr;
            
            // Extract bookingId from string format
            String bookingId = selected.split(" ")[2];

            Event event = findEventFromBooking(bookingId);

            if (event == null) {
                showError("Event not Found for Booking"); //safety for no booking found
                return;
            }

            bookingManager.cancelBooking(event, bookingId);

            view.updateStatus("Booking cancelled.",
                    "-fx-text-fill: green;");

            refreshBookings();
            refreshEventDropdown();
            if (onBookingChanged != null) onBookingChanged.run(); // update event list in event tab
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    public void refreshBookings() {
        List<Booking> bookings = bookingManager.getAllBookings();

        List<String> bookingStrings = new ArrayList<>();

        for (Booking b : bookings) {
            bookingStrings.add(
                    "Booking ID: " + b.getBookingId() +
                            " | User: " + b.getUserId() +
                            " | Event: " + b.getEventId() +
                            " | Status: " + b.getStatus()
            );
        }

        view.updateBookingsList(bookingStrings);
    }

    public void refreshUserDropdown(List<User> users) {
        this.users = users;
        ComboBox<String> userCombo = view.getUserCombo(); // get reference from view
        userCombo.getItems().clear(); // clear old items

        for (User u : users) {
            String display = u.getuserID() + " - " + u.getname();
            userCombo.getItems().add(display);
        }

        // Optionally select the first user by default
        if (!users.isEmpty()) {
            userCombo.getSelectionModel().select(0);
        }
    }

    public void refreshEventDropdown() {
        List<Event> events = eventManager.getAllEvents();
        ComboBox<String> eventCombo = view.getEventCombo();
        eventCombo.getItems().clear();
        for (Event e : events) {
            String display = e.getEventId() + " - " + e.getTitle() +
                    " (" + e.getConfirmedBookings().size() +
                    "/" + e.getCapacity() + ")";
            eventCombo.getItems().add(display);
        }
        if (!events.isEmpty()) {
            eventCombo.getSelectionModel().select(0);
        }
    }


    public void setOnBookingChanged(Runnable callback) {
        this.onBookingChanged = callback;
    }




    private User findUser(String userId) {
    for (User u : users) {
        if (u.getuserID().equals(userId)) {
            return u;
        }
    }
    return null;
}

    private Event findEvent(String eventId) {
        return eventManager.getEvent(eventId);
    }

    private Event findEventFromBooking(String bookingId) {
        for (Event e : eventManager.getAllEvents()) {
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
        alert.showAndWait();
    }

    public BookingView getView() {
        return view;
    }
}
