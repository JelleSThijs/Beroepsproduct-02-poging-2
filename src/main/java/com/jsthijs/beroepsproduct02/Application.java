package com.jsthijs.beroepsproduct02;

import com.jsthijs.beroepsproduct02.models.Tags;
import com.jsthijs.beroepsproduct02.models.User;
import com.jsthijs.beroepsproduct02.panes.HeaderPane;
import com.jsthijs.beroepsproduct02.screens.HomeScreen;
import com.jsthijs.beroepsproduct02.screens.Screen;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.HashMap;

public class Application extends javafx.application.Application {
    private static Stage primaryStage;
    public static int[] margin = {32, 32};
    public static int[] window_size = {1440, 800};
    public static Database db = new Database("localhost","root","beroepsproduct-02");
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

}
