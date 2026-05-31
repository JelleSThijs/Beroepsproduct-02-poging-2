package com.jsthijs.beroepsproduct02.screens;

import javafx.scene.Scene;

// Basisinterface voor alle schermen in de app.
public interface Screen {
    // Geeft de scene terug die dit scherm rendert.
    public Scene getScene();

    // Geeft de titel voor het venster terug.
    public String getTitle();
}
