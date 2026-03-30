// main package which groups all the model classes together)
package com.campusbooking.models;

// creating a class for guest user and inherits from User
public class Guest extends User{

    // creating a constructor that runs when a Guest object is created and the parameters are input values for name, email, and user ID
    public Guest(String name, String email, String userId) {

    // calls the parent User constructor and sets role to Guest
    super(name, email, userId, "guest");
}

}

