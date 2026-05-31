package com.jsthijs.beroepsproduct02.screens;

import static com.jsthijs.beroepsproduct02.Application.*;

import com.jsthijs.beroepsproduct02.Application;
import com.jsthijs.beroepsproduct02.models.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

// Detailpagina voor een item met tags en contactinformatie.
public class ItemScreen implements Screen {
    private final Item item;
    private final Scene scene;

    public ItemScreen(Item item) {
        this.item = item;

        // Root container.
        FlowPane root = new FlowPane();
        this.scene = new Scene(root, window_size[0], window_size[1]);
        root.setAlignment(Pos.TOP_CENTER);
        root.setVgap(70);
        ApplyStylesheet(this.scene);

        // Container voor afbeelding en tekst.
        HBox itemBox = new HBox();
        itemBox.setPrefSize(1160, 550);
        itemBox.setSpacing(48);

        Node itemImg = null;
        try {
            // Afbeelding laden.
            ImageView tempItemImg = new ImageView(item.getImage());
            tempItemImg.setFitWidth(400);
            tempItemImg.setFitHeight(550);
            itemImg = tempItemImg;
        } catch (Exception ex) {
            // Placeholder bij fout.
            Region placeholder = new Region();
            placeholder.setPrefSize(400, 550);
            placeholder.setStyle("-fx-background-color: -color-schaduw;");
            itemImg = placeholder;
        }

        // Afbeelding afronden met clip.
        Rectangle clip = new Rectangle(400, 550);
        clip.setArcWidth(20);
        clip.setArcHeight(20);
        itemImg.setClip(clip);

        // Tekstkolom opbouwen.
        VBox itemText = new VBox();
        itemText.setSpacing(24);
        itemText.setAlignment(Pos.CENTER_LEFT);

        // Titel van het item.
        FlowPane itemTitle = new FlowPane(new Text(item.getName()));
        itemTitle.setPrefHeight(48);
        itemTitle.setAlignment(Pos.CENTER_LEFT);
        itemTitle.getStyleClass().add("h1");

        // Lijst van tags tonen.
        HBox itemTags = new HBox();
        itemTags.setAlignment(Pos.CENTER_LEFT);
        itemTags.setPrefHeight(32);
        itemTags.setSpacing(10);
        item.renderTags().forEach(tag -> itemTags.getChildren().add(tag));

        // Beschrijving tekst.
        FlowPane itemDescription = new FlowPane(new Text(item.getSummary()));
        itemDescription.setPrefHeight(240);
        itemDescription.setStyle("-fx-border-color: -color-schaduw; -fx-padding: 16px; -fx-border-radius: 8px; -fx-background-radius: 8px;");

        // Pane voor eigenaar.
        FlowPane itemOwnerPane = new FlowPane();
        itemOwnerPane.setPrefHeight(48);
        itemOwnerPane.setAlignment(Pos.CENTER_LEFT);

        // Contactsectie.
        HBox contactBox = new HBox();
        contactBox.setPrefSize(712, 48);
        contactBox.setAlignment(Pos.CENTER_LEFT);
        contactBox.setSpacing(14);

        // Icoon laden.
        ImageView contactIcon = new ImageView(Application.class.getResource("icons/letter-48.png").toString());
        contactIcon.setFitWidth(48);
        contactIcon.setPreserveRatio(true);
        contactIcon.getStyleClass().add("icon");

        // Contactknop instellen.
        Button contactButton = new Button("Neem contact op");
        contactButton.setPrefSize(150, 48);
        contactButton.getStyleClass().addAll("btn", "h3");

        // Variabelen voor contactgegevens van de eigenaar alvast maken.
        String itemOwnerPhonenumber = "";
        String itemOwnerEmail = "";

        try {
            // Eigenaar gegevens ophalen.
            ResultSet rs = db.getItemOwnerDetails(item.getUserId());
            if (rs.next()) {
                Label ownerTxt =
                    new Label("Eigenaar: " + rs.getString("name") + " In " + rs.getString("city"));
                ownerTxt.getStyleClass().addAll("h2", "hyperlink");
                ownerTxt.setOnMouseClicked(mouseEvent -> {
                    // Navigeren naar profiel van eigenaar wanneer er op de label wordt geklikt.
                    NavigateTo(new ProfileScreen(this.item.getUserId()));
                });

                itemOwnerPane.getChildren().add(ownerTxt);

                // Lege variablen vullen met de contactgegevens.
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
            // Toon een dialoog met kopieerbare contactgegevens.
            Alert contactAlert = new Alert(Alert.AlertType.INFORMATION);
            contactAlert.getDialogPane().getStylesheets().add(
                Objects.requireNonNull(Application.class.getResource("stylesheets/style.css")).toExternalForm()
            );
            contactAlert.setTitle("Contactgegevens");
            contactAlert.setHeaderText("Kopieer de contactgegevens hieronder");

            // Content voor de dialoog.
            VBox contactContent = new VBox(10);

            if (!finalItemOwnerEmail.isEmpty()) {
                // Email veld tonen als er een email-adres is.
                Label emailLabel = new Label("Email");
                emailLabel.getStyleClass().add("h3");
                TextField emailField = new TextField(finalItemOwnerEmail);
                emailField.getStyleClass().add("txtfield");
                emailField.setEditable(false);
                contactContent.getChildren().addAll(emailLabel, emailField);
            }

            if (!finalItemOwnerPhonenumber.isEmpty()) {
                // Telefoon veld tonen als er een telefoonnummer is.
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

        // Onderdelen samenvoegen.
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
