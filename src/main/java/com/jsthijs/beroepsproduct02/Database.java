package com.jsthijs.beroepsproduct02;

import com.jsthijs.beroepsproduct02.models.Item;
import com.jsthijs.beroepsproduct02.models.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.jsthijs.beroepsproduct02.Application.dbTags;

public class Database {

    private String host;
    private String port;
    private String user;
    private String passwd;
    private String dbname;

    private Connection conn;
    private Statement stmt;

    public Database(String host, String port, String user, String passwd, String dbname) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.passwd = passwd;
        this.dbname = dbname;

        try {
            this.conn = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + dbname , user, passwd);
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

    // item logica
    public void addItem(Item item) {
        try {
            // Prepare statement om fouten en sql injection tegen te gaan
            PreparedStatement ps = this.conn.prepareStatement("INSERT INTO items VALUES (0, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, item.getName());
            ps.setString(2, item.getSummary());
            ps.setString(3, item.getImage());
            ps.setString(4, item.getMaker());
            ps.setInt(5, item.getReleaseYear());
            ps.setString(6, item.getType());
            ps.setInt(7, item.getUserId());
            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        // haalt het id op van het net toegevoegde regel en zet het in de item class
        try {
            ResultSet rs = this.stmt.executeQuery("SELECT id FROM items WHERE userId = " + item.getUserId() + " ORDER BY id DESC LIMIT 1");
            if (rs.next()) { item.setId(rs.getInt("id")); }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public void setItemTags(int itemId, ArrayList<String> tagNames) {
        // verwijderd bestaande tags van een item
        try {
            PreparedStatement ps1 = this.conn.prepareStatement("DELETE FROM itemTags WHERE itemId = ?");
            ps1.setInt(1, itemId);
            ps1.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }

        // voegt tags toe aan een item
        tagNames.forEach(tagName -> {
            try {
                PreparedStatement ps = this.conn.prepareStatement("INSERT INTO itemTags VALUES (?, ?)");
                ps.setInt(1, itemId);
                ps.setInt(2, dbTags.getIdByName(tagName));
                ps.executeUpdate();

            } catch (SQLException e) { throw new RuntimeException(e); }
        });
    }

    public ResultSet getItem(int id) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM items WHERE id = ?");
            ps.setInt(1, id);
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getItems(String type, int limit) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM items WHERE type = ? ORDER BY id DESC LIMIT ?");
            ps.setString(1, type);
            ps.setInt(2, limit);
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getUserItems(int userId) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM items WHERE userid = ?");
            ps.setInt(1, userId);
            return ps.executeQuery();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet getItemTags(int itemId) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM itemTags WHERE itemId = ?");
            ps.setInt(1, itemId);
            return ps.executeQuery();
        } catch (Exception e) { throw new RuntimeException(e); }
    }

    // user logica
    public void addUser(User user) {
        try {
            // Prepare statement om fouten en sql injection tegen te gaan
            PreparedStatement ps = this.conn.prepareStatement("INSERT INTO users VALUES (0, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setString(3, user.getName());

            // zet de kolom naar null als de gebruiker geen gegevens heeft ingevoerd
            ps.setString(4, user.getEmail() != null ? user.getEmail() : null);
            ps.setString(5, user.getPhoneNumber() != null ? user.getPhoneNumber() : null);
            ps.setString(6, user.getCity() != null ? user.getCity() : null);

            ps.setInt(7, user.getIsAdmin());

            ps.executeUpdate();
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public ResultSet loginUser(String username, String password) {
        try {
            PreparedStatement ps = this.conn.prepareStatement("SELECT * FROM users WHERE username = ? AND password = ?");
            ps.setString(1, username);
            ps.setString(2, password);
            return ps.executeQuery();

        } catch (SQLException e) { throw new RuntimeException(e); }

    }

    public ResultSet getTags() {
        // haalt alle tags op uit de database
        try {
            ResultSet rs  = this.stmt.executeQuery("SELECT id, name FROM tags");
            return rs;
        } catch (SQLException e) { throw new RuntimeException(e); }

    }

    public ResultSet executeQuery(String query) throws SQLException {
        ResultSet rs = stmt.executeQuery(query);
        return rs;
    }

    public void executeUpdate(String query) throws SQLException {
        stmt.executeUpdate(query);
    }
}


