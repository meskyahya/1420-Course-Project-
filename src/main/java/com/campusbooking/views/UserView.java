package com.campusbooking.views;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

public class UserView {

    private VBox view;

    private TextField idField;
    private TextField nameField;
    private TextField emailField;

    private ComboBox<String> typeBox;

    private ListView<String> listView;

    private Label statusLabel;

    private Button addButton;
    private Button viewButton;

    public UserView() {
        buildUI();
    }

    private void buildUI() {

        view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("User Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        idField = new TextField();
        idField.setPromptText("Enter user ID");

        nameField = new TextField();
        nameField.setPromptText("Enter name");

        emailField = new TextField();
        emailField.setPromptText("Enter email");

        typeBox = new ComboBox<>();
        typeBox.getItems().addAll("Student", "Staff", "Guest");
        typeBox.setPromptText("Select role...");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(10);
        form.setPadding(new Insets(10));

        form.add(new Label("User ID:"), 0, 0);
        form.add(idField, 1, 0);

        form.add(new Label("Name:"), 0, 1);
        form.add(nameField, 1, 1);

        form.add(new Label("Email:"), 0, 2);
        form.add(emailField, 1, 2);

        form.add(new Label("Role:"), 0, 3);
        form.add(typeBox, 1, 3);

        addButton = new Button("Add User");
        viewButton = new Button("Refresh List");

        HBox buttons = new HBox(10, addButton, viewButton);

        form.add(buttons, 0, 4, 2, 1);

        listView = new ListView<>();
        listView.setPrefHeight(200);

        statusLabel = new Label("Fill in details and click Add User.");

        view.getChildren().addAll(
                title,
                form,
                new Label("Registered Users:"),
                listView,
                statusLabel
        );
    }

    public VBox getView() { return view; }

    public TextField getIdField() { return idField; }

    public TextField getNameField() { return nameField; }

    public TextField getEmailField() { return emailField; }

    public ComboBox<String> getTypeBox() { return typeBox; }

    public ListView<String> getListView() { return listView; }

    public Label getStatusLabel() { return statusLabel; }

    public Button getAddButton() { return addButton; }

    public Button getViewButton() { return viewButton; }

    public void clearFields() {
        idField.clear();
        nameField.clear();
        emailField.clear();
        typeBox.getSelectionModel();
    }
}
