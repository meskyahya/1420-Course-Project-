// main package which groups all the model classes together)
package com.campusbooking.models;

// creating a class for the user object 
public class User {

    // creating a field to store the user name
    private String name;

    // creating a field to store the user email
    private String email;

    // creating a field to store userID
    private String userId;

    // creating a field to store the role
    private String role;

    // creating a constructor which runs if the user object is created and the parameters are the input values that is used to set fields
    public User(String name, String email, String userId, String role) {

        // sets name field
        this.name = name;

        // sets email field
        this.email = email;

        // sets userId field
        this.userId = userId;

        // sets role field
        this.role = role;
    }

    // getter and setters

    // creating a method to return name
    public String getName() { 
        return name; }
    
    // creating a method to update name
    public void setName(String name) { 
        this.name = name; }

   // creating a method to return email
    public String getEmail() {
        return email; }
    
    // creating a method to update email
    public void setEmail(String email) { 
        this.email = email; }

    // creating a method to userId
    public String getuserID() { 
        return userId; }
    
    // creating a method to update userID
    public void setuserId(String userId) { 
        this.userId = userId; }

    // creating a method to return role    
    public String getrole() { 
        return role; }
    
    // creating a method to update role
    public void setrole(String role) { 
        this.role = role; }
}
