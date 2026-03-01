package com.jsthijs.beroepsproduct02.screens;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import static com.jsthijs.beroepsproduct02.Application.*;

public class RegisterScreen implements Screen{
    private final Scene scene;

    public RegisterScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);

        HBox loginPane = new HBox();
        loginPane.setPrefSize(350, 225);
        loginPane.setStyle("-fx-background-color: #8e2795;");
        loginPane.setPadding(new Insets(250, 0, 0, 0));


        VBox labelPane = new VBox();
        labelPane.setPrefSize(125, 225);

        labelPane.getChildren().add(new FlowPane(new Text("Gebruikersnaam")));
        labelPane.getChildren().add(new FlowPane(new Text("Wachtwoord")));
        Button registerButton = new Button("Registreren");
        registerButton.setOnMouseClicked(e -> { NavigateTo(new RegisterScreen()); });
        labelPane.getChildren().add(registerButton);


        VBox userInputPane = new VBox();
        userInputPane.setPrefSize(225, 225);

        TextField username = new TextField();
        username.setPromptText("Gebruikersnaam");
        userInputPane.getChildren().add(username);

        TextField password = new TextField();
        username.setPromptText("Wachtwoord");
        userInputPane.getChildren().add(password);

        Button loginButton = new Button("Login");
        loginButton.setOnMouseClicked(e -> {

        });
        userInputPane.getChildren().add(loginButton);

        loginPane.getChildren().addAll(labelPane, userInputPane);

        root.getChildren().addAll(header, loginPane);

    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Register Screen";
    }

}
