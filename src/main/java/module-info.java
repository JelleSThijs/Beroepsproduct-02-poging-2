module com.jsthijs.beroepsproduct02 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;


    opens com.jsthijs.beroepsproduct02 to javafx.fxml;
    exports com.jsthijs.beroepsproduct02;
    exports com.jsthijs.beroepsproduct02.panes;
    opens com.jsthijs.beroepsproduct02.panes to javafx.fxml;
    exports com.jsthijs.beroepsproduct02.screens;
    opens com.jsthijs.beroepsproduct02.screens to javafx.fxml;
}