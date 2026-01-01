package com.example.chatapp.model;

public class User {

    // data that will received
    // variables name must be the same as a key in ths DB
         String id;
         String username;
         String email;
         String status;
         String password;
         String imageUrl; // for userAdapter

         // constructors
    public User()
    {
    }
    public User(String id, String username, String email, String status, String password, String imageUrl) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.password = password;

        this.imageUrl = imageUrl;
    }

    // Setter ans Getter methods

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    //

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
