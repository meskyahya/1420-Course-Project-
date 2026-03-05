package com.useraddition.usermanagement;

public class User {

    private int id;
    private String name;
    private String email;

    public User(int id, String name, String email){
        this.id = id;
        this.name = name;
        this.email = email;
    }
    public int getId(){
        return id;}
    public void serId(int id){
        this.id = id;}
    public String getname(){
        return name;}
    public void setname( String name){
        this.name = name;}
    public String getemail(){
        return email;}
    public void setemail(String email){
        this.email = email; }

    public String getrole(){
        return "User";
    }

    public String toString(){
        return "|" + id + "|" + name + "|" + email + "|" + getrole();}


}


