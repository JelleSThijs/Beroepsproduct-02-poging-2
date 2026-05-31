package com.jsthijs.beroepsproduct02.models;

import static com.jsthijs.beroepsproduct02.Application.*;

import com.jsthijs.beroepsproduct02.screens.ItemScreen;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

// Model voor een item met render-logica voor de UI.
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

    // Maakt een item aan vanuit een bestaand item uit de database.
    public Item(ResultSet rs) throws SQLException {
        try {
            // Itemgegevens lezen uit de database.
            this.id = rs.getInt("id");
            this.name = rs.getString("name");
            this.summary = rs.getString("summary");
            this.image = rs.getString("image");
            this.maker = rs.getString("maker");
            this.releaseYear = rs.getInt("releaseYear");
            this.type = rs.getString("type");
            this.userId = rs.getInt("userId");

            // Item tags ophalen en vullen.
            this.tags = new ArrayList<>();
            ResultSet tagRs = db.getItemTags(this.id);
            while (tagRs.next()) {
                this.tags.add(dbTags.getNameById(tagRs.getInt("tagId")));
            }

        } catch (Exception e) { throw new SQLException(e); }
    }

    // Bouwt de kaart voor weergave in lijsten.
    public Pane renderItem() {
        // Container voor de itemkaart.
        FlowPane itemPane = new FlowPane();
        itemPane.setMinWidth(144);
        itemPane.setPrefWidth(144);
        itemPane.setMaxWidth(144);
        itemPane.setHgap(14);
        itemPane.setVgap(4);
        itemPane.setId("item-small");

        // Stack voor afbeelding en tag.
        StackPane itemStack = new StackPane();
        itemStack.setPrefSize(144, 196);
        itemStack.setAlignment(Pos.BOTTOM_RIGHT);
        itemPane.getChildren().add(itemStack);

        // Probeert een foto op te halen, anders wordt er een grijs vlak gerenderd.
        try {
            ImageView itemImg = new ImageView(this.getImage());
            itemImg.setFitWidth(144);
            itemImg.setFitHeight(196);

            // Afronding met clip.
            Rectangle clip = new Rectangle(144, 196);
            clip.setArcWidth(40);
            clip.setArcHeight(40);
            itemImg.setClip(clip);

            itemStack.getChildren().add(itemImg);
        } catch (Exception e) {
            // Fallback placeholder als afbeelding niet kan laden.
            Region itemImg = new Region();
            itemImg.setPrefSize(144, 196);
            itemImg.setStyle("-fx-background-color: lightgray; -fx-border-radius: 20; -fx-background-radius: 20");
            itemImg.getStyleClass().add("img");
            itemStack.getChildren().add(itemImg);
        }

        // Titelblok toevoegen.
        FlowPane itemTitle = new FlowPane(new Text(this.getName()));
        itemTitle.setPrefWidth(144);
        itemTitle.setAlignment(Pos.CENTER_LEFT);
        itemTitle.getStyleClass().addAll("h3", "txtfield");
        itemPane.getChildren().add(itemTitle);

        // Tags toevoegen; typeTag gaat in de stack.
        renderTags().forEach(tag -> {
            if (tag.getId() != null && tag.getId().equals("typeText")) {
                tag.setStyle("-fx-background-color: -color-licht; -fx-border-color: -color-schaduw");
                itemStack.getChildren().add(tag);
            } else { itemPane.getChildren().add(tag); }
        });

        // Klik opent de detailpagina.
        itemPane.setOnMouseClicked(e -> {
            NavigateTo(new ItemScreen(this));
        });

        return itemPane;
    }

    // Maakt label-tags aan met styling en wrapping.
    public ArrayList<Label> renderTags() {
        ArrayList<Label> tags = new ArrayList<>();

        // Type-tag (badge op de afbeelding).
        Label typeText = new Label(this.getType());
        typeText.getStyleClass().add("tag");
        typeText.setId("typeText");
        typeText.setStyle("-fx-background-color: lightblue; -fx-border-color: blue");
        tags.add(typeText);

        // Maker-tag.
        Label makerText = new Label(this.getMaker());
        makerText.getStyleClass().add("tag");
        makerText.setStyle("-fx-background-color: lightpink; -fx-border-color: purple");
        makerText.setMaxWidth(144);
        makerText.setWrapText(true);
        tags.add(makerText);

        // Jaar-tag.
        Label releaseYearText = new Label(this.getReleaseYear().toString());
        releaseYearText.getStyleClass().add("tag");
        releaseYearText.setStyle("-fx-background-color: lightyellow; -fx-border-color: yellow");
        releaseYearText.setMaxWidth(144);
        releaseYearText.setWrapText(true);
        tags.add(releaseYearText);

        // Extra tags uit de lijst.
        this.getTags().forEach(tag -> {
            Label tagText = new Label(tag);
            tagText.getStyleClass().add("tag");
            tagText.setStyle("-fx-background-color: lightsalmon;  -fx-border-color: red");
            tagText.setMaxWidth(144);
            tagText.setWrapText(true);
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
