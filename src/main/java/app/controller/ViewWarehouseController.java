package app.controller;

import app.dto.ItemDto;
import app.dto.WarehouseDto;
import app.handler.ItemsLoadedHandler;
import app.handler.WarehouseViewExitInitializer;
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

public class ViewWarehouseController implements Initializable {

    private static final String ADD_FXML = "/fxml/add-item.fxml";

    private WarehouseRestClient warehouseRestClient;

    @FXML
    private TextField cityTF;

    @FXML
    private TextField countryTF;

    @FXML
    private TableView<ItemTableModel> itemsTV;

    @FXML
    private Button exitButton;

    @FXML
    private Button addItemButton;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField numberOfItemsTF;

    @FXML
    private TextField streetNumberTF;

    @FXML
    private TextField streetTF;

    public ViewWarehouseController(){
        warehouseRestClient = new WarehouseRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeItemsTV();
        initializeAddButton();
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
                addItemController.loadWarehouse(nameTF.getText());

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

    public void loadData(WarehouseTableModel warehouse, ItemsLoadedHandler handler){
        ObservableList<ItemTableModel> data = FXCollections.observableArrayList();


        warehouseRestClient.loadWarehouse(warehouse.getIdWarehouse(), warehouseDto -> {
            Platform.runLater(() -> {
                List<ItemDto> itemDtoList = warehouseDto.getItems();
                data.addAll(itemDtoList.stream().map(itemDto -> ItemTableModel.of(itemDto)).collect(Collectors.toList()));
                itemsTV.setItems(data);
                fillWarehouseData(warehouseDto);
                handler.handle();
            });

        });
    }

    private void fillWarehouseData(WarehouseDto warehouse) {
        nameTF.setEditable(false);
        cityTF.setEditable(false);
        streetTF.setEditable(false);
        streetNumberTF.setEditable(false);
        countryTF.setEditable(false);
        numberOfItemsTF.setEditable(false);

        nameTF.setText(warehouse.getName());
        cityTF.setText(warehouse.getAddressDto().getCity());
        streetTF.setText(warehouse.getAddressDto().getStreet());
        streetNumberTF.setText(warehouse.getAddressDto().getStreetNumber().toString());
        countryTF.setText(warehouse.getAddressDto().getCountryDto().getCountry());
        numberOfItemsTF.setText(Integer.toString(warehouse.getItems().size()));

    }

    public void initializeExitButton(WarehouseViewExitInitializer initializer){
        exitButton.setOnAction(actionEvent -> initializer.init());
    }


}
