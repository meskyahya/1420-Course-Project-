package com.useraddition.usermanagement;

public class Student extends User {

    public Student(int id, String name, String email){
        super(id, name, email);
    }

    public String getrole(){
        return "Student";
    }
}

