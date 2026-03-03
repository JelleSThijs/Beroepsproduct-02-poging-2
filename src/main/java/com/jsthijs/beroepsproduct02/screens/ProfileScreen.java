package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
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
    private final ToggleGroup toggleGroup = new ToggleGroup();

    public ProfileScreen(int userId) {
        this.userId = userId;

        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(10);
        root.getChildren().add(header);

        if (user.getId() == this.userId) {
            FlowPane crudPane = new FlowPane();
            crudPane.setHgap(10);
            crudPane.setPrefWidth(window_size[0]);
            crudPane.setAlignment(Pos.CENTER);

            Button newButton = new Button("Nieuw");
            newButton.setOnAction(e -> { NavigateTo(new NewScreen()); });

            Button editButton = new Button("Edit");
            editButton.setOnAction(e -> {
                NavigateTo(new EditScreen((Item) this.toggleGroup.getSelectedToggle().getUserData()));
            });

            Button deleteButton = new Button("Verwijder");
            deleteButton.setOnAction(e -> {
                user.deleteItem((Item) this.toggleGroup.getSelectedToggle().getUserData());
            });

            crudPane.getChildren().addAll(deleteButton, editButton, newButton);
            root.getChildren().add(crudPane);
        }

        VBox vbox = new VBox(itemList(userId));
        vbox.setPrefWidth(window_size[0]);
        vbox.setAlignment(Pos.TOP_CENTER);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(window_size[0], Double.MAX_VALUE);

        scrollPane.setContent(vbox);
        root.getChildren().add(scrollPane);

    }

    private Pane itemList(Integer userId) {
        FlowPane itemList = new FlowPane();
        itemList.setMaxWidth(1160);
        itemList.setHgap(10);
        itemList.setVgap(10);

        try {
            ResultSet rs = db.getUserItems(this.userId);
            while (rs.next()) {
                Item item = new Item(rs);
                VBox itemPane = new VBox();
                if (user.getId() == this.userId) {
                    RadioButton rb = new RadioButton("Selecteer item");
                    rb.setUserData(item);
                    rb.setToggleGroup(this.toggleGroup);
                    itemPane.getChildren().add(rb);
                }

                itemPane.getChildren().add(item.renderItem());
                itemList.getChildren().add(itemPane);
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
