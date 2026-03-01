package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;

import static com.jsthijs.beroepsproduct02.Application.*;

public class ProfileScreen implements Screen {
    private final Scene scene;
    private final int userId;
    private ToggleGroup toggleGroup;

    public ProfileScreen(int userId) {
        this.userId = userId;

        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.getChildren().add(header);

        if (user.getId() == this.userId) {
            FlowPane crudPane = new FlowPane();
            crudPane.setHgap(10);
            crudPane.setPrefWidth(window_size[0]);
            crudPane.setAlignment(Pos.CENTER);

            Button deleteButton = new Button("Verwijder");
            deleteButton.setOnAction(e -> {
                user.deleteItem((Item) this.toggleGroup.getSelectedToggle().getUserData());
            });

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> {
                NavigateTo(new EditScreen((Item) this.toggleGroup.getSelectedToggle().getUserData()));
            });

            Button newButton = new Button("Nieuw");
            newButton.setOnAction(e -> { NavigateTo(new NewScreen()); });

            crudPane.getChildren().addAll(deleteButton, editButton, newButton);
            root.getChildren().add(crudPane);
        }

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPannable(true);

        VBox items = new VBox();
        items.setPadding(new Insets(0, 120, 0, 120));
        items.setSpacing(8);
        items.getChildren().add(itemList(userId));

        scrollPane.setContent(items);
        root.getChildren().add(scrollPane);

    }

    private Pane itemList(Integer userId) {
        FlowPane itemList = new FlowPane();
        itemList.setMinSize(1200, 300);
        itemList.setPrefSize(1200, 300);
        itemList.setHgap(10);
        itemList.setVgap(10);

        String query = "SELECT items.*, tags.name as tagName FROM items\n" +
                "JOIN itemtags ON items.ID = itemtags.itemId\n" +
                "JOIN tags ON tagId = tags.ID \n" +
                "WHERE userId ='" + userId + "'\n" +
                "GROUP BY items.ID\n" +
                "ORDER BY items.ID DESC;";

        try {
            ResultSet item = db.executeQuery(query);
            while(item.next()){
                itemList.getChildren().add(renderItem(new Item(item), item.getString("tagName")));
            }
        } catch (Exception ex) { System.err.println("Error while executing query: " + ex.getMessage()); }

        return itemList;
    }

    private Pane renderItem(Item item, String tagName) {
        FlowPane itemPane = new FlowPane();
        itemPane.setMinSize(144, 248);
        itemPane.setPrefSize(144, 248);
        itemPane.setMaxSize(144, 248);
        itemPane.setHgap(13);
        itemPane.setVgap(4);

        if (user.getId() == this.userId) {
            RadioButton rb = new RadioButton("Selecteer item");
            rb.setUserData(item);
            rb.setToggleGroup(this.toggleGroup);
            itemPane.getChildren().add(rb);
        }


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
        return "User Profile";
    }
}
