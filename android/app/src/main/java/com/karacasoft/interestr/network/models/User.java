package com.karacasoft.interestr.network.models;

/**
 * Created by karacasoft on 13/11/17.
 */

public class User {

    private int id;
    private String username;
    private String password;
    private String email;

    public User() {}

    public User(String username) {
        this.username = username;
    }
    public User(String username,String password) {
        this.username = username;
        this.password = password;
    }

    public User(String username,String password,String email) {
        this.username = username;
        this.password = password;
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword(){return password;}

    public void setPassword(String password){this.password=password;}

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email= email;
    }

}
