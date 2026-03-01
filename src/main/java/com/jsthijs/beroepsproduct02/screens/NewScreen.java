package com.jsthijs.beroepsproduct02.screens;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.sql.ResultSet;
import java.sql.SQLException;

import static com.jsthijs.beroepsproduct02.Application.*;

public class NewScreen implements Screen {
    private final Scene scene;

    public NewScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);

        GridPane itemPane =  new GridPane();
        itemPane.setAlignment(Pos.TOP_CENTER);
        itemPane.setPrefSize(1160, 550);
        itemPane.setMaxSize(1160, 550);


        TextField imagePath = new TextField();
        imagePath.setPromptText("Link naar foto / poster");
        imagePath.setPadding(new Insets(150, 100, 150, 100));

        FlowPane inputs = new FlowPane();
        inputs.setPrefSize(700, 550);
        inputs.setMaxSize(700, 550);
        inputs.setAlignment(Pos.CENTER);
        inputs.setOrientation(Orientation.VERTICAL);
        inputs.setHgap(10);
        inputs.setVgap(10);

        TextField title = new TextField();
        title.setPromptText("Titel");
        title.setPrefWidth(600);

        ChoiceBox genre = new ChoiceBox();
        genre.setPrefSize(50, 32);
        genre.setValue("Genre");
        try {
            ResultSet rs = db.executeQuery("SELECT name FROM tags;");
            while(rs.next()) { genre.getItems().add(rs.getString("name")); }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        TextField releaseYear = new TextField();
        releaseYear.setPromptText("Jaar");
        releaseYear.setPrefSize(50, 32);
        releaseYear.setMaxSize(50, 32);

        ChoiceBox type = new ChoiceBox();
        type.getItems().addAll("boek", "film");
        type.setPrefSize(50, 32);

        TextArea summary = new TextArea();
        summary.setPromptText("Samenvatting");
        summary.setPrefSize(600, 240);

        FlowPane userFullName = new FlowPane(new Text(user.getName()));

        Button save = new Button("opslaan");
        save.setOnAction(e -> {
        });

        inputs.getChildren().addAll(title, genre, releaseYear, type, summary, userFullName);
        itemPane.add(imagePath, 0, 0);
        itemPane.add(inputs, 1, 0);
        root.getChildren().addAll(header, itemPane, save);

    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "New Item Screen";
    }

}
