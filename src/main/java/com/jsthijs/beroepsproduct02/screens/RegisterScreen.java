package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
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

        FlowPane alert = new FlowPane(new Text("gegevens incorrect"));
        alert.setMinWidth(window_size[0]);
        alert.setAlignment(Pos.CENTER);
        alert.setVisible(false);

        GridPane registerPane = new GridPane();
        registerPane.setPrefSize(350, 225);
        registerPane.setPadding(new Insets(150, 0, 0, 0));
        registerPane.setHgap(15);
        registerPane.setVgap(15);
        registerPane.setAlignment(Pos.TOP_CENTER);

// Row 0 - Username
        Text usernameLabel = new Text("Gebruikersnaam");
        TextField username = new TextField();
        username.setPromptText("Gebruikersnaam");

        registerPane.add(usernameLabel, 0, 0);
        registerPane.add(username, 1, 0);

// Row 1 - Password
        Text passwordLabel = new Text("Wachtwoord");
        TextField password = new TextField();
        password.setPromptText("Wachtwoord");

        registerPane.add(passwordLabel, 0, 1);
        registerPane.add(password, 1, 1);

// Row 2 - naam van gebruiker
        Text nameLabel = new Text("Naam");
        TextField name = new TextField();
        name.setPromptText("Naam");

        registerPane.add(nameLabel, 0, 2);
        registerPane.add(name, 1, 2);

// Row 3 - email
        Text emailLabel = new Text("Email (optioneel)");
        TextField email = new TextField();
        email.setPromptText("Email");

        registerPane.add(emailLabel, 0, 3);
        registerPane.add(email, 1, 3);

// Row 4 - telefoonnummer
        Text phoneLabel = new Text("Telefoonnummer (optioneel)");
        TextField phoneNumber = new TextField();
        phoneNumber.setPromptText("Telefoonnummer");

        registerPane.add(phoneLabel, 0, 4);
        registerPane.add(phoneNumber, 1, 4);

// Row 5 - woonplaats
        Text cityLabel = new Text("Woonplaats (optioneel)");
        TextField city = new TextField();
        city.setPromptText("Woonplaats");

        registerPane.add(cityLabel, 0, 5);
        registerPane.add(city, 1, 5);

// Row 6 - Buttons
        Button registerButton = new Button("Registreren");
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

        registerPane.add(registerButton, 0, 6, 2, 1);
        root.getChildren().addAll(header, registerPane, alert);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Register Screen";
    }

}
