package app.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class AppController implements Initializable {

    private static final String URL_EMPLOYEE = "/fxml/employees.fxml";
    private static final String URL_WAREHOUSES = "/fxml/warehouses.fxml";
    private static final String URL_WAREHOUSE = "/fxml/warehouse.fxml";
    private static final String URL_LOGIN = "/fxml/login.fxml";

    @FXML
    private Pane appPain;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem employeesModuleMI;

    @FXML
    private MenuItem exitMI;

    @FXML
    private MenuItem logoutMI;

    @FXML
    private MenuItem warehousesModuleMI;

    @FXML
    private MenuItem warehouseModuleMI;


    public AppController(){

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDefault();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
        initializeEmployeeModuleMI();
        initializeWarehousesModuleMI();
        initializeWarehouseModuleMI();
        initializeExitMI();
        initializeLogoutMI();
    }

    private void initializeWarehouseModuleMI() {
        warehouseModuleMI.setOnAction(actionEvent -> loadModule(URL_WAREHOUSE));
    }

    private void initializeLogoutMI() {
        logoutMI.setOnAction(actionEvent -> logout());
    }


    private void initializeExitMI() {
        exitMI.setOnAction(actionEvent -> getStage().close());
    }

    private void initializeWarehousesModuleMI() {
        warehousesModuleMI.setOnAction(actionEvent -> loadModule(URL_WAREHOUSES));
    }

    private void initializeEmployeeModuleMI() {
        employeesModuleMI.setOnAction(actionEvent -> loadModule(URL_EMPLOYEE));
    }

    private void loadDefault(){
        loadModule(URL_EMPLOYEE);
    }

    private void logout(){

        try {
            Stage login = new Stage();
            login.initStyle(StageStyle.UNDECORATED);
            Scene scene = new Scene(FXMLLoader.load(getClass().getResource(URL_LOGIN)), 600, 400);
            login.setScene(scene);
            getStage().close();
            login.show();

        } catch (IOException e) {
            throw new RuntimeException("nie mozna zaladowac pliku fxml" + URL_LOGIN);
        }
    }

    private void loadModule(String moduleUrl){
        appPain.getChildren().clear();

        try{
            BorderPane module = new BorderPane(FXMLLoader.load(getClass().getResource(moduleUrl)));
            appPain.getChildren().add(module);

        }catch (IOException e){
            throw new RuntimeException("nie mozna zaladowac pliku fxml" + moduleUrl);
        }
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }



}
