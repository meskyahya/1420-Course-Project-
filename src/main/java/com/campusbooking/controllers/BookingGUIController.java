/**
 * BookingGUIController
 *
 * This class acts as the controller for the booking interface in the application.
 * It connects the user interface (BookingView) with the backend logic handled by
 * the BookingManager and EventManager.
 *
 * Responsibilities:
 * - Handles user interactions such as booking and cancelling events
 * - Retrieves selected data from the GUI (users, events, bookings)
 * - Uses manager classes to perform operations like creating or cancelling bookings
 * - Updates the UI components (dropdowns, lists, status messages) after each action
 * - Ensures the interface stays synchronized with the underlying data
 *
 * Key Features:
 * - Validates user input before performing actions
 * - Displays error messages using alerts when needed
 * - Dynamically refreshes bookings and event capacity after changes
 * - Uses a callback (onBookingChanged) to notify other parts of the application
 *
 * Overall, this class serves as the middle layer between the GUI and the application
 * logic, following a structure similar to the MVC (Model-View-Controller) design pattern.
 */


package com.campusbooking.controllers; //defines the package this class belongs to 

import com.campusbooking.managers.*; //imports all manager classses (BookingManager, EventManager, ect)
import com.campusbooking.models.*; // Imports all model classes (User, Event, Booking, etc.)
import com.campusbooking.views.*; //Imports all view classes (GUI components)
import com.campusbooking.views.BookingView; // Import for popup alerts
import javafx.scene.control.Alert // Import for popup alerts
import javafx.scene.control.ComboBox; // Import for dropdown menus

import java.util.ArrayList; //import for dynamic array list
import java.util.List; //import for list interface

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

        this.bookingManager = bookingManager; //assign booking manager
        this.view = view; //assign view
        this.users = users; //assign user list
        this.eventManager = eventManager; //assign event manager

        initialize();
    }
    //sets up button actionsn adn loads initial data
    private void initialize() { 
        //button actions. 
        //When "book" is clicked --> run handleBook()
        view.getBookButton().setOnAction(e -> handleBook());
        //When "cancel" is clicked --> run handleCancel()
        view.getCancelButton().setOnAction(e -> handleCancel());
        
        //Load UI data (the dropdowns and booking list)
        refreshUserDropdown(users);
        refreshEventDropdown();
        refreshBookings();
    }
    //method that creates a list of display strings like userID - name. 
    private void loadUsers() { //loading user into the view
        List<String> userStrings = new ArrayList<>();

        for (User u : users) { //loop through each user 
            userStrings.add(u.getuserID() + " - " + u.getName()); //format and add to list
        }
        //sends the list to view
        view.setUsers(userStrings);
    }
    //method for events
    private void loadEvents() {
        List<String> eventStrings = new ArrayList<>(); //create empty list
        //loops through all events from eventManager and creates a display text like eventID - title.
        for (Event e : eventManager.getAllEvents()) {
            eventStrings.add(e.getEventId() + " - " + e.getTitle()); //format and add
        }
    //pass the list to the view
        view.setEvents(eventStrings); //send to view
    }
    //This method runs when the user clicks Book. 
    private void handleBook() { //handles booking an event 
        try {//try blcok to catch errors
            
            //get selected values for UI:
            String userIdStr = view.getSelectedUserId();
            String eventId = view.getSelectedEventId();
            
            //validation -> make sure something is selected
            if (userIdStr == null || eventId == null) {
                showError("Select user and event.");
                return;
            }
            //finding objects using ID's:
            String userId = userIdStr;
            User user = findUser(userId);
            Event event = findEvent(eventId);
            
            //Make sure user and event are found
            if (user == null || event == null){
                showError("No Booking or User Found");
                return;
            }
            
            //create booking using manager*
            Booking booking = bookingManager.bookEvent(user, event);

            //Update Status text inn UI:
            view.updateStatus("Booking created: " + booking.getBookingId(),
                    "-fx-text-fill: green;");
            //refresh UI
            refreshBookings();
            refreshEventDropdown();
            
            //notify other parts of app 
            if (onBookingChanged != null) onBookingChanged.run(); // update event list in event tab
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }
    
    //runs when user clicks cancel. 
    private void handleCancel() { //handles cancelling a booking
        try {
            //debug prints (for testing)
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
            // Example string:
            // "Booking ID: B001 | User: U1 | Event: E1 | Status: CONFIRMED"
            // split(" ") → ["Booking", "ID:", "B001", ...]
            String bookingId = selected.split(" ")[2];
            
            //find event that this booking belonngs to
            Event event = findEventFromBooking(bookingId);

            if (event == null) {
                showError("Event not Found for Booking"); //safety for no booking found
                return;
            }
            //cancel booking
            bookingManager.cancelBooking(event, bookingId);
            //update UI
            view.updateStatus("Booking cancelled.",
                    "-fx-text-fill: green;");

            refreshBookings();
            refreshEventDropdown();

            //notify other parts of app
            if (onBookingChanged != null) onBookingChanged.run(); // update event list in event tab
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }
    //refreshes booking list in UI
    public void refreshBookings() {
        List<Booking> bookings = bookingManager.getAllBookings();

        List<String> bookingStrings = new ArrayList<>();
        //convert each booking into a display string
        for (Booking b : bookings) {
            bookingStrings.add(
                    "Booking ID: " + b.getBookingId() +
                            " | User: " + b.getUserId() +
                            " | Event: " + b.getEventId() +
                            " | Status: " + b.getStatus()
            );
        }
        //update list in view
        view.updateBookingsList(bookingStrings);
    }
    //refresh user dropdown
    public void refreshUserDropdown(List<User> users) {
        this.users = users;
        
        ComboBox<String> userCombo = view.getUserCombo(); // get reference from view
        userCombo.getItems().clear(); // clear old items
        //add users to dropdown
        for (User u : users) {
            String display = u.getuserID() + " - " + u.getName();
            userCombo.getItems().add(display);
        }

        //select user first by defualt
        if (!users.isEmpty()) {
            userCombo.getSelectionModel().select(0);
        }
    }
    //refresh event dropdownn
    public void refreshEventDropdown() {
        List<Event> events = eventManager.getAllEvents();
        ComboBox<String> eventCombo = view.getEventCombo();
        eventCombo.getItems().clear();

        //add events with capacity info
        for (Event e : events) {
            String display = e.getEventId() + " - " + e.getTitle() +
                    " (" + e.getConfirmedBookings().size() +
                    "/" + e.getCapacity() + ")";
            eventCombo.getItems().add(display);
        }
        //select first evennt by defult
        if (!events.isEmpty()) {
            eventCombo.getSelectionModel().select(0);
        }
    }

    //sets callback function for when booking change
    public void setOnBookingChanged(Runnable callback) {
        this.onBookingChanged = callback;
    }



    //Finds a user by ID
    private User findUser(String userId) {
    for (User u : users) {
        if (u.getuserID().equals(userId)) {
            return u;
        }
    }
    return null;
}
    //Finds event by ID using EventMManager
    private Event findEvent(String eventId) {
        return eventManager.getEvent(eventId);
    }
    //Searches confirmed booking and waitlist. Finds which event a booking belongs to
    private Event findEventFromBooking(String bookingId) {
        for (Event e : eventManager.getAllEvents()) {
            //check confirmed bookings
            for (Booking b : e.getConfirmedBookings()) {
                if (b.getBookingId().equals(bookingId)) {
                    return e;
                }
            }
            //check waitlist
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
    //retunr this view
    public BookingView getView() {
        return view;
    }
}
