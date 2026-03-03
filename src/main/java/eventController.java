import javafx.scene.control.Alert;
import javafx.scene.layout.VBox;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

            if (id == null || id.isEmpty() ||
                    title == null || title.isEmpty() ||
                    location == null || location.isEmpty() ||
                    dateTimeText == null || dateTimeText.isEmpty() ||
                    type == null) {
                showError("All required fields must be filled.");
                return;
            }

            int capacity = Integer.parseInt(capacityText);

            DateTimeFormatter formatter =
                    DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            LocalDateTime dateTime =
                    LocalDateTime.parse(dateTimeText, formatter);

            Event event;

            switch (type) {
                case "Workshop":
                    event = new Workshop(id, title, dateTime, location, capacity, extra);
                    break;

                case "Seminar":
                    event = new Seminar(id, title, dateTime, location, capacity, extra);
                    break;

                case "Concert":
                    event = new Concert(id, title, dateTime, location, capacity, extra);
                    break;

                default:
                    showError("Invalid event type.");
                    return;
            }

            eventManager.addEvent(event);
            refreshView();
            view.clearFields();

        } catch (NumberFormatException e) {
            showError("Capacity must be a number.");
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    private void handleCancelEvent() {
        String selected = view.getSelectedEventString();
        if (selected == null) {
            showError("Select an event.");
            return;
        }

        String eventId = selected.split(" - ")[0];
        eventManager.cancelEvent(eventId);
        refreshView();
    }

    private void refreshView() {
        List<String> eventStrings = new ArrayList<>();

        for (Event e : eventManager.getAllEvents()) {
            eventStrings.add(
                    e.getEventId() + " - " +
                            e.getTitle() + " (" +
                            e.getConfirmedBookings().size() + "/" +
                            e.getCapacity() + ") [" +
                            e.getStatus() + "]"
            );
        }

        view.refreshEventList(eventStrings);
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