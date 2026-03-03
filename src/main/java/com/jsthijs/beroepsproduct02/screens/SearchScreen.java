package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.Application;
import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import static com.jsthijs.beroepsproduct02.Application.*;

public class SearchScreen implements Screen {
    private final Scene scene;

    public SearchScreen(String searchText, String tag, String releaseYear, String type) {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);

        VBox vbox = new VBox(itemList(searchText, tag, releaseYear, type));
        vbox.setPrefWidth(window_size[0]);
        vbox.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(window_size[0], Double.MAX_VALUE);

        scrollPane.setContent(vbox);
        root.getChildren().addAll(header, scrollPane);

    }

    private Pane itemList(String searchText, String tag, String releaseYear, String type) {
        FlowPane itemList = new FlowPane();
        itemList.setMaxWidth(1160);
        itemList.setHgap(10);
        itemList.setVgap(10);

        try {
            ResultSet rs = db.getSearchResults(searchText, tag, releaseYear, type);
            while(rs.next()){
                itemList.getChildren().add(new Item(rs).renderItem());
            }
        } catch (SQLException ex) { throw new RuntimeException(ex); }

        return itemList;
    }


    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Search Results";
    }
}
