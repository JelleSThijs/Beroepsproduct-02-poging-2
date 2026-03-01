package com.jsthijs.beroepsproduct02.models;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Item {
    private Integer id;
    private String name;
    private String summary;
    private String image;
    private String maker;
    private Integer releaseYear;
    private String type;
    private int userId;

    public Item(String name, String summary, String image, String maker, Integer releaseYear, String type, int userId) {
        this.name = name;
        this.summary = summary;
        this.image = image;
        this.maker = maker;
        this.releaseYear = releaseYear;
        this.type = type;
        this.userId = userId;
    }

    public Item(ResultSet rs) throws SQLException {
        this.name = rs.getString("name");
        this.summary = rs.getString("summary");
        this.image = rs.getString("image");
        this.maker = rs.getString("maker");
        this.releaseYear = rs.getInt("releaseYear");
        this.type = rs.getString("type");
        this.userId = rs.getInt("userId");
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getSummary() {
        return summary;
    }

    public String getImage() {
        return image;
    }

    public Integer getReleaseYear() {
        return releaseYear;
    }

    public String getMaker() {
        return maker;
    }

    public String getType() {
        return type;
    }

    public int getUserId() {
        return userId;
    }
}
