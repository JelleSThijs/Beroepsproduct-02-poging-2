package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

public class HomeScreen implements Screen {
    private final Scene scene;

    public HomeScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);

        VBox items = new VBox();
        items.setPadding(new Insets(0, 120, 0, 120));
        items.setSpacing(8);

        root.getChildren().addAll(header, items);



        FlowPane newBooks = new FlowPane(new Text("Nieuw toegevoegde boeken"));
        newBooks.setPrefSize(1200, 32);
        newBooks.setStyle("-fx-background-color: green");

        items.getChildren().addAll(newBooks, itemList("boek", 6));

        FlowPane newFilms = new FlowPane(new Text("Nieuw toegevoegde films"));
        newFilms.setPrefSize(1200, 32);
        newFilms.setStyle("-fx-background-color: green");

        items.getChildren().addAll(newFilms, itemList("film", 6));


    }

    private Pane itemList(String type, Integer limit) {
        FlowPane itemList = new FlowPane();
        itemList.setMinSize(1200, 300);
        itemList.setPrefSize(1200, 300);
        itemList.setHgap(10);
        itemList.setVgap(10);

        ResultSet rs = db.getItems(type, limit);
        try {
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
