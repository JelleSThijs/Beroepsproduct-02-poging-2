package com.jsthijs.beroepsproduct02.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.db;

public class User {
    private int id;
    private String username;
    private String password;
    private String name;
    private String email;
    private String phoneNumber;
    private String city;
    private int isAdmin = 0;

    public User(String username, String password, String name, String email, String phoneNumber, String city) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email; // deze velden zijn optioneel
        this.phoneNumber = phoneNumber; // deze velden zijn optioneel
        this.city = city; // deze velden zijn optioneel
    }

    public User(ResultSet rs) throws SQLException {
        this.id = rs.getInt("id");
        this.username = rs.getString("username");
        this.password = rs.getString("password");
        this.name = rs.getString("name");
        this.email = rs.getString("email");
        this.phoneNumber = rs.getString("phoneNumber");
        this.city = rs.getString("city");
    }

    public void deleteItem(Item item) {
        if (this.id == item.getUserId()) {

        }
    }


    public int getId() {
        return this.id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getCity() {
        return city;
    }

    public int getIsAdmin() {
        return isAdmin;
    }
}
