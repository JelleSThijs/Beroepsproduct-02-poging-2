package com.jsthijs.beroepsproduct02.screens;

import com.jsthijs.beroepsproduct02.models.Item;
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

        title.setPromptText("Titel");
        title.setPrefWidth(600);

        imagePath.setPromptText("Link naar foto / poster");

        maker.setPromptText("Uitgever / Schrijver");
        maker.setPrefWidth(600);

        releaseYear.setPromptText("Jaar");
        releaseYear.setPrefSize(50, 32);
        releaseYear.setMaxSize(50, 32);

        type.getItems().addAll("boek", "film");
        type.setPrefSize(50, 32);
        type.setValue("boek");

        summary.setPromptText("Samenvatting");
        summary.setPrefSize(600, 240);

        FlowPane userFullName = new FlowPane(new Text(user.getName()));

        genre.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genre.setMaxHeight(425);

        dbTags.getName().forEach(tagName -> { genre.getItems().add(tagName); });

        save.setOnAction(e -> { saveItem(); });

        inputs.getChildren().addAll(title, imagePath, maker, releaseYear, type, summary, userFullName);
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
