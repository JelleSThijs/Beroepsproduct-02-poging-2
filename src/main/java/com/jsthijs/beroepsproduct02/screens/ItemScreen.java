package com.jsthijs.beroepsproduct02.screens;

import static com.jsthijs.beroepsproduct02.Application.*;

import com.jsthijs.beroepsproduct02.Application;
import com.jsthijs.beroepsproduct02.models.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class ItemScreen implements Screen {
    private final Item item;
    private final Scene scene;

    public ItemScreen(Item item) {
        this.item = item;

        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(70);
        ApplyStylesheet(this.scene);

        HBox itemBox = new HBox();
        itemBox.setPrefSize(1160, 550);
        itemBox.setSpacing(48);

        ImageView itemImg = new ImageView(item.getImage());
        itemImg.setFitWidth(400);
        itemImg.setFitHeight(550);

        Rectangle clip = new Rectangle(400, 550);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        itemImg.setClip(clip);

        VBox itemText = new VBox();
        itemText.setSpacing(24);
        itemText.setAlignment(Pos.CENTER_LEFT);

        FlowPane itemTitle = new FlowPane(new Text(item.getName()));
        itemTitle.setPrefHeight(48);
        itemTitle.setAlignment(Pos.CENTER_LEFT);
        itemTitle.getStyleClass().add("h1");

        HBox itemTags = new HBox();
        itemTags.setAlignment(Pos.CENTER_LEFT);
        itemTags.setPrefHeight(32);
        itemTags.setSpacing(10);
        item.renderTags().forEach(tag -> itemTags.getChildren().add(tag));

        FlowPane itemDescription = new FlowPane(new Text(item.getSummary()));
        itemDescription.setPrefHeight(240);
        itemDescription.setStyle("-fx-border-color: -color-schaduw; -fx-padding: 16px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        FlowPane itemOwnerPane = new FlowPane();
        itemOwnerPane.setPrefHeight(48);
        itemOwnerPane.setAlignment(Pos.CENTER_LEFT);

        HBox contactBox = new HBox();
        contactBox.setPrefSize(712, 48);
        contactBox.setAlignment(Pos.CENTER_LEFT);
        contactBox.setSpacing(14);

        ImageView contactIcon = new ImageView(Application.class.getResource("icons/letter-48.png").toString());
        contactIcon.setFitWidth(48);
        contactIcon.setPreserveRatio(true);
        contactIcon.getStyleClass().add("icon");

        Button contactButton = new Button("Neem contact op");
        contactButton.setPrefSize(150, 48);
        contactButton.getStyleClass().addAll("btn", "h3");

        String itemOwnerPhonenumber = "";
        String itemOwnerEmail = "";

        try {
            ResultSet rs = db.getItemOwnerDetails(item.getUserId());
            if (rs.next()) {
                Text ownerTxt =
                    new Text("Eigenaar: " + rs.getString("name") + " In " + rs.getString("city"));
                ownerTxt.getStyleClass().add("h2");
                itemOwnerPane.getChildren().add(ownerTxt);

                itemOwnerPhonenumber = rs.getString("phonenumber");
                itemOwnerEmail = rs.getString("email");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        String finalItemOwnerEmail = itemOwnerEmail;
        String finalItemOwnerPhonenumber = itemOwnerPhonenumber;
        contactButton.setOnAction(actionEvent -> {
            actionEvent.consume();
            Alert contactAlert = new Alert(Alert.AlertType.INFORMATION);
            contactAlert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(Application.class.getResource("stylesheets/style.css")).toExternalForm()
            );
            contactAlert.setTitle("Contactgegevens");
            contactAlert.setHeaderText("Kopieer de contactgegevens hieronder");

            VBox contactContent = new VBox(10);

            if (!finalItemOwnerEmail.isEmpty()) {
                Label emailLabel = new Label("Email");
                emailLabel.getStyleClass().add("h3");
                TextField emailField = new TextField(finalItemOwnerEmail);
                emailField.getStyleClass().add("txtfield");
                emailField.setEditable(false);
                contactContent.getChildren().addAll(emailLabel, emailField);
            }

            if (!finalItemOwnerPhonenumber.isEmpty()) {
                Label phoneLabel = new Label("Telefoonnummer");
                phoneLabel.getStyleClass().add("h3");
                TextField phoneField = new TextField(finalItemOwnerPhonenumber);
                phoneField.getStyleClass().add("txtfield");
                phoneField.setEditable(false);
                contactContent.getChildren().addAll(phoneLabel, phoneField);
            }

            contactAlert.getDialogPane().setContent(contactContent);
            contactAlert.showAndWait();
        });

        contactBox.getChildren().addAll(contactIcon, contactButton);
        itemText.getChildren().addAll(itemTitle, itemTags, itemDescription, itemOwnerPane, contactBox);
        itemBox.getChildren().addAll(itemImg, itemText);
        root.getChildren().addAll(header, itemBox);
    }

    public Scene getScene() {
        return this.scene;
    }
    public String getTitle() {
        return item.getName();
    }

}
