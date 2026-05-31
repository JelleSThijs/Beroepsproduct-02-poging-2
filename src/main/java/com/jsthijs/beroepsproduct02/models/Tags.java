package com.jsthijs.beroepsproduct02.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.db;

// Model voor tags en tag-gerelateerde helpers.
public class Tags {
    private ArrayList<Integer> id;
    private ArrayList<String> name;

    public Tags () {
        this.id = new ArrayList<>();
        this.name = new ArrayList<>();

        ResultSet rs = db.getTags();
        try {
            while (rs.next()) {
                this.id.add(rs.getInt("id"));
                this.name.add(rs.getString("name"));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    // Constructor voor testen.
    public Tags(ArrayList<Integer> ids, ArrayList<String> names) {
        this.id = ids;
        this.name = names;
    }

    public String getNameById(int id) {
        int indexNum = this.id.indexOf(id);
        return name.get(indexNum);
    }

    public int getIdByName(String name) {
        int indexNum = this.name.indexOf(name);
        return id.get(indexNum);
    }

    public ArrayList<String> getName() {
        return name;
    }
}
