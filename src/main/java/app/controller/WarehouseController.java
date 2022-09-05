package app.controller;

import app.factory.PopUpFactory;
import app.handler.WarehouseViewButtonInitializer;
import app.rest.WarehouseRestClient;
import app.table.EmployeeTableModel;
import app.table.WarehouseTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//view Items chce zeby pojawialo sie w oknie tym samym co warehouse(zamiast niego) dlatego implementacja otwierania
//napisana w appController, ktory jest odpowiedzialny za przypisywanie widoku do glownego panelu
//reszta, jak mp. add bedzie implementowana jako osobne okno tutaj
public class WarehouseController implements Initializable {

    private static final String VIEW_DETAILS_WAREHOUSE = "/fxml/view-details-warehouse.fxml";

    private PopUpFactory popUpFactory;
    private WarehouseRestClient warehouseRestClient;

    @FXML
    private Button addButton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button viewItemsButton;

    @FXML
    private TableView<WarehouseTableModel> warehousesTV;

    public WarehouseController(){
        popUpFactory = new PopUpFactory();
        warehouseRestClient = new WarehouseRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
//        initializeAddButton();
//        initializeDeleteButton();
        initializeViewDetailsButton();
    }

    private void initializeViewDetailsButton() {

        viewDetailsButton.setOnAction(actionEvent -> {
            WarehouseTableModel selectedWarehouse = warehousesTV.getSelectionModel().getSelectedItem();
            if (selectedWarehouse != null) {
                try {
                    Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o magazynie");
                    waitingPopUp.show();
                    Stage warehouseDetailsView = new Stage();
                    warehouseDetailsView.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_DETAILS_WAREHOUSE));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    warehouseDetailsView.setScene(scene);
                    ViewDetailsWarehouseController viewController = loader.getController();

                    viewController.loadData(selectedWarehouse, () -> {
                        waitingPopUp.close();
                        warehouseDetailsView.show();
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public void initializeViewItemsButton(WarehouseViewButtonInitializer initializer) {
        viewItemsButton.setOnAction(actionEvent -> {
            WarehouseTableModel selectedWarehouse = warehousesTV.getSelectionModel().getSelectedItem();
            initializer.init(selectedWarehouse);
        });
    }

    private void initializeTableView() {

        warehousesTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<WarehouseTableModel, String>("name"));

        TableColumn cityColumn = new TableColumn("city");
        cityColumn.setMinWidth(100);
        cityColumn.setCellValueFactory(new PropertyValueFactory<WarehouseTableModel, String>("city"));

        TableColumn countryColumn = new TableColumn("country");
        countryColumn.setMinWidth(100);
        countryColumn.setCellValueFactory(new PropertyValueFactory<WarehouseTableModel, String>("country"));


        warehousesTV.getColumns().addAll(nameColumn, cityColumn, countryColumn);

        loadWarehouses();
    }

    private void loadWarehouses() {

        ObservableList<WarehouseTableModel> data = FXCollections.observableArrayList();

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o magazynach");
        waitingPopUp.show();

        warehouseRestClient.loadWarehouses(warehouses -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(warehouses.stream().map(warehouseDto -> WarehouseTableModel.of(warehouseDto)).collect(Collectors.toList()));
                warehousesTV.setItems(data);
                waitingPopUp.close();
            });

        });

    }





}
