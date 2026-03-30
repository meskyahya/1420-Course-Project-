package com.campusbooking.controllers;

import com.campusbooking.managers.*;
import com.campusbooking.models.*;
import com.campusbooking.views.*;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventController {

    private EventManager eventManager;
    private EventView view;
    private Runnable OnEventsChanged;
    private ListView<String> eventList = new ListView<>();

    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
        this.view = new EventView();
        initialize();
        view.refreshEventList(getAllEventsAsString());
    }
    // Update Events On change
    public void setOnEventsChanged(Runnable callback) {
        this.OnEventsChanged = callback;
    }

    // Start Up
    private void initialize() {
        view.getAddButton().setOnAction(e -> handleAddEvent());
        view.getCancelButton().setOnAction(e -> handleCancelEvent());
        view.getSearchButton().setOnAction(e -> handleSearch());
        view.getClearButton().setOnAction(e -> refreshEventList());
    }
    // Handle Add Events and Errors
    private void handleAddEvent() {
        try {
            String id = view.getEventId();
            String title = view.getTitle();
            String capacityText = view.getCapacity();
            String type = view.getEventType();
            String extra = view.getExtraField();
            String location = view.getLocation();
            String dateTimeText = view.getDateTime();

            if (id.isEmpty() || title.isEmpty() || type == null ||
                location.isEmpty() || dateTimeText.isEmpty()) {
                showError("All required fields must be filled.");
                return;
            }

            int capacity = Integer.parseInt(capacityText);
            if (capacity <= 0) {
                showError("Capacity must be greater than 0.");
                return;
            }

            LocalDateTime dateTime = LocalDateTime.parse(dateTimeText);

            Event event;
            if (type.equals("Workshop")) {
                event = new Workshop(id, title, dateTime, location, capacity, extra);
            } else if (type.equals("Seminar")) {
                event = new Seminar(id, title, dateTime, location, capacity, extra);
            } else if (type.equals("Concert")) {
                event = new Concert(id, title, dateTime, location, capacity, extra);
            } else {
                showError("Unknown event type.");
                return;
            }

            eventManager.addEvent(event);
            view.refreshEventList(getAllEventsAsString());
            view.clearFields();

            if (OnEventsChanged != null) OnEventsChanged.run();

        } catch (NumberFormatException ex) {
            showError("Capacity must be a number.");
        } catch (Exception ex) {
            showError(ex.getMessage());
        }
    }

    // Handle Cancel Event
    private void handleCancelEvent() {
        String selected = view.getSelectedEventString();
        if (selected == null) {
            showError("Select an event to cancel.");
            return;
        }
        String eventId = selected.split(" - ")[0];
        eventManager.cancelEvent(eventId);
        view.refreshEventList(getAllEventsAsString());
        if (OnEventsChanged != null) OnEventsChanged.run();
    }
    // Handle Search
    private void handleSearch() {
        String searchText = view.getSearchText().toLowerCase();
        String filterType = view.getFilterType();

        List<Event> allEvents = eventManager.getAllEvents();
        List<String> results = new ArrayList<>();

        for (int i = 0; i < allEvents.size(); i++) {
            Event e = allEvents.get(i);

            boolean titleMatch = e.getTitle().toLowerCase().contains(searchText);

            boolean typeMatch;
            if (filterType.equals("All")) {
                typeMatch = true;
            } else if (filterType.equals("Workshop") && e instanceof Workshop) {
                typeMatch = true;
            } else if (filterType.equals("Seminar") && e instanceof Seminar) {
                typeMatch = true;
            } else if (filterType.equals("Concert") && e instanceof Concert) {
                typeMatch = true;
            } else {
                typeMatch = false;
            }

            if (titleMatch && typeMatch) {
                results.add(e.getEventId() + " - " + e.getTitle() +
                            " (" + e.getConfirmedBookings().size() +
                            "/" + e.getCapacity() + ") [" + e.getStatus() + "]");
            }
        }

        view.refreshEventList(results);
    }

    // Turns Events to Strings
    private List<String> getAllEventsAsString() {
        List<Event> events = eventManager.getAllEvents();
        List<String> result = new ArrayList<>();
        for (Event e : events) {
            result.add(e.getEventId() + " - " + e.getTitle() +
                       " (" + e.getConfirmedBookings().size() +
                       "/" + e.getCapacity() + ") [" + e.getStatus() + "]");
        }
        return result;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    public void refreshEventList() {
        view.refreshEventList(getAllEventsAsString());
    }

    public VBox getView() {
        return view;
    }
}
