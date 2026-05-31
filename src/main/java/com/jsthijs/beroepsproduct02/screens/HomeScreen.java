package com.jsthijs.beroepsproduct02.screens;

// Startscherm met nieuwe items per categorie.

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

public class HomeScreen implements Screen {
    private final Scene scene;

    public HomeScreen() {
        // Root container voor de pagina.
        VBox root = new VBox();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        // Stylesheet toepassen.
        ApplyStylesheet(this.scene);

        // Container voor item lijsten.
        VBox items = new VBox();
        items.setPadding(new Insets(16, 120, 16, 120));
        items.setSpacing(8);

        // Scroll-bare container voor de lijsten.
        ScrollPane scrollPane = new ScrollPane(items);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        // Header en content toevoegen.
        root.getChildren().addAll(header, scrollPane);

        // Titel voor nieuwe boeken.
        FlowPane newBooks = new FlowPane(new Text("Nieuw toegevoegde boeken"));
        newBooks.setPrefHeight(32);
        newBooks.getStyleClass().add("h2");

        items.getChildren().addAll(newBooks, itemList("boek", 6));

        // Titel voor nieuwe films.
        FlowPane newFilms = new FlowPane(new Text("Nieuw toegevoegde films"));
        newFilms.setPrefHeight(32);
        newFilms.getStyleClass().add("h2");

        items.getChildren().addAll(newFilms, itemList("film", 6));


    }

    // Haalt items op en bouwt de view.
    private Pane itemList(String type, Integer limit) {
        // Flow-pane voor de items.
        FlowPane itemList = new FlowPane();
        itemList.setMinHeight(300);
        itemList.setHgap(10);
        itemList.setVgap(10);

        // Items uit de database halen.
        ResultSet rs = db.getItems(type, limit);
        try {
            // Voor elke rij een item renderen.
            while (rs.next()) { itemList.getChildren().add(new Item(rs).renderItem()); }
        } catch (SQLException e) { throw new RuntimeException(e); }

        return itemList;
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Home Screen";
    }

}
