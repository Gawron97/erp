package app.controller;

import app.factory.PopUpFactory;
import app.rest.ItemRestClient;
import app.table.ItemTableModel;
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

public class WarehouseController implements Initializable {

    private PopUpFactory popUpFactory;
    private ItemRestClient itemRestClient;

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
    private TableView<ItemTableModel> warehouseTV;

    public WarehouseController(){
        popUpFactory = new PopUpFactory();
        itemRestClient = new ItemRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
    }

    private void initializeTableView() {
        warehouseTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("name"));

        TableColumn quantityColumn = new TableColumn("quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, Double>("quantity"));

        TableColumn quantityTypeColumn = new TableColumn("quantityType");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("quantityType"));

        warehouseTV.getColumns().addAll(nameColumn, quantityColumn, quantityTypeColumn);

        loadItems();

    }

    private void loadItems() {
        ObservableList<ItemTableModel> data = FXCollections.observableArrayList();

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o przedmiotach");
        waitingPopUp.show();

        itemRestClient.loadItems(items -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(items.stream().map(itemDto -> ItemTableModel.of(itemDto)).collect(Collectors.toList()));
                warehouseTV.setItems(data);
                waitingPopUp.close();
            });

        });


    }
}
