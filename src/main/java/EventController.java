package controllers;

import javafx.scene.control.Alert;
import models.*;
import views.EventView;

public class EventController {

    private EventBookingSystem system;
    private EventView view;

    public EventController(EventBookingSystem system, EventView view) {
        this.system = system;
        this.view = view;
        initialize();
    }

    private void initialize() {
        view.getAddButton().setOnAction(e -> handleAddEvent());
        view.getCancelButton().setOnAction(e -> handleCancelEvent());
    }

    private void handleAddEvent() {
        try {
            String id = view.getEventId();
            String title = view.getTitle();
            int capacity = Integer.parseInt(view.getCapacity());
            String type = view.getEventType();
            String extra = view.getExtraField();

            if (id.isEmpty() || title.isEmpty() || type == null) {
                showError("All required fields must be filled.");
                return;
            }

            if (capacity <= 0) {
                showError("Capacity must be greater than 0.");
                return;
            }

            Event event = switch (type) {
                case "Workshop" -> new Workshop(id, title, capacity, extra);
                case "Seminar" -> new Seminar(id, title, capacity, extra);
                case "Concert" -> new Concert(id, title, capacity, extra);
                default -> null;
            };

            system.addEvent(event);
            view.refreshEventList(system.getAllEventsAsString());
            view.clearFields();

        } catch (NumberFormatException ex) {
            showError("Capacity must be a number.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    private void handleCancelEvent() {

        String selected = view.getSelectedEventString();

        if (selected == null) {
            showError("Select an event to cancel.");
            return;
        }

        // Extract eventId from string format: "E001 - Title (2/10)"
        String eventId = selected.split(" - ")[0];

        system.cancelEvent(eventId);
        view.refreshEventList(system.getAllEventsAsString());
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }
}
