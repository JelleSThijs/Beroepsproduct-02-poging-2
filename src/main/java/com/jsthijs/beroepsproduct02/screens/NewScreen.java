package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;

import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.*;

public class NewScreen implements Screen {
    private final Scene scene;
    protected TextField imagePath = new TextField();
    protected TextField title = new TextField();
    protected TextField maker = new TextField();
    protected TextField releaseYear = new TextField();
    protected ChoiceBox<String> type = new ChoiceBox<String>();
    protected TextArea summary = new TextArea();
    protected ListView<String> genre = new ListView<String>();
    protected Button save = new Button("opslaan");


    public NewScreen() {
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        ApplyStylesheet(this.scene);

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
        inputs.setVgap(24);

        title.getStyleClass().addAll("h2", "txtfield");
        imagePath.getStyleClass().addAll("h3", "txtfield");

        title.setPromptText("Titel");
        title.setPrefWidth(600);

        imagePath.setPromptText("Link naar foto / poster");

        HBox shortInputBox = new HBox();
        shortInputBox.setSpacing(25);
        shortInputBox.setAlignment(Pos.CENTER_LEFT);

        maker.setPromptText("Uitgever / Schrijver");
        maker.setPrefSize(300, 32);
        maker.getStyleClass().addAll("h3", "txtfield");

        releaseYear.setPromptText("Jaar");
        releaseYear.setPrefSize(100, 32);
        releaseYear.getStyleClass().addAll("h3", "txtfield");
        releaseYear.setTextFormatter(new TextFormatter<>(change -> {
            if (change.getText().matches("[0-9]*") && change.getControlNewText().length() <= 4) {
                return change;
            }
                return null;
        }));

        type.getItems().addAll("boek", "film");
        type.setPrefSize(100, 32);
        type.setValue("boek");
        type.getStyleClass().addAll("h3", "dropdown");


        summary.setPromptText("Samenvatting");
        summary.setPrefSize(600, 240);
        summary.getStyleClass().add("h3");

        FlowPane ownerDetails = new FlowPane(
                new Text("Eigenaar: " + user.getName() + " in " + user.getCity())
        );
        ownerDetails.setPadding(new Insets(0, 0, 0, 6));
        ownerDetails.getStyleClass().add("h2");

        genre.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genre.setMaxHeight(440);
        genre.getStyleClass().add("h3");

        dbTags.getName().forEach(tagName -> { genre.getItems().add(tagName); });

        save.setOnAction(e -> { saveItem(); });
        save.getStyleClass().addAll("btn", "h3");

        shortInputBox.getChildren().addAll(maker, releaseYear, type);
        inputs.getChildren().addAll(title, imagePath, shortInputBox, summary, ownerDetails);
        itemPane.getChildren().addAll(inputs, genre);
        root.getChildren().addAll(header, itemPane, save);

    }

    private void saveItem() {
        ArrayList<String> itemTags = new ArrayList<>(genre.getSelectionModel().getSelectedItems());
        Item item = new Item(
                title.getText(),
                summary.getText(),
                imagePath.getText(),
                maker.getText(),
                Integer.parseInt(releaseYear.getText()),
                type.getValue(),
                user.getId(),
                itemTags
        );

        db.addItem(item);
        db.setItemTags(item.getId(), itemTags);
        NavigateTo(new ProfileScreen(user.getId()));
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "New Item Screen";
    }

}
