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
import javafx.stage.Stage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import static com.jsthijs.beroepsproduct02.Application.*;

// Scherm om een bestaand item te bewerken.
public class EditScreen extends NewScreen implements Screen {
    private Scene scene;

    public EditScreen(Item item) {
        super();

        // Vul de invoervelden met de huidige item gegevens.
        imagePath.setText(item.getImage());
        title.setText(item.getName());
        maker.setText(item.getMaker());
        releaseYear.setText(item.getReleaseYear().toString());
        type.setValue(item.getType());
        summary.setText(item.getSummary());
        item.getTags().forEach(tag -> {
            // Selecteer bestaande tags in de lijst.
            genre.getSelectionModel().select(tag);
        });

        // save actie veranderen zodat wijzigingen opslaan in plaats van nieuwe item maken.
        save.setOnAction(e -> { updateItem(item); });
    }

    @Override
    public String getTitle() {
        return "Edit Item Screen";
    }

    // Slaat wijzigingen op en keert terug naar het profiel.
    private void updateItem(Item item) {
        // Geselecteerde tags verzamelen.
        ArrayList<String> itemTags = new ArrayList<>(genre.getSelectionModel().getSelectedItems());
        // Nieuwe data in de itemklas zetten.
        item.setData(
                title.getText(),
                summary.getText(),
                imagePath.getText(),
                maker.getText(),
                Integer.parseInt(releaseYear.getText()),
                type.getValue(),
                user.getId(),
                itemTags
        );

        // Update de item in de database.
        db.updateItem(item);
        // Tags van de item updaten in de database
        db.setItemTags(item.getId(), itemTags);
        // Terug naar profiel sturen.
        NavigateTo(new ProfileScreen(user.getId()));
    }

}
