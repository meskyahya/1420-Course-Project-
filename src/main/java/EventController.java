import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class EventController {

    private EventManager eventManager;
    private EventView view;

    public EventController(EventManager eventManager) {
        this.eventManager = eventManager;
        this.view = new EventView();
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
        String eventId = selected.split(" - ")[0];
        eventManager.cancelEvent(eventId);
        view.refreshEventList(getAllEventsAsString());
    }

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

    public VBox getView() {
        return view;
    }
}
