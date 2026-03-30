// Declaring the package
package com.campusbooking.controllers;

// Importing all the classes from managers package
import com.campusbooking.managers.*;

// Importing the model classes like User, Student, Staff, Guest
import com.campusbooking.models.*;

// Importing the UserView class [UI]
import com.campusbooking.views.*;

// Importing the JavaFX UI controls (including buttons, labels)
import javafx.scene.control.*;

// Importing the layout classes of JavaFX
import javafx.scene.layout.*;

// Importing the List interface
import java.util.List;

// Usercontroller Defines the controller class that manages user-related logic which includes creating a user, validating the user input, storing the details and 
// diplay them.
public class UserController {

    // fields to store the users in the system
    private List<User> users;

    // fields to store the UI
    private UserView view;

    // a callback funciton to modify if the user changes 
    private Runnable onUserChanged;

    // Creating a constuctor that takes in a list of users
    public UserController(List<User> users) {

        // assiging the passed user list to the controller
        this.users = users;

        // creating a new userview object
        this.view = new UserView();

        // method call to update the user list.
        refreshUsers();

        // method call( runs addUser if the button is clicked)
        view.getAddButton().setOnAction(e -> addUser());

        // method call (runs refreshUsers if the button is clicked)
        view.getViewButton().setOnAction(e -> refreshUsers());
    }

    // creating a method that lets the other classes to set the callback 
    public void setOnUserChanged(Runnable callback) {

        // assigning the parameter to the field
        this.onUserChanged = callback;
    }

    // creating a method to add new user
    private void addUser() {
        try {
            // creating a variable to store ID inputs and removes any extra spaces
            String id = view.getIdField().getText().trim();

            // creating a variable to store the name input
            String name = view.getNameField().getText();

            // creating a variable to store the emai input
            String email = view.getEmailField().getText();

            // creating a varible to store the input that was selected from the drop down 
            String role = view.getTypeBox().getValue();

            // if statements to check if the fields are empty
            if (id.isEmpty() || name.isEmpty() || email.isEmpty() || role == null) {

                // if true then a message is displayed to ask the user to fill all the fields
                view.getStatusLabel().setText("Please fill in all fields.");
                return;
            }

            // for loop to go through each user
            for (int i = 0; i < users.size(); i++) {

                // if statements to see if the user Id exists 
                if (users.get(i).getuserID().equals(id)) {

                    // if true an error message is displayed
                    view.getStatusLabel().setText("The entered User ID already exists.");
                    return;
                }
            }

            // creating a varible to store the new user object
            User user;

            // switch statement to choose which type of user
            switch (role) {

                // if student create student object
                case "Student":
                    user = new Student(name, email, id);
                    break;
                    
                //  if staff creats staff object 
                case "Staff":
                    user = new Staff(name, email, id);
                    break;
                    
                // the default user is guest so creates guest object
                default:
                    user = new Guest(name, email, id);
            }

            // adding user to the list 
            users.add(user);

            // updating the UI list
            refreshUsers();

            // displays a message if no errors
            view.getStatusLabel().setText(" THe User is added successfully!!");

            // If a callback is set, run it (notify other parts of program)
            if (onUserChanged != null) onUserChanged.run();

            // clearing input fields
            view.clearFields();

        // runs if there is an error
        } catch (Exception ex) {

            // displays error message
            view.getStatusLabel().setText("The user data is INVALID.");
        }
    }

    // Creating a method to update the list on screen
    private void refreshUsers() {

        // clearing the current list in the UI
        view.getListView().getItems().clear();

        // for loop which goes through each user
        for (User u : users) {

            // adding a text to the UI list
            view.getListView().getItems().add( u.getuserID() + " - " + u.getname() + " (" + u.getrole() + ")");
        }
    }

    // creating a Method to return view
    public UserView getView() {
        return view;
    }

    // creating a method to return user list
    public List<User> getUsers() {
        return users;
    }
}
