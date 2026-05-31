package com.jsthijs.beroepsproduct02;

import com.jsthijs.beroepsproduct02.models.Tags;
import com.jsthijs.beroepsproduct02.models.User;
import com.jsthijs.beroepsproduct02.panes.HeaderPane;
import com.jsthijs.beroepsproduct02.screens.HomeScreen;
import com.jsthijs.beroepsproduct02.screens.Screen;

import javafx.css.Stylesheet;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;
import java.util.Objects;

public class Application extends javafx.application.Application {
    public static Stylesheet stylesheet;
    private static Stage primaryStage;
    public static int[] margin = {32, 32};
    public static int[] window_size = {1440, 800};
    public static Database db = new Database("localhost","root","beroepsproduct-02-jelle-thijs");
    public static FlowPane header = new HeaderPane().getHeader();
    public static User user = null;
    public static Tags dbTags = new Tags();

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        NavigateTo(new HomeScreen());
        stage.show();
    }

    public static void NavigateTo(Screen screenClass){
        primaryStage.setScene(screenClass.getScene());
        primaryStage.setTitle(screenClass.getTitle());
    }

    public static void ApplyStylesheet(Scene scene) {
        scene.getStylesheets().add(
            Objects.requireNonNull(Application.class.getResource("stylesheets/style.css")).toExternalForm()
        );
    }
}
