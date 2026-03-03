import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.ArrayList;
import java.util.List;

//Allows admin to view and manage the waitlist for each event.
public class WaitlistController {

    private WaitlistManager waitlistManager = new WaitlistManager();
    private List<Event> events = new ArrayList<>();

    private VBox view;
    private ComboBox<String> eventDropdown;
    private ListView<String> waitlistView;
    private ListView<String> confirmedView;
    private Label statusLabel;
    private Label promotionLabel;

    public WaitlistController() {
        buildUI();
    }

    //Set the list of events to display in the dropdown
    public void setEvents(List<Event> events) {
        this.events = events;
        List<String> names = new ArrayList<>();
        for (Event e : events) {
            names.add(e.getEventId() + " - " + e.getTitle());
        }
        eventDropdown.setItems(FXCollections.observableArrayList(names));
    }

    private void buildUI() {
        view = new VBox(12);
        view.setPadding(new Insets(20));

        // Title
        Label title = new Label("Waitlist Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        // Event selection
        Label selectLabel = new Label("Select Event:");
        eventDropdown = new ComboBox<>();
        eventDropdown.setPromptText("Choose an event...");
        eventDropdown.setPrefWidth(250);
        eventDropdown.setOnAction(e -> refreshLists());

        // Promotion notification label
        promotionLabel = new Label("");
        promotionLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        promotionLabel.setStyle("-fx-text-fill: green;");

        // Waitlist section
        Label waitlistLabel = new Label("Waitlist (in order):");
        waitlistLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        waitlistView = new ListView<>();
        waitlistView.setPrefHeight(150);

        // Confirmed section
        Label confirmedLabel = new Label("Confirmed Bookings:");
        confirmedLabel.setFont(Font.font("System", FontWeight.BOLD, 13));
        confirmedView = new ListView<>();
        confirmedView.setPrefHeight(150);

        // Buttons
        Button cancelWaitlistBtn = new Button("Cancel Selected Waitlisted Booking");
        cancelWaitlistBtn.setOnAction(e -> cancelWaitlisted());

        Button cancelConfirmedBtn = new Button("Cancel Selected Confirmed Booking");
        cancelConfirmedBtn.setOnAction(e -> cancelConfirmed());

        HBox buttonRow = new HBox(10);
        buttonRow.getChildren().addAll(cancelWaitlistBtn, cancelConfirmedBtn);

        // Status label
        statusLabel = new Label("Select an event to view its waitlist.");
        statusLabel.setStyle("-fx-text-fill: grey;");

        view.getChildren().addAll(
            title,
            selectLabel, eventDropdown,
            promotionLabel,
            waitlistLabel, waitlistView,
            confirmedLabel, confirmedView,
            buttonRow,
            statusLabel
        );
    }

    private void refreshLists() {
        promotionLabel.setText("");
        Event event = getSelectedEvent();
        if (event == null) return;

        // Populate waitlist
        List<String> waitlistItems = new ArrayList<>();
        List<Booking> waitlist = waitlistManager.getWaitlist(event);
        for (int i = 0; i < waitlist.size(); i++) {
            Booking b = waitlist.get(i);
            waitlistItems.add((i + 1) + ". " + b.getUserId() +
                              " (Booking: " + b.getBookingId() + ")");
        }
        waitlistView.setItems(FXCollections.observableArrayList(waitlistItems));

        // Populate confirmed
        List<String> confirmedItems = new ArrayList<>();
        for (Booking b : waitlistManager.getConfirmed(event)) {
            confirmedItems.add(b.getUserId() +
                               " (Booking: " + b.getBookingId() + ")");
        }
        confirmedView.setItems(FXCollections.observableArrayList(confirmedItems));

        statusLabel.setText("Showing: " + event.getTitle() +
                            " | Waitlist: " + waitlist.size() +
                            " | Confirmed: " + confirmedItems.size());
    }

    //Cancel a selected waitlisted booking
    private void cancelWaitlisted() {
        Event event = getSelectedEvent();
        int index = waitlistView.getSelectionModel().getSelectedIndex();
        if (event == null || index < 0) {
            statusLabel.setText("Please select an event and a waitlisted booking.");
            return;
        }
        Booking booking = waitlistManager.getWaitlist(event).get(index);
        event.cancelBooking(booking);
        statusLabel.setText("Waitlisted booking cancelled.");
        promotionLabel.setText("");
        refreshLists();
    }

    //Cancel a selected confirmed booking and show promotion if one occurs.
    private void cancelConfirmed() {
        Event event = getSelectedEvent();
        int index = confirmedView.getSelectionModel().getSelectedIndex();
        if (event == null || index < 0) {
            statusLabel.setText("Please select an event and a confirmed booking.");
            return;
        }
        Booking booking = waitlistManager.getConfirmed(event).get(index);
        Booking promoted = waitlistManager.cancelBooking(event, booking);

        if (promoted != null) {
            promotionLabel.setText("✓ " + promoted.getUserId() +
                                   " has been promoted to Confirmed!");
        } else {
            promotionLabel.setText("");
        }

        statusLabel.setText("Confirmed booking cancelled.");
        refreshLists();
    }

    //Get the currently selected Event from the dropdown.
    private Event getSelectedEvent() {
        int index = eventDropdown.getSelectionModel().getSelectedIndex();
        if (index < 0 || index >= events.size()) return null;
        return events.get(index);
    }

    // Returns the UI pane to embed in MainApp
    public VBox getView() {
        return view;
    }
}
