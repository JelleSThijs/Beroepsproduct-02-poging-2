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

        items.getChildren().addAll(newBooks, itemList("Book", 6));

        FlowPane newFilms = new FlowPane(new Text("Nieuw toegevoegde films"));
        newFilms.setPrefSize(1200, 32);
        newFilms.setStyle("-fx-background-color: green");

        items.getChildren().addAll(newFilms, itemList("Film", 6));


    }

    private Pane itemList(String type, Integer limit) {
        FlowPane itemList = new FlowPane();
        itemList.setMinSize(1200, 300);
        itemList.setPrefSize(1200, 300);
        itemList.setHgap(10);
        itemList.setVgap(10);

        String query = "SELECT items.*, tags.name as tagName FROM items\n" +
                 "JOIN itemtags ON items.ID = itemtags.itemId\n" +
                 "JOIN tags ON tagId = tags.ID \n" +
                 "WHERE TYPE ='" + type + "'\n" +
                 "GROUP BY items.ID\n" +
                 "ORDER BY items.ID DESC LIMIT " + limit + ";";

        try {
            ResultSet item = db.executeQuery(query);
            while(item.next()){
                itemList.getChildren().add(renderItem(new Item(item), item.getString("tagName")));
            }
        } catch (Exception ex) {
            System.err.println("Error while executing query: " + ex.getMessage());
        }

        return itemList;
    }

    private Pane renderItem(Item item, String tagName) {
        FlowPane itemPane = new FlowPane();
        itemPane.setMinSize(144, 248);
        itemPane.setPrefSize(144, 248);
        itemPane.setMaxSize(144, 248);
        itemPane.setHgap(13);
        itemPane.setVgap(4);


        ImageView itemImg = new ImageView(item.getImage());
        itemImg.setFitHeight(196);
        itemImg.setFitWidth(144);

        FlowPane itemTitle = new FlowPane(new Text(item.getName()));

        FlowPane tagText = new FlowPane(new Text(tagName));
        tagText.setStyle("-fx-background-color: red");
        tagText.setPrefSize(70, 16);

        FlowPane releaseYear = new FlowPane(new Text(item.getReleaseYear().toString()));
        releaseYear.setPrefSize(60, 16);
        releaseYear.setStyle("-fx-background-color: blue");

        FlowPane maker = new FlowPane(new Text(item.getMaker()));
        maker.setStyle("-fx-background-color: purple");
        maker.setPrefSize(144, 16);

        itemPane.getChildren().addAll(itemImg, itemTitle, tagText, releaseYear, maker);

        return itemPane;
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Home Screen";
    }

}
