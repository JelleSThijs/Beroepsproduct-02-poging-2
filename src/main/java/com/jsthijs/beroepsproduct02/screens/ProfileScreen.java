package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.sql.ResultSet;
import java.sql.SQLException;

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

        try {
            ResultSet rs = db.getUserItems(this.userId);
            while (rs.next()) {
                if (user.getId() == this.userId) {
                    VBox ItemPane = new VBox();

                }
                itemList.getChildren().add(new Item(rs).renderItem());
            }
        } catch (SQLException e) { throw new RuntimeException(e); }

        return itemList;
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "User Profile";
    }
}
