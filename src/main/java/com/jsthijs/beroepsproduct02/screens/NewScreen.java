package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
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

        HBox itemPane =  new HBox();
        itemPane.setAlignment(Pos.CENTER);
        itemPane.setPrefSize(window_size[0], 550);
        itemPane.setMaxSize(window_size[0], 550);





        FlowPane inputs = new FlowPane();
        inputs.setPrefSize(700, 550);
        inputs.setMaxSize(700, 550);
        inputs.setAlignment(Pos.CENTER);
        inputs.setOrientation(Orientation.VERTICAL);
        inputs.setHgap(10);
        inputs.setVgap(10);

        TextField imagePath = new TextField();
        imagePath.setPromptText("Link naar foto / poster");

        TextField title = new TextField();
        title.setPromptText("Titel");
        title.setPrefWidth(600);

        TextField maker = new TextField();
        maker.setPromptText("Titel");
        maker.setPrefWidth(600);

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

        ListView genre = new ListView();
        genre.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genre.setMaxHeight(425);

        try {
            ResultSet rs = db.executeQuery("SELECT name FROM tags;");
            while(rs.next()) { genre.getItems().add(rs.getString("name")); }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Button save = new Button("opslaan");
        save.setOnAction(e -> {

            int itemId = 0;
            try {
                ResultSet rs = db.executeQuery("SELECT id FROM items ORDER BY id DESC LIMIT 1;");
                while(rs.next()) { itemId = rs.getInt("id"); }

                int finalItemId = itemId;
                db.executeUpdate(
                    "INSERT INTO items VALUES (" +
                        itemId + ", '" +
                        title.getText() + "', '" +
                        summary.getText() + "', '" +
                        imagePath.getText() + "', '" +
                        maker.getText() + "', " +
                        Integer.parseInt(releaseYear.getText()) + ", '" +
                        type.getValue().toString() + "', " +
                        user.getId() +
                    ");"
                );


                genre.getItems().forEach(item -> {
                    String tagName = item.toString();
                    try {
                        db.executeUpdate(
                            "INSERT INTO itemtags VALUES (" +
                                    finalItemId + ", (SELECT id FROM tags WHERE name = '" +
                                    tagName + "';)" +
                            ");"
                        );
                    } catch (SQLException ex) { throw new RuntimeException(ex); }
                });

            } catch (SQLException ex) { throw new RuntimeException(ex); }
        });

        inputs.getChildren().addAll(imagePath, title, maker, releaseYear, type, summary, userFullName);
        itemPane.getChildren().addAll(inputs, genre);
        root.getChildren().addAll(header, itemPane, save);

    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "New Item Screen";
    }

}
