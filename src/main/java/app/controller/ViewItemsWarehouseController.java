package app.controller;

import app.dto.ItemDto;
import app.dto.WarehouseDto;
import app.factory.PopUpFactory;
import app.handler.ProcessFinishedHandler;
import app.handler.WarehouseViewExitInitializer;
import app.rest.ItemRestClient;
import app.rest.WarehouseRestClient;
import app.table.ItemTableModel;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewItemsWarehouseController implements Initializable {

    private static final String ADD_FXML = "/fxml/add-item.fxml";
    private static final String DELETE_FXML = "/fxml/delete-item.fxml";
    private static final String TRANSPORT_FXML = "/fxml/transport-item.fxml";

    private WarehouseRestClient warehouseRestClient;
    private PopUpFactory popUpFactory;

    @FXML
    private TableView<ItemTableModel> itemsTV;

    @FXML
    private Button exitButton;

    @FXML
    private Button addItemButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button transportButton;


    private WarehouseTableModel warehouse;

    public ViewItemsWarehouseController(){
        warehouseRestClient = new WarehouseRestClient();
        popUpFactory = new PopUpFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeItemsTV();
        initializeAddButton();
        initializeDeleteButton();
        initializeTransportButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(actionEvent -> {
            try {
                Stage deleteStage = new Stage();
                deleteStage.initStyle(StageStyle.UNDECORATED);
                deleteStage.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(DELETE_FXML));
                Scene scene = new Scene(loader.load(), 500, 400);
                deleteStage.setScene(scene);

                DeleteItemController deleteItemController = loader.getController();
                deleteItemController.saveIdItem(itemsTV.getSelectionModel().getSelectedItem().getIdItem());

                deleteStage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void initializeTransportButton() {
        transportButton.setOnAction(actionEvent -> {
            try {

                Stage waitingPopUp = popUpFactory.createWaitingPopUp("wczytujemy dane potrzebne o przedmiocie i transporcie");
                waitingPopUp.show();

                Stage transportStage = new Stage();
                transportStage.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(TRANSPORT_FXML));
                Scene scene = new Scene(loader.load(), 1024, 768);
                transportStage.setScene(scene);

                TransportItemController transportItemController = loader.getController();

                transportItemController.loadTransportData(itemsTV.getSelectionModel().getSelectedItem().getIdItem(),
                        warehouse.getIdWarehouse(), () -> {
                    waitingPopUp.close();
                    transportStage.show();
                });

            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void initializeAddButton() {
        addItemButton.setOnAction(actionEvent -> {
            try{
                Stage addStage = new Stage();
                addStage.initStyle(StageStyle.UNDECORATED);
                addStage.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(ADD_FXML));
                Scene scene = new Scene(loader.load(), 500, 400);
                addStage.setScene(scene);

                AddItemController addItemController = loader.getController();
                addItemController.loadWarehouse(warehouse);

                addStage.show();
            }catch (IOException e){
                e.printStackTrace();
            }


        });
    }

    private void initializeItemsTV() {

        itemsTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("name"));

        TableColumn quantityColumn = new TableColumn("quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, Double>("quantity"));

        TableColumn quantityTypeColumn = new TableColumn("quantityType");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("quantityType"));

        itemsTV.getColumns().addAll(nameColumn, quantityColumn, quantityTypeColumn);

    }

    public void loadData(WarehouseTableModel warehouse, ProcessFinishedHandler handler){
        ObservableList<ItemTableModel> data = FXCollections.observableArrayList();

        this.warehouse = warehouse;

        warehouseRestClient.loadWarehouse(warehouse.getIdWarehouse(), warehouseDto -> {
            Platform.runLater(() -> {
                List<ItemDto> itemDtoList = warehouseDto.getItems();
                data.addAll(itemDtoList.stream().map(itemDto -> ItemTableModel.of(itemDto)).collect(Collectors.toList()));
                itemsTV.setItems(data);
                handler.handle();
            });

        });
    }


    public void initializeExitButton(WarehouseViewExitInitializer initializer){
        exitButton.setOnAction(actionEvent -> initializer.init());
    }


}
