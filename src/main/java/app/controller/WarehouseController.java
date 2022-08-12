package app.controller;

import app.dto.WarehouseDto;
import app.factory.PopUpFactory;
import app.rest.WarehouseRestClient;
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

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehouseController implements Initializable {

    private static final String VIEW_WAREHOUSE = "/fxml/view-warehouse.fxml";

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
    private Button viewButton;

    @FXML
    private TableView<WarehouseTableModel> warehousesTV;

    public WarehouseController(){
        popUpFactory = new PopUpFactory();
        warehouseRestClient = new WarehouseRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        initializeViewButton();

    }

    private void initializeViewButton() {
        viewButton.setOnAction(actionEvent -> {
            WarehouseTableModel selectedWarehouse = warehousesTV.getSelectionModel().getSelectedItem();
            try{
                Stage warehouseView = new Stage();
                warehouseView.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_WAREHOUSE));

                Scene scene = new Scene(loader.load(), 1024, 768);
                warehouseView.setScene(scene);

                Stage waitingPopUp = popUpFactory.createWaitingPopUp("pobieramy dane o magazynie");
                waitingPopUp.show();

                ViewWarehouseController viewController = loader.<ViewWarehouseController>getController();

                viewController.loadData(selectedWarehouse, () -> {
                    waitingPopUp.close();
                    warehouseView.show();
                });

                


            }catch (IOException e){
                e.printStackTrace();
            }
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
