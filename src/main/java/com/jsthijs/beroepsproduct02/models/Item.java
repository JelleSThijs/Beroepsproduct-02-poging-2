package com.jsthijs.beroepsproduct02.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.db;
import static com.jsthijs.beroepsproduct02.Application.dbTags;

public class Item {
    private Integer id;
    private String name;
    private String summary;
    private String image;
    private String maker;
    private Integer releaseYear;
    private String type;
    private int userId;
    private ArrayList<String> tags;

    public Item(String name, String summary, String image, String maker, Integer releaseYear, String type, int userId, ArrayList<String> tags) {
        this.name = name;
        this.summary = summary;
        this.image = image;
        this.maker = maker;
        this.releaseYear = releaseYear;
        this.type = type;
        this.userId = userId;
        this.tags = tags;
    }

    // maak een nieuw item class aan vanuit een bestaand item uit de database
    public Item(ResultSet rs) throws SQLException {
        try {
            // itemgegevens zetten
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.summary = rs.getString("summary");
            this.image = rs.getString("image");
            this.maker = rs.getString("maker");
            this.releaseYear = rs.getInt("releaseYear");
            this.type = rs.getString("type");
            this.userId = rs.getInt("userId");

            // item tags ophalen
            this.tags = new ArrayList<>();
            ResultSet tagRs = db.getItemTags(this.id);
            while (tagRs.next()) {
                this.tags.add(dbTags.getNameById(tagRs.getInt("tagId")));
            }

        } catch (Exception e) { throw new SQLException(e); }

    }

    public void setId(Integer id) {
        this.id = id;
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

    public ArrayList<String> getTags() {
        return tags;
    }
}
