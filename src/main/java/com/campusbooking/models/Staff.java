// main package that groups all the model classes
package com.campusbooking.models;

// creating a class for staff user and inherits from User
public class Staff extends User {

    // creating a constructor that runs when a Staff object is created and the parameters are input values for name, email, and user ID
    public Staff(String name, String email, String userId) {

    // calling the parent User constructor and sets role to Staff
    super(name, email, userId, "Staff");
    }
}


