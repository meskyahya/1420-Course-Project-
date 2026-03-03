package controllers;

// importing your user models
import models.User;
import models.Student;
import models.Staff;
import models.Guest;

// importing javafx files to create a GUI
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.geometry.Insets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import java.util.ArrayList;

// Creating a class called UserController
// removed extends Application because MainApp launches JavaFX
public class UserController {

    // Creating an array list called users to store all the user objects like student, staff and guest and
    // an array list is used here as the size of the array can be increased or decreased
    ArrayList<User> users = new ArrayList<User>();

    // root pane to return to MainApp tab
    private VBox root;

    // ListView to display the Id, name, email and role
    ListView<String> listview;

    // Method to build UI and return root VBox
    public VBox getView() {

        // Creating the input fields, these will create the boxes for the user to enter their details.
        // Box for the ID
        TextField idfield = new TextField();
        // Box for the name
        TextField namefield = new TextField();
        // Box for the email
        TextField emailfield = new TextField();

        // Creating a ComboBox where the user can choose between student, staff and guest.
        // the add function adds the Student, Staff and Guest to the boxes where the user can choose one
        ComboBox<String> typeBox = new ComboBox<String>();
        typeBox.getItems().add("Student");
        typeBox.getItems().add("Staff");
        typeBox.getItems().add("Guest");

        // creating a clickable button and the addbutton will add users and the view button lets you view the users
        Button addbutton = new Button("Add an User");
        Button viewbutton = new Button("View an Users");

        // Creating a list to view the Id, name , email and their role(staff, student, guest).
        listview = new ListView<String>();

        // Creating the layout
        // GridPane is used to create a grid to place the elements in rows and cols
        GridPane form = new GridPane();
        // the horizontal space is 10 pixel
        form.setHgap(10);
        // the vertical spacing is 10 pixel
        form.setVgap(10);
        // creating spaces around the layouts of the grid
        form.setPadding(new Insets(10));

        // form.add adds the elements on to the grid
        // row 0 is for the ID
        form.add(new Label("User ID:"), 0, 0);
        form.add(idfield, 1, 0);
        // row 1 is for the name
        form.add(new Label("Name:"), 0, 1);
        form.add(namefield, 1, 1);
        // row 2 is for the email
        form.add(new Label("Email:"), 0, 2);
        form.add(emailfield, 1, 2);
        // row 3 is for the roles
        form.add(new Label("Role:"), 0, 3);
        form.add(typeBox, 1, 3);

        // The HBox adds the elements horizontally
        HBox buttons = new HBox(10, addbutton, viewbutton);
        form.add(buttons, 0, 4, 2, 1);

        // The VBox adds the elements vertically
        root = new VBox(10, form, listview);
        // setPadding is for the spaces near the edges
        root.setPadding(new Insets(10));

        // the setOnAction defines what will happen after the button is clicked
        addbutton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // Reads what the user had entered and the getText reads from TextField and getValue reads from ComboBox
                String idText = idfield.getText();
                String name = namefield.getText();
                String email = emailfield.getText();
                String type = typeBox.getValue();

                // checks if the user have entered all the details and if it's true then
                // an error message is printed
                if (idText.isEmpty() || name.isEmpty() || email.isEmpty() || type == null) {
                    showAlert("Please fill in all your details");
                    return;
                }

                // Converts the text to numbers
                int id = Integer.parseInt(idText);

                // checking if the given ID already exists and if it did then an error message is printed
                for (int i = 0; i < users.size(); i++) {
                    if (users.get(i).getId() == id) {
                        showAlert("Please Enter a valid ID this already exists");
                        return;
                    }
                }

                // creating a new user object based on the role the user selects
                User user;
                // student
                if (type.equals("Student")) {
                    user = new Student(id, name, email);
                }
                // staff
                else if (type.equals("Staff")) {
                    user = new Staff(id, name, email);
                }
                // guest
                else {
                    user = new Guest(id, name, email);
                }

                // adding the user to the user list and then displaying them
                users.add(user);
                listview.getItems().add(user.toString());

                // cleans the input boxes so the user can enter multiple times
                idfield.clear();
                namefield.clear();
                emailfield.clear();
                typeBox.getSelectionModel().clearSelection();
            }

        });

        // the viewbutton defines what happens when it's clicked
        viewbutton.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent e) {

                // clearing the list to ensure there is no duplicates
                listview.getItems().clear();

                // using the for loop to add on all the user details
                for (int i = 0; i < users.size(); i++) {
                    User u = users.get(i);

                    listview.getItems().add(
                            "ID: " + u.getId() +
                                    " Name: " + u.getName() +
                                    " Email: " + u.getEmail() +
                                    " Role: " + u.getRole()
                    );
                }

            }
        });

        // return the root VBox so MainApp can add it to a tab
        return root;
    }

    // creating an alert message and the message will be displayed till the user closes it
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
