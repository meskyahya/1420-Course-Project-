import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import java.util.ArrayList;

public class UserController {

    private ArrayList<User> users = new ArrayList<User>();
    private VBox view;
    private TextField idField;
    private TextField nameField;
    private TextField emailField;
    private ComboBox<String> typeBox;
    private ListView<String> listView;
    private Label statusLabel;

    public UserController() {
        buildUI();
    }

    private void buildUI() {
        view = new VBox(10);
        view.setPadding(new Insets(20));

        Label title = new Label("User Management");
        title.setFont(Font.font("System", FontWeight.BOLD, 20));

        idField = new TextField();
        idField.setPromptText("Enter user ID (number)");
        nameField = new TextField();
        nameField.setPromptText("Enter name");
        emailField = new TextField();
        emailField.setPromptText("Enter email");

        typeBox = new ComboBox<String>();
        typeBox.getItems().add("Student");
        typeBox.getItems().add("Staff");
        typeBox.getItems().add("Guest");
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

        Button addButton = new Button("Add User");
        Button viewButton = new Button("View All Users");
        HBox buttons = new HBox(10, addButton, viewButton);
        form.add(buttons, 0, 4, 2, 1);

        listView = new ListView<String>();
        listView.setPrefHeight(200);

        statusLabel = new Label("Fill in details and click Add User.");
        statusLabel.setStyle("-fx-text-fill: grey;");

        view.getChildren().addAll(title, form, new Label("Registered Users:"), listView, statusLabel);

        addButton.setOnAction(e -> handleAddUser());
        viewButton.setOnAction(e -> handleViewUsers());
    }

    private void handleAddUser() {
        String idText = idField.getText().trim();
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String type = typeBox.getValue();

        if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || type == null) {
            statusLabel.setText("Please fill in all fields.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        int id;
        try {
            id = Integer.parseInt(idText);
        } catch (NumberFormatException ex) {
            statusLabel.setText("User ID must be a number.");
            statusLabel.setStyle("-fx-text-fill: red;");
            return;
        }

        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getuserID() == id) {
                statusLabel.setText("User ID already exists.");
                statusLabel.setStyle("-fx-text-fill: red;");
                return;
            }
        }

        User user;
        if (type.equals("Student")) {
            user = new Student(name, email, id);
        } else if (type.equals("Staff")) {
            user = new Staff(name, email, id);
        } else {
            user = new Guest(name, email, id);
        }

        users.add(user);
        listView.getItems().add(
                "ID: " + user.getuserID() +
                        " | Name: " + user.getname() +
                        " | Email: " + user.getemail() +
                        " | Role: " + user.getrole()
        );

        idField.clear();
        nameField.clear();
        emailField.clear();
        typeBox.getSelectionModel().clearSelection();
        statusLabel.setText("User added successfully.");
        statusLabel.setStyle("-fx-text-fill: green;");
    }

    private void handleViewUsers() {
        listView.getItems().clear();
        if (users.size() == 0) {
            statusLabel.setText("No users registered yet.");
            statusLabel.setStyle("-fx-text-fill: grey;");
            return;
        }
        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            listView.getItems().add(
                    "ID: " + u.getuserID() +
                            " | Name: " + u.getname() +
                            " | Email: " + u.getemail() +
                            " | Role: " + u.getrole()
            );
        }
        statusLabel.setText("Showing " + users.size() + " user(s).");
        statusLabel.setStyle("-fx-text-fill: grey;");
    }

    public ArrayList<User> getUsers() { return users; }
    public VBox getView() { return view; }
}
