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
import java.sql.SQLException;
import java.util.concurrent.Flow;

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

        FlowPane alert = new FlowPane(new Text("gegevens incorrect"));
        alert.setMinWidth(window_size[0]);
        alert.setAlignment(Pos.CENTER);
        alert.setVisible(false);

        Button loginButton = new Button("Login");

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

        userInputPane.getChildren().add(loginButton);
        loginPane.getChildren().addAll(labelPane, userInputPane);
        root.getChildren().addAll(header, loginPane, alert);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Login Screen";
    }

}
