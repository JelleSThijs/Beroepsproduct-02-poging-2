package com.jsthijs.beroepsproduct02.screens;

// Scherm voor het aanmaken van een nieuw item.

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
        // Root van het scherm.
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        ApplyStylesheet(this.scene);

        // Container voor velden en lijst.
        HBox itemPane =  new HBox();
        itemPane.setAlignment(Pos.CENTER);
        itemPane.setPrefSize(window_size[0], 550);
        itemPane.setMaxSize(window_size[0], 550);

        // Container voor inputvelden.
        FlowPane inputs = new FlowPane();
        inputs.setPrefSize(700, 550);
        inputs.setMaxSize(700, 550);
        inputs.setAlignment(Pos.CENTER);
        inputs.setOrientation(Orientation.VERTICAL);
        inputs.setHgap(10);
        inputs.setVgap(24);

        // Styling voor titel en afbeelding.
        title.getStyleClass().addAll("h2", "txtfield");
        imagePath.getStyleClass().addAll("h3", "txtfield");

        title.setPromptText("Titel");
        title.setPrefWidth(600);

        imagePath.setPromptText("Link naar foto / poster");

        // Rij met korte velden.
        HBox shortInputBox = new HBox();
        shortInputBox.setSpacing(25);
        shortInputBox.setAlignment(Pos.CENTER_LEFT);

        maker.setPromptText("Uitgever / Schrijver");
        maker.setPrefSize(300, 32);
        maker.getStyleClass().addAll("h3", "txtfield");

        releaseYear.setPromptText("Jaar");
        releaseYear.setPrefSize(100, 32);
        releaseYear.getStyleClass().addAll("h3", "txtfield");
        // Beperk jaar invoer tot cijfers en max 4 tekens.
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

        // Eigenaar-tekst tonen.
        FlowPane ownerDetails = new FlowPane(
                new Text("Eigenaar: " + user.getName() + " in " + user.getCity())
        );
        ownerDetails.setPadding(new Insets(0, 0, 0, 6));
        ownerDetails.getStyleClass().add("h2");

        // Tags selecteren.
        genre.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        genre.setMaxHeight(440);
        genre.getStyleClass().add("h3");

        // Tags vullen uit de database.
        dbTags.getName().forEach(tagName -> { genre.getItems().add(tagName); });

        save.setOnAction(e -> { saveItem(); });
        // Opslaan knop voor het item.
        save.getStyleClass().addAll("btn", "h3");

        // Onderdelen samenstellen.
        shortInputBox.getChildren().addAll(maker, releaseYear, type);
        inputs.getChildren().addAll(title, imagePath, shortInputBox, summary, ownerDetails);
        itemPane.getChildren().addAll(inputs, genre);
        root.getChildren().addAll(header, itemPane, save);

    }

    // Maakt een nieuw item aan en slaat dit op.
    private void saveItem() {
        try {
            // Geselecteerde tags ophalen.
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

            // Opslaan in de database.
            db.addItem(item);
            db.setItemTags(item.getId(), itemTags);
            NavigateTo(new ProfileScreen(user.getId()));
        } catch (Exception ex) {
            showErrorAlert("Opslaan is mislukt.");
            throw new RuntimeException(ex);
        }
    }

    // Toont een foutmelding als opslaan faalt.
    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Fout");
        alert.setHeaderText("Er ging iets mis");
        alert.setContentText(message);
        alert.showAndWait();
    }

    public Scene getScene() {
        return this.scene;
    }

    public String getTitle() {
        return "New Item Screen";
    }

}
