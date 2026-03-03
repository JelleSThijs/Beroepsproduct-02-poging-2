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

public class EditScreen extends NewScreen implements Screen {
    private Scene scene;

    public EditScreen(Item item) {
        super();

        imagePath.setText(item.getImage());
        title.setText(item.getName());
        maker.setText(item.getMaker());
        releaseYear.setText(item.getReleaseYear().toString());
        type.setValue(item.getType());
        summary.setText(item.getSummary());
        item.getTags().forEach(tag -> {
            genre.getSelectionModel().select(tag);
        });

        save.setOnAction(e -> { updateItem(item); });
    }

    @Override
    public String getTitle() {
        return "Edit Item Screen";
    }

    private void updateItem(Item item) {
        ArrayList<String> itemTags = new ArrayList<>(genre.getSelectionModel().getSelectedItems());
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

        db.updateItem(item);
        db.setItemTags(item.getId(), itemTags);
        NavigateTo(new ProfileScreen(user.getId()));
    }

}
