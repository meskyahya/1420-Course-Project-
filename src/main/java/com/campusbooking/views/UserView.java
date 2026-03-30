// main package that groups all the view (UI) classes together
package com.campusbooking.views;

// import for spacing and padding
import javafx.geometry.Insets;

// import for UI controls like buttons, text fields, labels
import javafx.scene.control.*;

// import for layout containers like VBox and GridPane
import javafx.scene.layout.*;

// import for font styling
import javafx.scene.text.Font;

// import for font weight styling
import javafx.scene.text.FontWeight;

// creaitng a class that creates the user interface for managing users
public class UserView {

    //creating field that holds the main vertical layout container
    private VBox view;

    // creating a field for user to enter user ID
    private TextField idField;

    // creating a field for user to enter name
    private TextField nameField;

    // creating a field for user to enter email
    private TextField emailField;

    // creating a dropdown for selecting user role (Student, Staff, Guest)
    private ComboBox<String> typeBox;

    // craeting a list that shows all the registered users
    private ListView<String> listView;

    // creating a label that shows messages or the status update
    private Label statusLabel;

    // creating a button to add a user
    private Button addButton;

    // creating button to refresh the list of users
    private Button viewButton;

    // creating a constructor that runs when UserView is created
    public UserView() {

        // calls method to build the interface
        buildUI();
    }

    // craeting a method that sets up the layout and UI elements
    private void buildUI() {

        // creating vertical layout with 10px spacing
        view = new VBox(10);

        // adding 20px padding around the layout
        view.setPadding(new Insets(20));

        // creating a label for the title
        Label title = new Label("User Management");

        // setting the font and size
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        // creating text field for user ID
        idField = new TextField();

         // displaying the placeholder text
        idField.setPromptText("Enter user ID");

        // creating a text field for name
        nameField = new TextField();

        // displaying the placeholder text
        nameField.setPromptText("Enter name");

        // creating text field for email
        emailField = new TextField();

        // displaying the placeholder text
        emailField.setPromptText("Enter email");

         // creating a dropdown for user roles
        typeBox = new ComboBox<>();

         // adding options to the dropdown box
        typeBox.getItems().addAll("Student", "Staff", "Guest");

        // displaying the placeholder text
        typeBox.setPromptText("Select a role...");

        // creating a grid layout for the form
        GridPane form = new GridPane();

        // horizontal spacing between columns
        form.setHgap(10);

        // vertical spacing between rows
        form.setVgap(10);

        // creating a padding inside the form
        form.setPadding(new Insets(10));

        // adding a label at column 0 row 0
        form.add(new Label("User ID:"), 0, 0);

         // adding a text field at column 1 row 0
        form.add(idField, 1, 0);

        // adding a label at column 0 row 1
        form.add(new Label("Name:"), 0, 1);

        // adding a text field at column 1 row 1
        form.add(nameField, 1, 1);

        // adding a label at column 0 row 2
        form.add(new Label("Email:"), 0, 2);

        // adding a text field at column 1 row 2
        form.add(emailField, 1, 2);

        // adding a label at column 0 row 3
        form.add(new Label("Role:"), 0, 3);

        // adding a dropdown at column 1 row 3
        form.add(typeBox, 1, 3);

        // creating a button to add user
        addButton = new Button("Add User");

        // creating button to refresh list
        viewButton = new Button("Refresh List");

        // creating horizontal layout for the buttons with 10px spacing
        HBox buttons = new HBox(10, addButton, viewButton);

        // adding buttons to form spanning 2 columns
        form.add(buttons, 0, 4, 2, 1);

        // creating list view to display users
        listView = new ListView<>();

        // setting preferred height of list
        listView.setPrefHeight(200);

         // creating a label to show status messages
        statusLabel = new Label("Fill in details and click Add User.");

        // adding all the UI elements to main vertical layout
        view.getChildren().addAll(
                title,
                form,
                // label above the list view to show a heading for the registered users section
                new Label("Registered Users:"),
                listView,
                statusLabel
        );
    }

    // creating a method that returns main layout container
    public VBox getView() { 
        return view; }

    // creating a method which returns ID text field
    public TextField getIdField() { 
        return idField; }

    // creating a method which returns name text field
    public TextField getNameField() { 
        return nameField; }

    // creating a method which returns email text field
    public TextField getEmailField() {
        return emailField; }

    // creating a method which returns role dropdown
    public ComboBox<String> getTypeBox() { 
        return typeBox; }

    // creating a method which returns list view
    public ListView<String> getListView() { 
        return listView; }

    // creating a method which returns status label
    public Label getStatusLabel() { 
        return statusLabel; }

    // creating a method which returns Add User button
    public Button getAddButton() { 
        return addButton; }

    // creating a method which returns Refresh List button
    public Button getViewButton() { 
        return viewButton; }

    // creating a method to clear all input fields
    public void clearFields() {

        // clearing the ID field
        idField.clear();

        // clearing the name field
        nameField.clear();

        // clearing the email field
        emailField.clear();

        // clearing the dropdown selection
        typeBox.getSelectionModel();
    }
}
