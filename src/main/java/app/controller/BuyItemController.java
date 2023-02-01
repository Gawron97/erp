package app.controller;

import app.dto.ItemDto;
import app.dto.QuantityTypeDto;
import app.dto.WarehouseCBDto;
import app.factory.PopUpFactory;
import app.handler.ButtonInitializer;
import app.handler.ProcessFinishedHandler;
import app.rest.ItemRestClient;
import app.rest.QuantityTypeRestClient;
import app.rest.WarehouseRestClient;
import app.table.StockItemTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.ResourceBundle;

public class BuyItemController implements Initializable {

    private PopUpFactory popUpFactory;
    private ItemRestClient itemRestClient;
    private QuantityTypeRestClient quantityTypeRestClient;
    private WarehouseRestClient warehouseRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private TextField priceTF;

    @FXML
    private ChoiceBox<WarehouseCBDto> warehouseCB;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField quantityTypeTF;

    private Integer idWarehouse;
    private StockItemTableModel stockItemTableModel;

    public BuyItemController(){
        popUpFactory = new PopUpFactory();
        itemRestClient = new ItemRestClient();
        quantityTypeRestClient = new QuantityTypeRestClient();
        warehouseRestClient = new WarehouseRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSaveButton();
        initializeCancelButton();
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> getStage().close());
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(actionEvent -> saveItem());
    }

    private void saveItem() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Kupujemy przedmiot");
        waitingPopUp.show();

        String name = nameTF.getText();
        QuantityTypeDto quantityTypeDto = QuantityTypeDto.of(stockItemTableModel.getIdQuantityType(),
                stockItemTableModel.getQuantityType());

        double quantity = Double.parseDouble(quantityTF.getText());
        double price = Double.parseDouble(priceTF.getText());
        idWarehouse = warehouseCB.getSelectionModel().getSelectedItem().getIdWarehouse();

        ItemDto itemDto = ItemDto.of(name, quantity, price, quantityTypeDto, idWarehouse);

        itemRestClient.saveItem(itemDto, httpStatus -> {
            Platform.runLater(() -> {
                waitingPopUp.close();

                if(httpStatus.equals(HttpStatus.OK))
                {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Item zostal kupiony", () -> getStage().close());
                    infoPopUp.show();
                }else {
                    Stage errorPopUp = popUpFactory.createErrorPopUp("wystapil blad przy kupowaniu itemu",
                            () -> getStage().close());
                    errorPopUp.show();
                }
            });
        });

    }

    public void loadData(StockItemTableModel stockItemTableModel, ButtonInitializer initializer){
        this.stockItemTableModel = stockItemTableModel;
        loadItemStockDetails();
        loadWarehousesCB();
        Platform.runLater(() -> initializer.init());
    }

    private void loadItemStockDetails(){
        Platform.runLater(() -> {
            nameTF.setEditable(false);
            priceTF.setEditable(false);
            quantityTypeTF.setEditable(false);
            nameTF.setText(stockItemTableModel.getName());
            priceTF.setText(Double.toString(stockItemTableModel.getPrice()));
            quantityTypeTF.setText(stockItemTableModel.getQuantityType());
        });
    }

    private void loadWarehousesCB(){
        warehouseRestClient.loadWarehousesToCB(warehouseCBDtoList -> {
            Platform.runLater(() -> {
                warehouseCB.setItems(FXCollections.observableList(warehouseCBDtoList));
            });
        });
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
