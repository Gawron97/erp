package app.controller;

import app.dto.ItemDto;
import app.dto.QuantityTypeDto;
import app.factory.PopUpFactory;
import app.handler.ProcessFinishedHandler;
import app.rest.ItemRestClient;
import app.rest.QuantityTypeRestClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EditItemController implements Initializable {

    private final ItemRestClient itemRestClient;
    private final QuantityTypeRestClient quantityTypeRestClient;
    private final PopUpFactory popUpFactory;


    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private ChoiceBox<QuantityTypeDto> quantityTypeCB;

    @FXML
    private Button saveButton;

    @FXML
    private TextField warehouseTF;

    private Integer idItem;

    public EditItemController(){
        itemRestClient = new ItemRestClient();
        quantityTypeRestClient = new QuantityTypeRestClient();
        popUpFactory = new PopUpFactory();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(actionEvent -> saveItem());
    }

    private void saveItem() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("ladujemy item do bazy danych");
        waitingPopUp.show();

        String warehouse = warehouseTF.getText();
        String name = nameTF.getText();
        double quantity = Double.parseDouble(quantityTF.getText());
        QuantityTypeDto quantityTypeDto = quantityTypeCB.getSelectionModel().getSelectedItem();

        ItemDto itemDto = ItemDto.of(name, quantity, quantityTypeDto, warehouse);
        itemDto.setIdItem(idItem);

        itemRestClient.saveItem(itemDto, () -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                Stage infoPopUp = popUpFactory.createInfoPopUp("Item zostal edytowany", () -> getStage().close());
                infoPopUp.show();
            });
        });

    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> getStage().close());
    }

    public void loadItemData(Integer idItem){
        itemRestClient.loadItem(idItem, itemDto -> {
            this.idItem = idItem;
            warehouseTF.setText(itemDto.getWarehouseName());
            nameTF.setText(itemDto.getName());
            quantityTF.setText(Double.toString(itemDto.getQuantity()));

            quantityTypeRestClient.loadQuantityTypes(quantityTypeDtoList -> {
                Platform.runLater(() -> {
                    quantityTypeCB.setItems(FXCollections.observableList(quantityTypeDtoList));
                    for(int i=0; i<quantityTypeCB.getItems().size(); i++){
                        QuantityTypeDto quantityTypeDto = quantityTypeCB.getItems().get(i);
                        if(quantityTypeDto.equals(itemDto.getQuantityTypeDto())){
                            quantityTypeCB.getSelectionModel().select(i);
                            break;
                        }
                    }
                });
            });

        });
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
