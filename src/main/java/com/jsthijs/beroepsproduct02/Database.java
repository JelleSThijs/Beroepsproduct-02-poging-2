package com.jsthijs.beroepsproduct02;

import com.jsthijs.beroepsproduct02.models.Item;
import com.jsthijs.beroepsproduct02.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

import static com.jsthijs.beroepsproduct02.Application.dbTags;

// Database-toegang en query helper voor het project.
public class Database {

    private String host;
    private String port;
    private String user;
    private String passwd;
    private String dbname;

    private Connection conn;
    private Statement stmt;

    public Database(String host, String port, String user, String passwd, String dbname) {
        // Basis configuratie bewaren.
        this.host = host;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
        this.dbname = dbname;

        try {
            // Verbind met de database.
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname, user, passwd);
            // Maak een statement voor eenvoudige queries.
            this.stmt = this.conn.createStatement();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Database(String host, String user, String passwd, String dbname) {
        this(host, "3306", user, passwd, dbname);
    }

    public Database(String host, String user, String dbname) {
        this(host, "3306", user, "", dbname);
    }

    // Item-logica.
    // Voegt een nieuw item toe en zet het ID op het model.
    public void addItem(Item item) {
        try {
            // Prepare statement om fouten en sql injection tegen te gaan
            PreparedStatement ps = this.conn.prepareStatement("INSERT INTO items VALUES (0, ?, ?, ?, ?, ?, ?, ?)");
            // Velden invullen.
            ps.setString(1, item.getName());
            ps.setString(2, item.getSummary());
            ps.setString(3, item.getImage());
            ps.setString(4, item.getMaker());
            ps.setInt(5, item.getReleaseYear());
            ps.setString(6, item.getType());
            ps.setInt(7, item.getUserId());
            // Uitvoeren.
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Slaat gewijzigde itemgegevens op.
    public void updateItem(Item item) {
        try {
            // Prepare statement om fouten en sql injection tegen te gaan
            PreparedStatement ps = this.conn.prepareStatement("UPDATE items SET name = ?, summary = ?, image = ?, maker = ?, releaseYear = ?, type = ? WHERE id = ?");
            // Nieuwe waarden zetten.
            ps.setString(1, item.getName());
            ps.setString(2, item.getSummary());
            ps.setString(3, item.getImage());
            ps.setString(4, item.getMaker());
            ps.setInt(5, item.getReleaseYear());
            ps.setString(6, item.getType());
            ps.setInt(7, item.getId());
            // Update uitvoeren.
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public ResultSet getItemOwnerDetails(Integer userId) {
        try {
            // Eigenaar op basis van userId ophalen.
            PreparedStatement ps = this.conn.prepareStatement("SELECT name, email, phonenumber, city FROM users WHERE id = ?");
            // Parameter zetten.
            ps.setInt(1, userId);
            // Resultaat teruggeven.
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void deleteItem(int itemId) {
        try {
            // Verwijder item op ID.
            PreparedStatement ps = this.conn.prepareStatement("DELETE FROM items WHERE id = ?");
            ps.setInt(1, itemId);
            // Uitvoeren.
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }



    public void setItemTags(int itemId, ArrayList<String> tagNames) {
        // Verwijdert bestaande tags van een item.
        try {
            // Eerst alle huidige tags weggooien.
            PreparedStatement ps1 = this.conn.prepareStatement("DELETE FROM itemTags WHERE itemId = ?");
            ps1.setInt(1, itemId);
            ps1.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        // Voegt tags toe aan een item.
        tagNames.forEach(tagName -> {
            try {
                // Nieuwe tagkoppeling toevoegen.
                PreparedStatement ps = this.conn.prepareStatement("INSERT INTO itemTags VALUES (?, ?)");
                ps.setInt(1, itemId);
                ps.setInt(2, dbTags.getIdByName(tagName));
                ps.executeUpdate();

            } catch (SQLException e) { throw new RuntimeException(e); }
        });
    }

    public ResultSet getItem(int id) {
        try {
            // Specifiek item ophalen.
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM items WHERE id = ?");
            ps.setInt(1, id);
            // Resultaat teruggeven.
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getItems(String type, int limit) {
        try {
            // Items per type met limiet ophalen.
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM items WHERE type = ? ORDER BY id DESC LIMIT ?");
            ps.setString(1, type);
            ps.setInt(2, limit);
            // Resultaat teruggeven.
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getItemsByUser(String username) {
        try {
            // Items van een gebruiker ophalen.
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM items WHERE userId = ?");
            ps.setString(1, username);
            // Resultaat teruggeven.
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getSearchResults(String searchText, String tag, String releaseYear, String type) {
        try {
            // Start index voor extra parameters.
            int i = 2;

            // Basis query opbouwen.
            String query = "SELECT * FROM items WHERE name LIKE ? ";
            if (!Objects.equals(tag, "Genre")) { query += "AND id IN (SELECT itemId FROM itemTags WHERE tagId = ?) "; }
            if (!Objects.equals(releaseYear, "")) { query += "AND releaseYear = ? "; }
            if (!Objects.equals(type, "Type")) { query += "AND type = ?"; }

            // Query voorbereiden.
            PreparedStatement ps = this.conn.prepareStatement(query);
            // Zoektekst zetten.
            ps.setString(1, "%" + searchText + "%");
            if (!Objects.equals(tag, "Genre")) { ps.setInt(i, dbTags.getIdByName(tag)); i ++; }
            if (!Objects.equals(releaseYear, "")) { ps.setInt(i, Integer.parseInt(releaseYear)); i++; }
            if (!Objects.equals(type, "Type")) { ps.setString(i, type); i++; }

            // Debug output van de query.
            System.out.println(ps);
            // Resultaat teruggeven.
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }


    }

    public ResultSet getItemTags(int itemId) {
        try {
            // Tags voor een item ophalen.
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM itemTags WHERE itemId = ?");
            ps.setInt(1, itemId);
            // Resultaat teruggeven.
            return ps.executeQuery();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    // User-logica.
    // Registreert een gebruiker als de gebruikersnaam nog vrij is.
    public Boolean addUser(User user) {
        boolean userExists = false;
        // Controleren op al bestaande gebruiker.
        try {
            // Controleren op bestaande gebruikersnaam.
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM users WHERE username = ?");
            ps.setString(1, user.getUsername());
            if (ps.executeQuery().next()) { userExists = true; }
        } catch (SQLException e) { throw new RuntimeException(e); }

        if (!userExists) {
            // Gebruiker aanmaken in de database.
            try {
                // Prepare statement om fouten en sql injection tegen te gaan
                PreparedStatement ps = this.conn.prepareStatement("INSERT INTO users VALUES (0, ?, ?, ?, ?, ?, ?, ?)");
                // Basisgegevens.
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getPassword());
                ps.setString(3, user.getName());

                // Zet de kolom naar null als de gebruiker geen gegevens heeft ingevoerd.
                ps.setString(4, user.getEmail() != null ? user.getEmail() : null);
                ps.setString(5, user.getPhoneNumber() != null ? user.getPhoneNumber() : null);
                ps.setString(6, user.getCity() != null ? user.getCity() : null);

                // Admin-vlag.
                ps.setInt(7, user.getIsAdmin());

                // Uitvoeren en status teruggeven.
                ps.executeUpdate();
                userExists = true;
            } catch (SQLException e) { throw new RuntimeException(e); }
        }
        return userExists;
    }

    // Logt een gebruiker in en geeft het resultaat terug.
    public ResultSet loginUser(String username, String password) {
        try {
            // Gebruiker op basis van credentials ophalen.
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            // Resultaat teruggeven.
            return ps.executeQuery();

        } catch (SQLException e) { throw new RuntimeException(e); }

    }

    public void deleteUser(int userId) {
        try {
            // Gebruiker verwijderen op ID.
            PreparedStatement ps = this.conn.prepareStatement("DELETE FROM users WHERE id = ?");
            ps.setInt(1, userId);
            // Uitvoeren.
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getTags() {
        // Haalt alle tags op uit de database.
        try {
            // Query uitvoeren.
            ResultSet rs = this.stmt.executeQuery("SELECT id, name FROM tags");
            // Resultaat teruggeven.
            return rs;
        } catch (SQLException e) { throw new RuntimeException(e); }

    }
}


