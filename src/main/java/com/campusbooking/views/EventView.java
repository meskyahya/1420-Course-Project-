package com.campusbooking.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.List;

public class EventView extends VBox {
    private TextField idField = new TextField();
    private TextField titleField = new TextField();
    private TextField capacityField = new TextField();
    private TextField locationField = new TextField();
    private TextField dateTimeField = new TextField();
    private ComboBox<String> typeBox = new ComboBox<>();
    private TextField extraField = new TextField();
    private Button addButton = new Button("Add Event");
    private Button cancelButton = new Button("Cancel Selected Event");
    private ListView<String> eventList = new ListView<>();
    // Search and filter
    private TextField searchField = new TextField();
    private ComboBox<String> filterBox = new ComboBox<>();
    private Button searchButton = new Button("Search");
    private Button clearButton = new Button("Clear");

    public EventView() {
        setSpacing(12);
        setPadding(new Insets(20));

        Label title = new Label("Event Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));
        getChildren().add(title);

        typeBox.getItems().addAll("Workshop", "Seminar", "Concert");
        dateTimeField.setPromptText("e.g. 2026-04-01T10:00");
        typeBox.setOnAction(e -> {
            String type = typeBox.getValue();
            if (type == null) return;
            switch (type) {
                case "Workshop" -> extraField.setPromptText("Topic");
                case "Seminar" -> extraField.setPromptText("Speaker Name");
                case "Concert" -> extraField.setPromptText("Age Restriction");
            }
        });
        filterBox.getItems().addAll("All", "Workshop", "Seminar", "Concert");
filterBox.setValue("All");
searchField.setPromptText("Search by title...");

HBox searchRow = new HBox(10, searchField, filterBox, searchButton, clearButton);

getChildren().addAll(
    new Label("Event ID"), idField,
    new Label("Title"), titleField,
    new Label("Capacity"), capacityField,
    new Label("Location"), locationField,
    new Label("Date/Time"), dateTimeField,
    new Label("Event Type"), typeBox,
    new Label("Extra Field"), extraField,
    addButton,
    cancelButton,
    new Label("Search & Filter"), searchRow,
    new Label("Events"),
    eventList
);
    }

    public String getEventId()     { return idField.getText(); }
    public String getTitle()       { return titleField.getText(); }
    public String getCapacity()    { return capacityField.getText(); }
    public String getLocation()    { return locationField.getText(); }
    public String getDateTime()    { return dateTimeField.getText(); }
    public String getEventType()   { return typeBox.getValue(); }
    public String getExtraField()  { return extraField.getText(); }
    public Button getAddButton()   { return addButton; }
    public Button getCancelButton(){ return cancelButton; }

    public String getSelectedEventString() {
        return eventList.getSelectionModel().getSelectedItem();
    }
    public String getSearchText() { return searchField.getText().trim(); }
    public String getFilterType() { return filterBox.getValue(); }
    public Button getSearchButton() { return searchButton; }
    public Button getClearButton() { return clearButton; }

    public void refreshEventList(List<String> events) {
        eventList.getItems().setAll(events);
    }

    public void clearFields() {
        idField.clear();
        titleField.clear();
        capacityField.clear();
        locationField.clear();
        dateTimeField.clear();
        extraField.clear();
        typeBox.getSelectionModel().clearSelection();
    }

}
