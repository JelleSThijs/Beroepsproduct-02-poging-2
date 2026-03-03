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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

public class LoginScreen implements Screen{
    private final Scene scene;

    public LoginScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(10);

        FlowPane alert = new FlowPane(new Text("gegevens incorrect"));
        alert.setMinWidth(window_size[0]);
        alert.setAlignment(Pos.CENTER);
        alert.setVisible(false);

        GridPane loginPane = new GridPane();
        loginPane.setPrefSize(350, 225);
        loginPane.setPadding(new Insets(250, 0, 0, 0));
        loginPane.setHgap(15);
        loginPane.setVgap(15);
        loginPane.setAlignment(Pos.TOP_CENTER);

// Row 0 - Username
        Text usernameLabel = new Text("Gebruikersnaam");
        TextField username = new TextField();
        username.setPromptText("Gebruikersnaam");

        loginPane.add(usernameLabel, 0, 0);
        loginPane.add(username, 1, 0);

// Row 1 - Password
        Text passwordLabel = new Text("Wachtwoord");
        TextField password = new TextField();
        password.setPromptText("Wachtwoord");

        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(password, 1, 1);

// Row 2 - Buttons
        Button registerButton = new Button("Registreren");
        GridPane.setHalignment(registerButton, HPos.CENTER);
        registerButton.setOnMouseClicked(e -> { NavigateTo(new RegisterScreen()); });

        Button loginButton = new Button("Login");
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.setOnMouseClicked(e -> {
            try {
                ResultSet rs = db.loginUser(username.getText(), password.getText());
                if (rs.next()) {
                    user = new User(rs);
                    NavigateTo(new ProfileScreen(user.getId()));
                }
                else {
                    username.setStyle("-fx-text-fill: red;");
                    password.setStyle("-fx-text-fill: red;");
                    alert.setVisible(true);
                }
            } catch (SQLException ex) { throw new RuntimeException(ex); }
        });

        loginPane.add(registerButton, 0, 2);
        loginPane.add(loginButton, 1, 2);

        root.getChildren().addAll(header, loginPane, alert);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Login Screen";
    }

}
