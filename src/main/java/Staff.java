package com.useraddition.usermanagement;

public class Staff extends User {

    public Staff(int id, String name, String email){
        super(id, name, email);
    }

    public String getrole(){
        return "Staff";
    }
}


