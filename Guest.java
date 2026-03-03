package com.useraddition.usermanagement;


public class Guest extends User {

    public Guest(int id, String name, String email){
        super(id, name, email);
    }

    public String getrole(){
        return "Guest";
    }
}