package app.controller;

import app.dto.AddressDto;
import app.dto.CountryDto;
import app.dto.WarehouseDto;
import app.factory.PopUpFactory;
import app.handler.OnEndedAction;
import app.rest.WarehouseRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.ResourceBundle;

public class EditWarehouseController implements Initializable {

    private final PopUpFactory popUpFactory;
    private final WarehouseRestClient warehouseRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField cityTextField;

    @FXML
    private TextField countryTextField;

    @FXML
    private Button editButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField streetNumberTextField;

    @FXML
    private TextField streetTextField;

    private Integer idWarehouse;

    public EditWarehouseController() {
        warehouseRestClient = new WarehouseRestClient();
        popUpFactory = new PopUpFactory();
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
        editButton.setOnAction(actionEvent -> saveNewWarehouse());
    }

    private void saveNewWarehouse() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Zapisujemy w bazach danych");
        waitingPopUp.show();

        String name = nameTextField.getText();
        String city = cityTextField.getText();
        String street = streetTextField.getText();
        String streetNumber = streetNumberTextField.getText();
        String country = countryTextField.getText();

        CountryDto countryDto = new CountryDto();
        countryDto.setCountry(country);

        AddressDto addressDto = AddressDto.of(city, street, Integer.parseInt(streetNumber), countryDto);

        WarehouseDto warehouseDto = WarehouseDto.of(name, addressDto);
        warehouseDto.setIdWarehouse(idWarehouse);

        warehouseRestClient.saveWarehouse(warehouseDto, responseCode -> {
            Platform.runLater(() -> {
                waitingPopUp.close();

                if(responseCode.equals(HttpStatus.OK)) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Warehouse edited in database",
                            () -> getStage().close());

                    infoPopUp.show();
                }else{
                    Stage errorPopUp = popUpFactory.createErrorPopUp("Something go wrong while editing warehouse",
                            () -> getStage().close());
                    errorPopUp.show();
                }
            });
        });
    }

    public void loadWarehouseData(Integer idWarehouse, OnEndedAction onEndedAction) {
        warehouseRestClient.loadWarehouse(idWarehouse, responseEntity -> {
            Platform.runLater(() -> {
                if(!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                    onEndedAction.action(false);
                } else {
                    WarehouseDto warehouseDto = (WarehouseDto) responseEntity.getBody();
                    this.idWarehouse = warehouseDto.getIdWarehouse();
                    nameTextField.setText(warehouseDto.getName());
                    cityTextField.setText(warehouseDto.getAddressDto().getCity());
                    streetTextField.setText(warehouseDto.getAddressDto().getStreet());
                    streetNumberTextField.setText(warehouseDto.getAddressDto().getStreetNumber().toString());
                    countryTextField.setText(warehouseDto.getAddressDto().getCountryDto().getCountry());
                    onEndedAction.action(true);
                }
            });
        });
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }
}
