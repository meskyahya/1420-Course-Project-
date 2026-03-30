// main package which groups all the model classes together)
package com.campusbooking.models;

// creating a class for a student user and inherits from User
public class Student extends User {

    // creating a constructor that runs when a Student object is created and the parameters are input values for name, email, and user ID
    public Student(String name, String email, String userId) {

    // calls the parent User constructor and sets role to Student
    super(name, email, userId, "Student");
    }
}

