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

        items.getChildren().addAll(newBooks, itemList("book", 6));

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
            while (rs.next()) { itemList.getChildren().add(renderItem(new Item(rs))); }
        } catch (SQLException e) { throw new RuntimeException(e); }

        return itemList;
    }

    private Pane renderItem(Item item) {
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
        itemPane.getChildren().addAll(itemImg, itemTitle);

        for (int i = 0; i < item.getTags().size() && i < 3; i ++) {
            FlowPane tagText = new FlowPane(new Text(item.getTags().get(i)));
            tagText.setStyle("-fx-background-color: red");
            tagText.setPrefSize(70, 16);
            itemTitle.getChildren().addAll(tagText);
        }


        FlowPane releaseYear = new FlowPane(new Text(item.getReleaseYear().toString()));
        releaseYear.setPrefSize(60, 16);
        releaseYear.setStyle("-fx-background-color: blue");

        FlowPane maker = new FlowPane(new Text(item.getMaker()));
        maker.setStyle("-fx-background-color: purple");
        maker.setPrefSize(144, 16);

        itemPane.getChildren().addAll(releaseYear, maker);

        return itemPane;
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Home Screen";
    }

}
