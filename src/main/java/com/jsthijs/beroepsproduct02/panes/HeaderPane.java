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
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;

import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

public class HeaderPane {
    private FlowPane header;

    public HeaderPane() {
    // header flowpane aanmaken
        FlowPane headerPane = new FlowPane();
        headerPane.setPrefSize(window_size[0], 112);
        headerPane.setAlignment(Pos.CENTER);
        headerPane.setHgap(32);
        headerPane.setStyle("-fx-background-color: #f0f0f0;");

    // Children aanmaken
        // home knop
        FlowPane homeButton = new FlowPane();
        homeButton.setPrefSize(72, 72);
        homeButton.setStyle("-fx-background-color: white;");
        homeButton.setAlignment(Pos.CENTER);
        homeButton.setOnMouseClicked(e -> { NavigateTo(new HomeScreen()); });

        ImageView homeIcon = new ImageView(Application.class.getResource("icons/home-64.png").toString());
        homeIcon.setPreserveRatio(true);
        homeIcon.setFitHeight(64);

        homeButton.getChildren().add(homeIcon);

        // zoekveld
        HBox searchBox = new HBox();
        searchBox.setAlignment(Pos.CENTER);
        searchBox.setPrefSize(1160, 40);
        searchBox.setMaxSize(1160, 40);
        searchBox.setSpacing(8);
        searchBox.setStyle("-fx-background-color: #a8a8a8;");

            // text veld
            TextField searchText = new TextField();
            searchText.setPromptText("Zoeken");
            searchText.setPrefSize(600, 40);

            // filter 1
            ChoiceBox filter1 = new ChoiceBox();
            filter1.setPrefSize(156, 40);
            filter1.setValue("Genre");
            try {
                ResultSet rs = db.executeQuery("SELECT name FROM tags;");
                while(rs.next()) { filter1.getItems().add(rs.getString("name")); }

            } catch (SQLException e) {
                throw new RuntimeException(e);
            }


            // filter 2
            TextField filter2 = new TextField();
            filter2.setPrefSize(156, 40);
            filter2.setPromptText("Release Date");

            // filter 3
            ChoiceBox filter3 = new ChoiceBox();
            filter3.setPrefSize(156, 40);
            filter3.setValue("Type");
            filter3.getItems().addAll("boek", "film");

            // zoekknop
            ImageView searchIcon = new ImageView(Application.class.getResource("icons/search-48.png").toString());
            searchIcon.setPreserveRatio(true);
            searchIcon.setFitHeight(48);

            FlowPane searchIconPane = new FlowPane(searchIcon);
            searchIconPane.setAlignment(Pos.CENTER);
            searchIconPane.setPrefSize(48,48);
            searchIconPane.setOnMouseClicked(event -> {
                NavigateTo(new SearchScreen(
                    searchText.getText(),
                    filter1.getValue().toString(),
                    filter2.getText(),
                    filter3.getValue().toString()
                ));
            });

        searchBox.getChildren().addAll(searchText, filter1, filter2, filter3, searchIconPane);

        // gebruikers account knop
        FlowPane userButton = new FlowPane();
        userButton.setPrefSize(72, 72);
        userButton.setStyle("-fx-background-color: white;");
        userButton.setAlignment(Pos.CENTER);
        userButton.setOnMouseClicked(e -> {
            if(user != null) { NavigateTo(new ProfileScreen(user.getId())); }
            else { NavigateTo(new LoginScreen()); }
        });

        ImageView userIcon = new ImageView(Application.class.getResource("icons/user-64.png").toString());
        userIcon.setPreserveRatio(true);
        userIcon.setFitHeight(64);

        userButton.getChildren().add(userIcon);

    // Children toevoegen
        headerPane.getChildren().addAll(homeButton, searchBox, userButton);
        header = headerPane;
    }

    public FlowPane getHeader() {
        return header;
    }

}
