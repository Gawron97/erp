module git.erp {
    requires javafx.controls;
    requires javafx.fxml;


    opens git.erp to javafx.fxml;
    exports git.erp;
}