package com.jsthijs.beroepsproduct02.screens;

import static com.jsthijs.beroepsproduct02.Application.*;

import com.jsthijs.beroepsproduct02.Application;
import com.jsthijs.beroepsproduct02.models.Item;
import com.mysql.cj.protocol.Resultset;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.Flow;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ItemScreen implements Screen {
    private final Item item;
    private final Scene scene;

    public ItemScreen(Item item) {
        this.item = item;

        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(70);

        HBox itemBox = new HBox();
        itemBox.setPrefSize(1160, 550);
        itemBox.setSpacing(48);

        itemBox.setStyle("-fx-border-color: red; -fx-border-width: 1px;");


        ImageView itemImg = new ImageView(item.getImage());
        itemImg.setFitWidth(400);
        itemImg.setFitHeight(550);

        VBox itemText = new VBox();
        itemText.setSpacing(24);
        itemText.setStyle("-fx-border-color: green; -fx-border-width: 1px;");

        FlowPane itemTitle = new FlowPane(new Text(item.getName()));
        itemTitle.setPrefHeight(48);
        itemTitle.setAlignment(Pos.CENTER_LEFT);
        itemTitle.setStyle("-fx-border-color: orange; -fx-border-width: 1px;");

        HBox itemTags = renderTags();
        itemTags.setStyle("-fx-border-color: blue; -fx-border-width: 1px;");
        itemTags.setAlignment(Pos.CENTER_LEFT);
        itemTags.setPrefHeight(32);

        FlowPane itemDescription = new FlowPane(new Text(item.getSummary()));
        itemDescription.setPrefHeight(240);
        itemDescription.setStyle("-fx-border-color: purple; -fx-border-width: 1px;");

        FlowPane itemOwnerPane = new FlowPane();
        itemOwnerPane.setPrefHeight(48);
        itemOwnerPane.setAlignment(Pos.CENTER_LEFT);
        itemOwnerPane.setStyle("-fx-border-color: pink; -fx-border-width: 1px;");

        HBox contactBox = new HBox();
        contactBox.setPrefSize(712, 48);
        contactBox.setAlignment(Pos.CENTER);
        contactBox.setSpacing(14);
        contactBox.setStyle("-fx-border-color: brown; -fx-border-width: 1px;");

        ImageView contactIcon = new ImageView(Application.class.getResource("icons/letter-48.png").toString());

        Button contactButton = new Button("Contact");

        String itemOwnerPhonenumber = "";
        String itemOwnerEmail = "";

        try {
            ResultSet rs = db.getItemOwnerDetails(item.getUserId());
            if (rs.next()) {
                itemOwnerPane.getChildren().add(
                  new Text(
                    rs.getString("name") +
                    " In " +
                    rs.getString("city")
                  )
                );
                itemOwnerPhonenumber = rs.getString("phonenumber");
                itemOwnerEmail = rs.getString("email");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        contactBox.getChildren().addAll(contactIcon, contactButton);
        itemText.getChildren().addAll(itemTitle, itemTags, itemDescription, itemOwnerPane, contactBox);
        itemBox.getChildren().addAll(itemImg, itemText);
        root.getChildren().addAll(header, itemBox);
    }

    private HBox renderTags() {
        HBox tagList = new HBox();
        tagList.setAlignment(Pos.CENTER);
        tagList.setSpacing(10);

        Text typeText = new Text(item.getType());
        Text makerText = new Text(item.getMaker());
        Text releaseYearText = new Text(item.getReleaseYear().toString());
        tagList.getChildren().addAll(typeText, makerText, releaseYearText);

        item.getTags().forEach(tag -> {
            Text tagText = new Text(tag);
            tagText.setStyle("-fx-background-color: lightgray; -fx-padding: 5px;");
            tagList.getChildren().add(tagText);
        });

        return tagList;
    }


    public Scene getScene() {
        return this.scene;
    }
    public String getTitle() {
        return item.getName();
    }

}
