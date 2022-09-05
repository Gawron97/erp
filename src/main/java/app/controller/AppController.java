package app.controller;

import app.factory.PopUpFactory;
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

    private static final String URL_EMPLOYEE = "/fxml/employee.fxml";
    private static final String URL_WAREHOUSE = "/fxml/warehouse.fxml";
    private static final String URL_WAREHOUSE_VIEW = "/fxml/view-items-warehouse.fxml";
    private static final String URL_ITEM = "/fxml/item.fxml";
    private static final String URL_LOGIN = "/fxml/login.fxml";

    private PopUpFactory popUpFactory;

    @FXML
    private Pane appPain;

    @FXML
    private BorderPane borderPane;

    @FXML
    private MenuItem employeeModuleMI;

    @FXML
    private MenuItem exitMI;

    @FXML
    private MenuItem logoutMI;

    @FXML
    private MenuItem warehouseModuleMI;

    @FXML
    private MenuItem itemModuleMI;


    public AppController(){
        popUpFactory = new PopUpFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadDefault();
        initializeMenuItems();
    }

    private void initializeMenuItems() {
        initializeEmployeeModuleMI();
        initializeWarehouseModuleMI();
        initializeItemModuleMI();
        initializeExitMI();
        initializeLogoutMI();
    }


    private void initializeLogoutMI() {
        logoutMI.setOnAction(actionEvent -> logout());
    }

    private void initializeExitMI() {
        exitMI.setOnAction(actionEvent -> getStage().close());
    }

    private void initializeItemModuleMI() {
        itemModuleMI.setOnAction(actionEvent -> loadModule(URL_ITEM));
    }

    private void initializeWarehouseModuleMI() {
        warehouseModuleMI.setOnAction(actionEvent -> loadWarehouseModule(URL_WAREHOUSE));
    }

    private void initializeEmployeeModuleMI() {
        employeeModuleMI.setOnAction(actionEvent -> loadModule(URL_EMPLOYEE));
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

    private void loadWarehouseModule(String urlWarehouse) {

        appPain.getChildren().clear();

        try{

            FXMLLoader loader = new FXMLLoader(getClass().getResource(urlWarehouse));
            BorderPane warehouseModule = new BorderPane(loader.load());
            appPain.getChildren().add(warehouseModule);

            WarehouseController warehouseController = loader.getController();

            warehouseController.initializeViewItemsButton(warehouseTableModel -> {
                try{
                    appPain.getChildren().clear();
                    FXMLLoader loader2 = new FXMLLoader(getClass().getResource(URL_WAREHOUSE_VIEW));
                    BorderPane itemsOfWarehouse = new BorderPane(loader2.load());
                    appPain.getChildren().add(itemsOfWarehouse);

                    Stage waitingPopUp = popUpFactory.createWaitingPopUp("Ladujemy dane o przedmiotach i magazynie");
                    ViewItemsWarehouseController viewItemsWarehouseController = loader2.getController();
                    waitingPopUp.show();
                    viewItemsWarehouseController.loadData(warehouseTableModel, () -> {
                        waitingPopUp.close();
                    });
                    viewItemsWarehouseController.initializeExitButton(() -> {
                        loadWarehouseModule(URL_WAREHOUSE);
                    });

                }catch (IOException e){
                    e.printStackTrace();
                }

            });

        }catch (IOException e){
            e.printStackTrace();
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
