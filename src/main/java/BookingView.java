import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

public class BookingView extends VBox {

    private ComboBox<String> userCombo;
    private ComboBox<String> eventCombo;
    private ListView<String> bookingsList;
    private Button bookButton, cancelButton;
    private Label statusLabel;

    public BookingView() {
        buildUI();
    }

    private void buildUI() {
        setSpacing(12);
        setPadding(new Insets(20));

        // Title
        Label title = new Label("Booking Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        getChildren().add(title);

        // User selection
        Label userLabel = new Label("Select User:");
        userCombo = new ComboBox<>();
        userCombo.setPromptText("Choose a user...");
        userCombo.setPrefWidth(280);
        getChildren().addAll(userLabel, userCombo);

        // Event selection
        Label eventLabel = new Label("Select Event:");
        eventCombo = new ComboBox<>();
        eventCombo.setPromptText("Choose an event...");
        eventCombo.setPrefWidth(280);
        getChildren().addAll(eventLabel, eventCombo);

        // Buttons
        bookButton = new Button("Book Event");
        cancelButton = new Button("Cancel Selected Booking");
        HBox buttonRow = new HBox(10, bookButton, cancelButton);
        getChildren().add(buttonRow);

        // Bookings list
        Label listLabel = new Label("User's Bookings:");
        listLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        bookingsList = new ListView<>();
        bookingsList.setPrefHeight(200);
        getChildren().addAll(listLabel, bookingsList);

        // Status
        statusLabel = new Label("Select user and event to book.");
        statusLabel.setStyle("-fx-text-fill: grey;");
        getChildren().add(statusLabel);
    }

    // Getters for BookingController
    public ComboBox<String> getUserCombo() { return userCombo; }
    public ComboBox<String> getEventCombo() { return eventCombo; }
    public ListView<String> getBookingsList() { return bookingsList; }
    public Button getBookButton() { return bookButton; }
    public Button getCancelButton() { return cancelButton; }
    public Label getStatusLabel() { return statusLabel; }

    // Update UI from controller
    public void updateBookingsList(List<String> bookings) {
        bookingsList.setItems(FXCollections.observableArrayList(bookings));
    }

    public void updateStatus(String message, String style) {
        statusLabel.setText(message);
        statusLabel.setStyle(style);
    }

    public String getSelectedUserId() {
        String selected = userCombo.getValue();
        if (selected == null) return null;
        return selected.split(" - ")[0]; // "U001 - Alice"
    }

    public String getSelectedEventId() {
        String selected = eventCombo.getValue();
        if (selected == null) return null;
        return selected.split(" - ")[0]; // "E101 - Intro to Git"
    }

    public void setUsers(List<String> userStrings) {
        userCombo.setItems(FXCollections.observableArrayList(userStrings));
    }

    public void setEvents(List<String> eventStrings) {
        eventCombo.setItems(FXCollections.observableArrayList(eventStrings));
    }
}