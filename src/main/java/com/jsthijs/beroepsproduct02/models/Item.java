package com.jsthijs.beroepsproduct02.models;

import static com.jsthijs.beroepsproduct02.Application.*;

import com.jsthijs.beroepsproduct02.screens.ItemScreen;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    public Pane renderItem() {
        FlowPane itemPane = new FlowPane();
        itemPane.setMinWidth(144);
        itemPane.setPrefWidth(144);
        itemPane.setMaxWidth(144);
        itemPane.setHgap(14);
        itemPane.setVgap(4);
        itemPane.setId("item-small");

        // probeerd een foto op te halen anders wordt er een grijs vlak gerendered
        try {
            ImageView itemImg = new ImageView(this.getImage());
            itemImg.setFitHeight(196);
            itemImg.setFitWidth(144);
            itemPane.getChildren().add(itemImg);
        } catch (Exception e) {
            Region itemImg = new Region();
            itemImg.setPrefHeight(196);
            itemImg.setPrefWidth(144);
            itemImg.setStyle("-fx-background-color: lightgray;");
            itemPane.getChildren().add(itemImg);
        }

        FlowPane itemTitle = new FlowPane(new Text(this.getName()));
        itemTitle.setPrefWidth(144);
        itemPane.getChildren().add(itemTitle);

        renderTags().forEach(tag -> {
            itemPane.getChildren().add(tag);
        });

        itemPane.setOnMouseClicked(e -> {
            // navigeer naar de detailscherm van dit item
            NavigateTo(new ItemScreen(this));
        });

        return itemPane;
    }

    public ArrayList<Label> renderTags() {
        ArrayList<Label> tags = new ArrayList<>();

        Label typeText = new Label(this.getType());
        typeText.getStyleClass().add("tag");
        typeText.setStyle("-fx-background-color: lightblue; -fx-border-color: blue");
        tags.add(typeText);

        Label makerText = new Label(this.getMaker());
        makerText.getStyleClass().add("tag");
        makerText.setStyle("-fx-background-color: lightpink; -fx-border-color: purple");
        tags.add(makerText);

        Label releaseYearText = new Label(this.getReleaseYear().toString());
        releaseYearText.getStyleClass().add("tag");
        releaseYearText.setStyle("-fx-background-color: lightyellow; -fx-border-color: yellow");
        tags.add(releaseYearText);

        this.getTags().forEach(tag -> {
            Label tagText = new Label(tag);
            tagText.getStyleClass().add("tag");
            tagText.setStyle("-fx-background-color: lightcoral;  -fx-border-color: red");
            tags.add(tagText);
        });

        return tags;
    }

    public void setData(String name, String summary, String image, String maker, Integer releaseYear, String type, int userId, ArrayList<String> tags) {
        this.name = name;
        this.summary = summary;
        this.image = image;
        this.maker = maker;
        this.releaseYear = releaseYear;
        this.type = type;
        this.userId = userId;
        this.tags = tags;
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
