package com.jsthijs.beroepsproduct02.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.db;
import static com.jsthijs.beroepsproduct02.Application.user;

// Model voor een gebruiker en basisacties.
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

    // Maakt een user op basis van een database-resultaat.
    public User(ResultSet rs) throws SQLException {
            // Velden uitlezen uit het resultaat.
            id = rs.getInt("id");
            username = rs.getString("username");
            password = rs.getString("password");
            name = rs.getString("name");
            email = rs.getString("email");
            phoneNumber = rs.getString("phoneNumber");
            city = rs.getString("city");
            isAdmin = rs.getInt("isAdmin");
    }

    // Verwijdert een item als de gebruiker rechten heeft.
    public void deleteItem(Item item) {
        // Alleen eigenaar of admin mag verwijderen.
        if (this.id == item.getUserId() || this.isAdmin == 1) {
            db.deleteItem(item.getId());
        }
    }

    // Alleen admins mogen een gebruiker verwijderen.
    public void deleteUser(int userId) {
        // Admin-check voor verwijdering.
        if (this.isAdmin == 1) {
            db.deleteUser(userId);
        }
    }

    public int getId() {
        // ID teruggeven.
        return this.id;
    }

    public String getUsername() {
        // Username teruggeven.
        return username;
    }

    public String getPassword() {
        // Password teruggeven.
        return password;
    }

    public String getName() {
        // Naam teruggeven.
        return name;
    }

    public String getEmail() {
        // Email teruggeven.
        return email;
    }

    public String getPhoneNumber() {
        // Telefoonnummer teruggeven.
        return phoneNumber;
    }

    public String getCity() {
        // Woonplaats teruggeven.
        return city;
    }

    public int getIsAdmin() {
        // Admin status teruggeven.
        return isAdmin;
    }
}
