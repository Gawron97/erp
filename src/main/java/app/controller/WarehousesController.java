package app.controller;

import app.factory.PopUpFactory;
import app.rest.WarehousesRestClient;
import app.table.ItemTableModel;
import app.table.WarehouseTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class WarehousesController implements Initializable {

    private PopUpFactory popUpFactory;
    private WarehousesRestClient warehousesRestClient;

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

    public WarehousesController(){
        popUpFactory = new PopUpFactory();
        warehousesRestClient = new WarehousesRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
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

        warehousesRestClient.loadWarehouses(warehouses -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(warehouses.stream().map(warehouseDto -> WarehouseTableModel.of(warehouseDto)).collect(Collectors.toList()));
                warehousesTV.setItems(data);
                waitingPopUp.close();
            });

        });

    }
}
