package com.example.lab2_mobiledevelopment.model;

public class User {
    private String id;
    private String Firstname;
    private String Lastname;
    private String username;
    private String imageURL;

    public User(String id, String Firstname, String Lastname) {
        this.id = id;
        this.Firstname = Firstname;
        this.Lastname = Lastname;
        this.imageURL = imageURL;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirstname() {
        return Firstname;
    }

    public void setFirstname(String Firstname) {
        this.Firstname = Firstname;
    }

    public String getLastname() {
        return Lastname;
    }

    public void setLastname(String Lastname) {
        this.Lastname = Lastname;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }
}
