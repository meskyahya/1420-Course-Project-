package com.campusbooking;

import com.campusbooking.persistence.*;
import com.campusbooking.models.*;
import com.campusbooking.controllers.*;
import com.campusbooking.managers.*;
import com.campusbooking.views.*;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.Stage;
import java.util.List;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) {

        // Load Data from CSV Files
        List<User> users = DataLoader.loadUsers("users.csv");
        List<Event> loadedEvents = DataLoader.loadEvents("events.csv");
        List<Booking> loadedBookings = DataLoader.loadBookings("bookings.csv");

        // Managers
        EventManager eventManager = new EventManager(loadedEvents);
        BookingManager bookingManager = new BookingManager(loadedBookings);
        List<Event> events = eventManager.getAllEvents();
        List<Booking> bookings = bookingManager.getAllBookings();

        for (Booking b : bookings) {
    try {
        Event e = eventManager.getEvent(b.getEventId());
        if (e != null && !b.isCancelled()) {
            e.addBooking(b);
        }
    } catch (Exception e) {
        System.out.println("Could not restore booking: " + b.getBookingId());
    }
}

            // User Management tab
            UserController userController = new UserController(users);
            Tab userTab = new Tab("User Management");
            userTab.setContent(userController.getView().getView());


            // Event Management tab
            EventController eventController = new EventController(eventManager);
            Tab eventTab = new Tab("Event Management");
            eventTab.setContent(eventController.getView());

            // Booking Management tab
            BookingView bookingView = new BookingView();
            BookingGUIController bookingController = new BookingGUIController(
                    bookingManager,
                    bookingView,
                    userController.getUsers(),
                    eventManager
            );
            Tab bookingTab = new Tab("Booking Management");
            bookingTab.setContent(bookingController.getView());


            // Waitlist Management tab
            WaitlistController waitlistController = new WaitlistController();
            waitlistController.setEvents(eventManager.getAllEvents());
            Tab waitlistTab = new Tab("Waitlist Management");
            waitlistTab.setContent(waitlistController.getView());

            // Updates
            userController.setOnUserChanged(() ->
                    bookingController.refreshUserDropdown(userController.getUsers())
            );
            eventController.setOnEventsChanged(() -> {
                bookingController.refreshEventDropdown(); // no argument now
                waitlistController.setEvents(eventManager.getAllEvents());// also refresh waitlist
                bookingController.refreshBookings();
            });
            bookingController.setOnBookingChanged(() ->
                    eventController.refreshEventList()
            );

            // TabPane setup
            TabPane tabPane = new TabPane();
            tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
            tabPane.getTabs().addAll(userTab, eventTab, bookingTab, waitlistTab);


            // BorderPane layout
            BorderPane root = new BorderPane();
            root.setCenter(tabPane);

            // Background gradient (UofG Red and Gold)
            root.setStyle("-fx-background-color: linear-gradient(to bottom right, #E51937, #FFC429);");

            // Image right side
            ImageView iconView = new ImageView(
                    new Image(getClass().getResource("/icon.png").toExternalForm()));
            iconView.setFitWidth(120);
            iconView.setPreserveRatio(true);
            VBox rightBox = new VBox(iconView);
            rightBox.setAlignment(Pos.TOP_RIGHT);
            rightBox.setPadding(new Insets(15));
            root.setRight(rightBox);

            // Scene
            Scene scene = new Scene(root, 850, 600);
            scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm());
            primaryStage.setTitle("Campus Event Booking System");
            primaryStage.setScene(scene);
            primaryStage.show();

            // Save Data on Close
            primaryStage.setOnCloseRequest(event -> {
                DataSaver.saveUsers(users, "users.csv");
                DataSaver.saveEvents(eventManager.getAllEvents(), "events.csv");
                DataSaver.saveBookings(bookingManager.getAllBookings(), "bookings.csv");
            });

    }
