package com.jsthijs.beroepsproduct02.models;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.db;

// Model voor tags en tag-gerelateerde helpers.
public class Tags {
    private ArrayList<Integer> id;
    private ArrayList<String> name;

    // Laadt tags uit de database en cached ze lokaal.
    public Tags () {
        // Lokale lijsten initialiseren.
        this.id = new ArrayList<>();
        this.name = new ArrayList<>();

        // Resultset ophalen.
        ResultSet rs = db.getTags();
        try {
            // Alle rijen inlezen.
            while (rs.next()) {
                this.id.add(rs.getInt("id"));
                this.name.add(rs.getString("name"));
            }
        } catch (SQLException e) { throw new RuntimeException(e); }
    }

    public String getNameById(int id) {
        // Index opzoeken voor het ID.
        int indexNum = this.id.indexOf(id);
        // Naam teruggeven.
        return name.get(indexNum);
    }

    public int getIdByName(String name) {
        // Index opzoeken voor de naam.
        int indexNum = this.name.indexOf(name);
        // ID teruggeven.
        return id.get(indexNum);
    }

    public ArrayList<String> getName() {
        // Lijst met tag-namen retourneren.
        return name;
    }
}
