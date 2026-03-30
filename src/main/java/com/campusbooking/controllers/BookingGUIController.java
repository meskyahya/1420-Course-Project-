package com.campusbooking.controllers;

import com.campusbooking.managers.*;
import com.campusbooking.models.*;
import com.campusbooking.views.*;
import com.campusbooking.views.BookingView;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.util.ArrayList;
import java.util.List;

//This class handles user actions like booking and cancelling events, and updates the UI accordingly.

public class BookingGUIController {

    private BookingManager bookingManager; //handles booking actions like create or cancel
    private BookingView view; //the BookingView object which is the GUI screen
    private List<User> users; //the list of users avaliable for booking
    private EventManager eventManager; //gives access to events and event data
    private Runnable onBookingChanged; //a callback so other parts of the program can refresh after a booking changes
    
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
        //button actions 
        view.getBookButton().setOnAction(e -> handleBook());
        view.getCancelButton().setOnAction(e -> handleCancel());
        //Load UI data
        refreshUserDropdown(users);
        refreshEventDropdown();
        refreshBookings();
    }
    //method that creates a list of display strings like userID - name. 
    private void loadUsers() {
        List<String> userStrings = new ArrayList<>();

        for (User u : users) {
            userStrings.add(u.getuserID() + " - " + u.getName());
        }
        //sends the list to view
        view.setUsers(userStrings);
    }
    //method for events
    private void loadEvents() {
        List<String> eventStrings = new ArrayList<>(); 
        //loops through all events from eventManager and creates a display text like eventID - title.
        for (Event e : eventManager.getAllEvents()) {
            eventStrings.add(e.getEventId() + " - " + e.getTitle());
        }
    //pass the list to the view
        view.setEvents(eventStrings);
    }
    //This method runs when the user clicks Book. 
    private void handleBook() {
        try {
            //get selected values:
            String userIdStr = view.getSelectedUserId();
            String eventId = view.getSelectedEventId();
            
            //validation
            if (userIdStr == null || eventId == null) {
                showError("Select user and event.");
                return;
            }

            String userId = userIdStr;
            //finding objects:
            User user = findUser(userId);
            Event event = findEvent(eventId);

            if (user == null || event == null){
                showError("No Booking or User Found");
                return;
            }
            
            //create booking*
            Booking booking = bookingManager.bookEvent(user, event);

            //Update UI:

            view.updateStatus("Booking created: " + booking.getBookingId(),
                    "-fx-text-fill: green;");
            refreshBookings();
            refreshEventDropdown();
            
            //notify other parts
            if (onBookingChanged != null) onBookingChanged.run(); // update event list in event tab


        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }
    
    //runs when user clicks cancel
    private void handleCancel() {
        try {
            System.out.println("Raw combo value: " + view.getUserCombo().getSelectionModel().getSelectedItem());
            System.out.println("Selected booking: " + view.getBookingsList().getSelectionModel().getSelectedItem());
            System.out.println("userIdStr: " + view.getSelectedUserId());
            System.out.println("selected: " + view.getBookingsList().getSelectionModel().getSelectedItem());
            //Get selected User
            String userIdStr = view.getSelectedUserId();

            //Get selected booking:
            String selected = view.getBookingsList()
                    .getSelectionModel()
                    .getSelectedItem();
            //Check if selected user and booking exist. print error
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
            String display = u.getuserID() + " - " + u.getName();
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
    //Searches confirmed booking and waitlist.  
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
    //Shows popup error using JavaFX Alert --> Error handling
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public BookingView getView() {
        return view;
    }
}
