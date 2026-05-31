package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

public class RegisterScreen implements Screen{
    private final Scene scene;

    public RegisterScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(10);
        ApplyStylesheet(this.scene);

        FlowPane alert = new FlowPane(new Text("gegevens incorrect"));
        alert.setMinWidth(window_size[0]);
        alert.setAlignment(Pos.CENTER);
        alert.setVisible(false);

        GridPane registerPane = new GridPane();
        registerPane.setPrefSize(450, 225);
        registerPane.setPadding(new Insets(150, 0, 0, 0));
        registerPane.setHgap(15);
        registerPane.setVgap(15);
        registerPane.setAlignment(Pos.TOP_CENTER);

// Row 0 - Title
        Text title = new Text("Registreren");
        title.getStyleClass().add("h1");
        registerPane.add(title, 0, 0);

// Row 1 - Username
        Text usernameLabel = new Text("Gebruikersnaam");
        usernameLabel.getStyleClass().add("h3");
        TextField username = new TextField();
        username.getStyleClass().addAll("h3", "txtfield");
        username.setPromptText("Gebruikersnaam");

        registerPane.add(usernameLabel, 0, 1);
        registerPane.add(username, 1, 1);

// Row 2 - Password
        Text passwordLabel = new Text("Wachtwoord");
        passwordLabel.getStyleClass().add("h3");
        TextField password = new TextField();
        password.getStyleClass().addAll("h3", "txtfield");
        password.setPromptText("Wachtwoord");

        registerPane.add(passwordLabel, 0, 2);
        registerPane.add(password, 1, 2);

// Row 3 - naam van gebruiker
        Text nameLabel = new Text("Naam");
        nameLabel.getStyleClass().add("h3");
        TextField name = new TextField();
        name.getStyleClass().addAll("h3", "txtfield");
        name.setPromptText("Naam");

        registerPane.add(nameLabel, 0, 3);
        registerPane.add(name, 1, 3);

// Row 4 - email
        Text emailLabel = new Text("Email (optioneel)");
        emailLabel.getStyleClass().add("h3");
        TextField email = new TextField();
        email.getStyleClass().addAll("h3", "txtfield");
        email.setPromptText("Email");

        registerPane.add(emailLabel, 0, 4);
        registerPane.add(email, 1, 4);

// Row 5 - telefoonnummer
        Text phoneLabel = new Text("Telefoonnummer (optioneel)");
        phoneLabel.getStyleClass().add("h3");
        TextField phoneNumber = new TextField();
        phoneNumber.getStyleClass().addAll("h3", "txtfield");
        phoneNumber.setPromptText("Telefoonnummer");
        phoneNumber.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("[0-9]*")) { return change; }
            return null;
        }));

        registerPane.add(phoneLabel, 0, 5);
        registerPane.add(phoneNumber, 1, 5);

// Row 6 - woonplaats
        Text cityLabel = new Text("Woonplaats (optioneel)");
        cityLabel.getStyleClass().add("h3");
        TextField city = new TextField();
        city.getStyleClass().addAll("h3", "txtfield");
        city.setPromptText("Woonplaats");

        registerPane.add(cityLabel, 0, 6);
        registerPane.add(city, 1, 6);

// Row 7 - Buttons
        Button registerButton = new Button("Registreren");
        registerButton.getStyleClass().add("btn");
        GridPane.setHalignment(registerButton, HPos.CENTER);
        registerButton.setOnMouseClicked(e -> {
            User tempUser = new User(
                    username.getText(),
                    password.getText(),
                    name.getText(),
                    email.getText(),
                    phoneNumber.getText(),
                    city.getText()
            );

            if (db.addUser(tempUser)) {
                try {
                    user = new User(db.loginUser(tempUser.getUsername(), tempUser.getPassword()));
                    NavigateTo(new ProfileScreen(user.getId()));
                } catch (SQLException ex) { throw new RuntimeException(ex); }
            }
        });

        registerPane.add(registerButton, 0, 7, 2, 1);
        root.getChildren().addAll(header, registerPane, alert);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Register Screen";
    }

}
