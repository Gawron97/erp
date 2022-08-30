package app.controller;

import app.dto.ItemDto;
import app.dto.QuantityTypeDto;
import app.factory.PopUpFactory;
import app.rest.ItemRestClient;
import app.rest.QuantityTypeRestClient;
import app.table.WarehouseTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddItemController implements Initializable {

    private PopUpFactory popUpFactory;
    private ItemRestClient itemRestClient;
    private QuantityTypeRestClient quantityTypeRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private TextField warehouseTF;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    @FXML
    private ChoiceBox<QuantityTypeDto> quantityTypeCB;
    private Integer idWarehouse;

    public AddItemController(){
        popUpFactory = new PopUpFactory();
        itemRestClient = new ItemRestClient();
        quantityTypeRestClient = new QuantityTypeRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeSaveButton();
        initializeCancelButton();
        initializeQuantityTypeCB();
    }

    private void initializeQuantityTypeCB() {

        quantityTypeRestClient.loadQuantityTypes(quantityTypeDtoList -> {
            Platform.runLater(() -> {
                quantityTypeCB.setItems(FXCollections.observableList(quantityTypeDtoList));
            });
        });

    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> getStage().close());
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(actionEvent -> saveItem());

    }

    private void saveItem() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("ladujemy item do bazy danych");
        waitingPopUp.show();

        String name = nameTF.getText();
        QuantityTypeDto quantityTypeDto = quantityTypeCB.getSelectionModel().getSelectedItem();
        double quantity = Double.parseDouble(quantityTF.getText());

        ItemDto itemDto = ItemDto.of(name, quantity, quantityTypeDto, idWarehouse);

        itemRestClient.saveItem(itemDto, () -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                Stage infoPopUp = popUpFactory.createInfoPopUp("Item zostal dodany", () -> getStage().close());
                infoPopUp.show();
            });
        });

    }

    public void loadWarehouse(WarehouseTableModel warehouse){
        warehouseTF.setEditable(false);
        warehouseTF.setText(warehouse.getName());
        this.idWarehouse = warehouse.getIdWarehouse();
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
