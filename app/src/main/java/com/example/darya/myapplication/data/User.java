package com.example.darya.myapplication.data;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;

    public User(int id, String firstName, String lastName, String email, String phone){
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
    }

    public String getFirstName(){
        return this.firstName;
    }
}
