package com.jsthijs.beroepsproduct02.panes;

import com.jsthijs.beroepsproduct02.Application;
import java.sql.ResultSet;

import com.jsthijs.beroepsproduct02.screens.HomeScreen;
import com.jsthijs.beroepsproduct02.screens.LoginScreen;
import com.jsthijs.beroepsproduct02.screens.ProfileScreen;
import com.jsthijs.beroepsproduct02.screens.SearchScreen;
import javafx.geometry.Pos;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

// Headerbalk met navigatie en zoekfilters.
public class HeaderPane {
    private FlowPane header;

    public HeaderPane() {
    // Header FlowPane aanmaken.
        // Container voor de header.
        FlowPane headerPane = new FlowPane();
        headerPane.setPrefWidth(window_size[0]);
        headerPane.setAlignment(Pos.CENTER);
        headerPane.setHgap(32);
        headerPane.setPadding(new javafx.geometry.Insets(8));
        headerPane.setStyle("-fx-background-color: #f0f0f0;");

    // Children aanmaken.
        // Home knop.
        // Home-knop container.
        FlowPane homeButton = new FlowPane();
        homeButton.setPrefSize(72, 72);
        homeButton.getStyleClass().add("icon");
        homeButton.setAlignment(Pos.CENTER);
        homeButton.setOnMouseClicked(e -> { NavigateTo(new HomeScreen()); });

        // Home-icoon laden.
        ImageView homeIcon = new ImageView(Application.class.getResource("icons/home-64.png").toString());
        homeIcon.setPreserveRatio(true);
        homeIcon.setFitHeight(64);

        homeButton.getChildren().add(homeIcon);

        // Zoekveld.
        // Container voor zoeken en filters.
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPrefSize(1160, 40);
        searchBox.setMaxSize(1160, 40);
        searchBox.setStyle("-fx-border-radius: 8px; -fx-background-radius: 8px;");


            // Text veld.
            // Zoekveld instellen.
            TextField searchText = new TextField();
            searchText.setPromptText("Zoeken");
            searchText.setPrefSize(600, 48);
            searchText.getStyleClass().addAll("h3", "dropdown");
            searchText.setStyle("-fx-border-radius: 100 0 0 100; -fx-background-radius: 100 0 0 100; -fx-padding: 0 0 0 16");

            // Filter 1 (tags uit de database).
            // Dropdown met tags.
            ChoiceBox filter1 = new ChoiceBox();
            filter1.setPrefSize(156, 48);
            filter1.getStyleClass().addAll("h3", "dropdown");
            filter1.setValue("Genre");
            filter1.getItems().add("Genre");
            try {
                ResultSet rs = db.getTags();
                while(rs.next()) { filter1.getItems().add(rs.getString("name")); }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

            // Filter 2.
            // Vrije tekst voor jaartal.
            TextField filter2 = new TextField();
            filter2.setPrefSize(156, 48);
            filter2.setPromptText("Release Date");
            filter2.getStyleClass().addAll("h3", "dropdown");
            filter2.setTextFormatter(new TextFormatter<>(change -> {
                if (change.getText().matches("[0-9]*") && change.getControlNewText().length() <= 4) {
                    return change;
                }
                    return null;
            }));

            // Filter 3.
            // Dropdown voor type.
            ChoiceBox filter3 = new ChoiceBox();
            filter3.setPrefSize(156, 48);
            filter3.getStyleClass().addAll("h3", "dropdown");
            filter3.setValue("Type");
            filter3.getItems().addAll("Type", "boek", "film");

        // Zoekknop.
            // Zoek-icoon laden.
            ImageView searchIcon = new ImageView(Application.class.getResource("icons/search-48.png").toString());
            searchIcon.setPreserveRatio(true);
            searchIcon.setFitHeight(32);

            FlowPane searchIconPane = new FlowPane(searchIcon);
            searchIconPane.setAlignment(Pos.CENTER);
            searchIconPane.getStyleClass().add("icon");
            searchIconPane.setStyle("-fx-border-radius: 0 100 100 0; -fx-background-radius: 0 100 100 0; -fx-padding: 0;");
            searchIconPane.setPrefSize(48,48);
            searchIconPane.setOnMouseClicked(event -> {
                // Navigeren naar zoekresultaten.
                NavigateTo(new SearchScreen(
                    searchText.getText(),
                    filter1.getValue().toString(),
                    filter2.getText(),
                    filter3.getValue().toString()
                ));
            });

        searchBox.getChildren().addAll(searchText, filter1, filter2, filter3, searchIconPane);

    // Gebruikers account knop.
        // Knop voor gebruikersprofiel.
        FlowPane userButton = new FlowPane();
        userButton.setPrefSize(72, 72);
        userButton.getStyleClass().add("icon");
        userButton.setAlignment(Pos.CENTER);
        userButton.setOnMouseClicked(e -> {
            if(user != null) { NavigateTo(new ProfileScreen(user.getId())); }
            else { NavigateTo(new LoginScreen()); }
        });

        // Gebruiker-icoon laden.
        ImageView userIcon = new ImageView(Application.class.getResource("icons/user-64.png").toString());
        userIcon.setPreserveRatio(true);
        userIcon.setFitHeight(64);

        userButton.getChildren().add(userIcon);

    // Children toevoegen.
        // Alles aan de header toevoegen.
        headerPane.getChildren().addAll(homeButton, searchBox, userButton);
        header = headerPane;
    }

    public FlowPane getHeader() {
        // Header teruggeven.
        return header;
    }

}
