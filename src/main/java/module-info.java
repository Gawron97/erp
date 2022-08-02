module git.erp {
    requires javafx.controls;
    requires javafx.fxml;
    requires static lombok;
    requires spring.boot.starter.web;
    requires spring.web;


    opens app to javafx.fxml;
    exports app;
    opens app.controller to javafx.fxml;
    exports app.controller;
    exports app.dto;
    opens app.dto to javafx.fxml;
    exports app.rest;
    opens app.rest to javafx.fxml;
    exports app.table;
    opens app.table to javafx.base;
    exports app.handler;
    opens app.handler to javafx.fxml;
}