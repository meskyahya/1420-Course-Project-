import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.util.List;

public class EventView extends VBox {

    private TextField idField = new TextField();
    private TextField titleField = new TextField();
    private TextField capacityField = new TextField();

    private ComboBox<String> typeBox = new ComboBox<>();
    private TextField extraField = new TextField();

    private Button addButton = new Button("Add Event");
    private Button cancelButton = new Button("Cancel Selected Event");

    private ListView<String> eventList = new ListView<>();

    public EventView() {

        setSpacing(10);

        typeBox.getItems().addAll("Workshop", "Seminar", "Concert");

        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            if (type == null) return;

            switch (type) {
                case "Workshop" -> extraField.setPromptText("Topic");
                case "Seminar" -> extraField.setPromptText("Speaker Name");
                case "Concert" -> extraField.setPromptText("Age Restriction");
            }
        });

        getChildren().addAll(
                new Label("Event ID"), idField,
                new Label("Title"), titleField,
                new Label("Capacity"), capacityField,
                new Label("Event Type"), typeBox,
                new Label("Extra Field"), extraField,
                addButton,
                cancelButton,
                new Label("Events"),
                eventList
        );
    }

    // --- Getters for Controller ---
    public String getEventId() { return idField.getText(); }
    public String getTitle() { return titleField.getText(); }
    public String getCapacity() { return capacityField.getText(); }
    public String getEventType() { return typeBox.getValue(); }
    public String getExtraField() { return extraField.getText(); }

    public Button getAddButton() { return addButton; }
    public Button getCancelButton() { return cancelButton; }

    public String getSelectedEventString() {
        return eventList.getSelectionModel().getSelectedItem();
    }

    public void refreshEventList(List<String> events) {
        eventList.getItems().setAll(events);
    }

    public void clearFields() {
        idField.clear();
        titleField.clear();
        capacityField.clear();
        extraField.clear();
        typeBox.getSelectionModel().clearSelection();
    }
}
