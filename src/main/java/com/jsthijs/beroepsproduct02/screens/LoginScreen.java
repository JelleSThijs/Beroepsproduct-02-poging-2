package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.User;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
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

// Scherm voor inloggen en navigatie naar registratie.
public class LoginScreen implements Screen{
    private final Scene scene;

    public LoginScreen() {
        // Root container opzetten.
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(10);
        ApplyStylesheet(this.scene);

        // Foutmelding voor onjuiste inlog.
        FlowPane alert = new FlowPane(new Text("gegevens incorrect"));
        alert.setMinWidth(window_size[0]);
        alert.setAlignment(Pos.CENTER);
        alert.setVisible(false);

        // Grid met velden en knoppen.
        GridPane loginPane = new GridPane();
        loginPane.setPrefSize(350, 225);
        loginPane.setPadding(new Insets(250, 0, 0, 0));
        loginPane.setHgap(15);
        loginPane.setVgap(15);
        loginPane.setAlignment(Pos.TOP_CENTER);

// Row 0 - Title
        Text title = new Text("Inloggen");
        title.getStyleClass().add("h1");
        loginPane.add(title, 0, 0);

// Row 1 - Username
        Text usernameLabel = new Text("Gebruikersnaam");
        usernameLabel.getStyleClass().add("h3");
        TextField username = new TextField();
        username.getStyleClass().addAll("h3", "txtfield");
        username.setPromptText("Gebruikersnaam");

        loginPane.add(usernameLabel, 0, 1);
        loginPane.add(username, 1, 1);

// Row 2 - Password
        Text passwordLabel = new Text("Wachtwoord");
        passwordLabel.getStyleClass().add("h3");
        TextField password = new TextField();
        password.getStyleClass().addAll("h3", "txtfield");
        password.setPromptText("Wachtwoord");

        loginPane.add(passwordLabel, 0, 2);
        loginPane.add(password, 1, 2);

// Row 2 - Buttons
        Button registerButton = new Button("Registreren");
        registerButton.getStyleClass().addAll("btn");
        registerButton.setPrefWidth(150);
        GridPane.setHalignment(registerButton, HPos.CENTER);
        registerButton.setOnMouseClicked(e -> { NavigateTo(new RegisterScreen()); });

        Button loginButton = new Button("Login");
        loginButton.getStyleClass().add("btn");
        loginButton.setPrefWidth(150);
        GridPane.setHalignment(loginButton, HPos.CENTER);
        loginButton.setOnMouseClicked(e -> {
            try {
                // Controleer inloggegevens en navigeer bij succes.
                ResultSet rs = db.loginUser(username.getText(), password.getText());
                if (rs.next()) {
                    // Gebruiker opslaan in de sessie.
                    user = new User(rs);
                    NavigateTo(new ProfileScreen(user.getId()));
                }
                else {
                    // Toon foutmelding als de login niet klopt.
                    username.setStyle("-fx-text-fill: red;");
                    password.setStyle("-fx-text-fill: red;");
                    alert.setVisible(true);
                }
            } catch (SQLException ex) {
                showErrorAlert("Inloggen is mislukt. Controleer uw gegevens.");
                throw new RuntimeException(ex);
            }
        });

        loginPane.add(registerButton, 0, 3);
        loginPane.add(loginButton, 1, 3);

        root.getChildren().addAll(header, loginPane, alert);
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "Login Screen";
    }

    // Toont een foutmelding als er iets misgaat bij het inloggen.
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Er ging iets mis");
        alert.setContentText(message);
        alert.showAndWait();
    }

}
