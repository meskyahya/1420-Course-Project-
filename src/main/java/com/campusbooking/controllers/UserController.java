package com.campusbooking.controllers;

import com.campusbooking.managers.*;
import com.campusbooking.models.*;
import com.campusbooking.views.*;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.List;

public class UserController {

    private List<User> users;
    private UserView view;
    private Runnable onUserChanged; //update user list

    public UserController(List<User> users) {

        this.users = users;
        this.view = new UserView();

        refreshUsers();

        view.getAddButton().setOnAction(e -> addUser());
        view.getViewButton().setOnAction(e -> refreshUsers());
    }

    public void setOnUserChanged(Runnable callback) {
        this.onUserChanged = callback;
    }

    private void addUser() {

        try {

            int id = Integer.parseInt(view.getIdField().getText());

            String name = view.getNameField().getText();
            String email = view.getEmailField().getText();
            String role = view.getTypeBox().getValue();

            User user;

            switch (role) {
                case "Student":
                    user = new Student(name, email, id);
                    break;
                case "Staff":
                    user = new Staff(name, email, id);
                    break;
                default:
                    user = new Guest(name, email, id);
            }

            users.add(user);

            refreshUsers();

            view.getStatusLabel().setText("User added successfully.");

            if (onUserChanged != null) onUserChanged.run(); //update user list

            view.clearFields();

        } catch (Exception ex) {

            view.getStatusLabel().setText("Invalid user data.");
        }
    }

    private void refreshUsers() {

        view.getListView().getItems().clear();

        for (User u : users) {
            view.getListView().getItems().add(
                    u.getuserID() + " - " + u.getname() + " (" + u.getrole() + ")"
            );
        }
    }

    public UserView getView() {
        return view;
    }

    public List<User> getUsers() {
        return users;
    }
}

