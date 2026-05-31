package com.jsthijs.beroepsproduct02.screens;

// Profielscherm met gebruikersinformatie en items.

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
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

        // Root container voor het profiel.
        VBox root = new VBox();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setSpacing(10);
        ApplyStylesheet(this.scene);
        root.getChildren().add(header);

        // Alleen tonen als er een ingelogde gebruiker is.
        if (user != null) {
            if (user.getId() == this.userId || user.getIsAdmin() == 1) {
                // CRUD knoppen voor items van deze gebruiker.
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

                Button deleteItemButton = new Button("Verwijder");
                deleteItemButton.setOnAction(e -> {
                    deleteItemAlert((Item) this.toggleGroup.getSelectedToggle().getUserData());
                });

                crudPane.getChildren().addAll(newButton, editButton, deleteItemButton);

                if (user.getIsAdmin() == 1 && this.userId != user.getId()) {
                    // Admins kunnen ook andere gebruikers verwijderen.
                    Button deleteUserButton = new Button("Verwijder Gebruiker");
                    deleteUserButton.setOnAction(e -> {
                        deleteUserAlert(this.userId);
                    });

                    crudPane.getChildren().add(deleteUserButton);
                }


                root.getChildren().add(crudPane);
            }
        }

        // Lijst met items van de gebruiker.
        VBox items = new VBox(itemList(this.userId));
        items.setPadding(new Insets(16, 120, 16, 120));
        items.setSpacing(8);

        // Scrollbare content.
        ScrollPane scrollPane = new ScrollPane(items);
        scrollPane.setFitToWidth(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        VBox.setVgrow(scrollPane, Priority.ALWAYS);

        scrollPane.setContent(items);
        root.getChildren().add(scrollPane);

    }

    private Pane itemList(Integer userId) {
        // Flowpane voor items.
        FlowPane itemList = new FlowPane();
        itemList.setMaxWidth(1160);
        itemList.setHgap(10);
        itemList.setVgap(10);

        try {
            // Items ophalen.
            ResultSet rs = db.getUserItems(this.userId);
            while (rs.next()) {
                Item item = new Item(rs);
                VBox itemPane = new VBox();
                itemPane.setSpacing(4);
                if (user != null) {
                    if (user.getId() == this.userId || user.getIsAdmin() == 1) {
                        // Selecteerknop voor bewerken/verwijderen.
                        RadioButton rb = new RadioButton("Selecteer item");
                        rb.setUserData(item);
                        rb.setToggleGroup(this.toggleGroup);
                        rb.getStyleClass().add("radiobtn");
                        itemPane.getChildren().add(rb);
                    }
                }

                // Itemkaart toevoegen.
                itemPane.getChildren().add(item.renderItem());
                itemList.getChildren().add(itemPane);
            }
        } catch (SQLException e) { throw new RuntimeException(e); }

        return itemList;
    }

    // Bevestiging voor item verwijderen.
    private void deleteItemAlert(Item item) {
        // Dialoog openen.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijdering");
        alert.setHeaderText(null);
        alert.setContentText("Weet je zeker dat je deze item wilt verwijderen?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            user.deleteItem(item);
            NavigateTo(new ProfileScreen(this.userId));
        } else {
            alert.close();
        }
    }

    // Bevestiging voor gebruiker verwijderen.
    private void deleteUserAlert(int userId) {
        // Dialoog openen.
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Bevestig verwijdering gebruiker");
        alert.setHeaderText(null);
        alert.setContentText("Weet je zeker dat je deze gebruiker wilt verwijderen?");
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            user.deleteUser(this.userId);
            NavigateTo(new HomeScreen());
        } else {
            alert.close();
        }
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }

    @Override
    public String getTitle() {
        return "Profiel pagina";
    }
}
