package app.controller;

import app.dto.TransportDto;
import app.dto.TransportItemDto;
import app.dto.WarehouseCBDto;
import app.factory.PopUpFactory;
import app.handler.ButtonInitializer;
import app.handler.ProcessFinishedHandler;
import app.rest.ItemRestClient;
import app.rest.WarehouseRestClient;
import app.table.TruckTableModel;
import app.util.TransportationTypeEnum;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.*;

public class TransportItemController implements Initializable {

    private final ItemRestClient itemRestClient;
    private final WarehouseRestClient warehouseRestClient;
    private final PopUpFactory popUpFactory;



    @FXML
    private ChoiceBox<TransportationTypeEnum> transportationTypeCB;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField currentQuantityTF;

    @FXML
    private Label maxCapacityInfoLabel;

    @FXML
    private TextField quantityToSendTF;

    @FXML
    private TextField quantityTypeTF;

    @FXML
    private Button saveButton;

    @FXML
    private Button selectTruckButton;

    @FXML
    private TableView<TruckTableModel> trucksTV;

    @FXML
    private TextField fromWarehouseTF;

    @FXML
    private ChoiceBox<WarehouseCBDto> toWarehousesCB;

    private Integer idItem;
    private TruckTableModel selectedTruck;
    private Integer idWarehouse;

    //TODO dodanie opcji all ktora uzupelni quantityToSend do ilosci jaka jest itemu w magazynie

    public TransportItemController(){
        itemRestClient = new ItemRestClient();
        warehouseRestClient = new WarehouseRestClient();
        popUpFactory = new PopUpFactory();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
        initializeSelectTruckButton();
    }

    private void initializeSelectTruckButton() {

        selectTruckButton.setOnAction(actionEvent -> {
            selectedTruck = trucksTV.getSelectionModel().getSelectedItem();
            maxCapacityInfoLabel.setText("MAX capacity: " + selectedTruck.getCapacity());
        });

    }

    private void initializeSaveButton() {
        saveButton.setOnAction(actionEvent -> transportItem());
    }

    private void transportItem() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Wysylamy item do transportu");
        waitingPopUp.show();

        TransportItemDto transportItemDto = createTransportItemDto();

        itemRestClient.transportItem(transportItemDto, httpStatus -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                if(httpStatus.equals(HttpStatus.OK)) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Item wyslany do transportu", () -> getStage().close());
                    infoPopUp.show();
                }else {
                    Stage errorPopUp = popUpFactory.createErrorPopUp("Blad przy wysylaniu itemu do transportu",
                            () -> getStage().close());
                    errorPopUp.show();
                }
            });
        });

    }

    private TransportItemDto createTransportItemDto() {

        Integer idItem = this.idItem;
        double quantityToSend = Double.parseDouble(quantityToSendTF.getText());
        String transportationType = transportationTypeCB.getSelectionModel().getSelectedItem().toString();
        Integer newWarehouseId = toWarehousesCB.getSelectionModel().getSelectedItem().getIdWarehouse();
        Integer idTruck = selectedTruck.getIdTruck();

        return TransportItemDto.of(idItem, quantityToSend, transportationType, newWarehouseId, idTruck);

    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> getStage().close());
    }

    public void loadTransportData(Integer idItem, Integer idWarehouse, ButtonInitializer initializer){

        itemRestClient.loadTransportData(idItem, transportDto -> {
            Platform.runLater(() -> {
                this.idItem = idItem;
                this.idWarehouse = idWarehouse;
                fillItemDetails(transportDto);
                loadWarehousesChoiceBox();
                initializeTruckTV();
                loadTrucksData(transportDto);
                loadTransportationTypeCB();
                initializer.init();
            });

        });
    }

    private void loadTransportationTypeCB() {

        List<TransportationTypeEnum> transportationTypeEnums = Arrays.asList(TransportationTypeEnum.TRANSPORT_TO_WAREHOUSE,
                TransportationTypeEnum.SELL);

        Platform.runLater(() -> {
            transportationTypeCB.setItems(FXCollections.observableList(transportationTypeEnums));
        });
    }

    private void loadTrucksData(TransportDto transportDto) {
        trucksTV.setItems(FXCollections.observableList(transportDto.getTruckDtoList().
                stream().map(truckDto -> TruckTableModel.of(truckDto)).toList()));
    }

    private void initializeTruckTV() {
        trucksTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<TruckTableModel, String>("name"));

        TableColumn capacityColumn = new TableColumn("capacity");
        capacityColumn.setMinWidth(100);
        capacityColumn.setCellValueFactory(new PropertyValueFactory<TruckTableModel, Double>("capacity"));


        trucksTV.getColumns().addAll(nameColumn, capacityColumn);

    }

    private void loadWarehousesChoiceBox() {

        warehouseRestClient.loadWarehousesToCB(warehouseCBDtoList -> {
            Platform.runLater(() -> {
                toWarehousesCB.setItems(FXCollections.observableList(warehouseCBDtoList));
            });
        });
    }

    private void fillItemDetails(TransportDto transportDto){
        fromWarehouseTF.setEditable(false);
        fromWarehouseTF.setText(transportDto.getWarehouseName());
        nameTF.setEditable(false);
        nameTF.setText(transportDto.getName());
        currentQuantityTF.setEditable(false);
        currentQuantityTF.setText(Double.toString(transportDto.getQuantity()));
        quantityTypeTF.setEditable(false);
        quantityTypeTF.setText(transportDto.getQuantityTypeDto().getName());
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
