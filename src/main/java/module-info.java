module git.erp {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;


    opens app to javafx.fxml;
    exports app;
    opens app.controller to javafx.fxml;
    exports app.controller;
}