package com.campusbooking.models;

public class Guest extends User{
    public Guest(String name, String email, int userId) {
        super(name, email, userId, "guest");
    }


}

