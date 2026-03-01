package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.User;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;

import static com.jsthijs.beroepsproduct02.Application.*;

public class LoginScreen implements Screen{
    private final Scene scene;

    public LoginScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);

        HBox loginPane = new HBox();
        loginPane.setPrefSize(350, 225);
        loginPane.setStyle("-fx-background-color: #8e2795;");
        loginPane.setPadding(new Insets(250, 0, 0, 0));


        VBox labelPane = new VBox();
        labelPane.setAlignment(Pos.TOP_CENTER);
        labelPane.setSpacing(17);
        labelPane.setPrefSize(125, 225);

        labelPane.getChildren().add(new FlowPane(new Text("Gebruikersnaam")));
        labelPane.getChildren().add(new FlowPane(new Text("Wachtwoord")));
        Button registerButton = new Button("Registreren");
        registerButton.setOnMouseClicked(e -> { NavigateTo(new RegisterScreen()); });
        labelPane.getChildren().add(registerButton);


        VBox userInputPane = new VBox();
        userInputPane.setSpacing(8);
        userInputPane.setAlignment(Pos.TOP_CENTER);
        userInputPane.setPrefSize(225, 225);

        TextField username = new TextField();
        username.setPromptText("Gebruikersnaam");
        userInputPane.getChildren().add(username);

        TextField password = new TextField();
        password.setPromptText("Wachtwoord");
        userInputPane.getChildren().add(password);

        Button loginButton = new Button("Login");
        loginButton.setOnMouseClicked(e -> { login(username.getText(), password.getText()); });
        userInputPane.getChildren().add(loginButton);

        loginPane.getChildren().addAll(labelPane, userInputPane);

        root.getChildren().addAll(header, loginPane);

    }

    private void login(String username, String password) {
        String query = "SELECT * FROM `users` WHERE username = '" + username + "' AND password = '" + password + "'";
        try {
            ResultSet rs = db.executeQuery(query);
            while (rs.next()) {
                user = new User(rs);
                NavigateTo(new HomeScreen());
            }

        } catch (Exception ex) {

        }
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Login Screen";
    }

}
