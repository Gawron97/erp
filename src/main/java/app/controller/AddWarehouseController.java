package app.controller;

import app.dto.AddressDto;
import app.dto.CountryDto;
import app.dto.WarehouseDto;
import app.factory.PopUpFactory;
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

public class AddWarehouseController implements Initializable {

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
    private TextField nameTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField streetNumberTextField;

    @FXML
    private TextField streetTextField;

    public AddWarehouseController() {
        this.popUpFactory = new PopUpFactory();
        this.warehouseRestClient = new WarehouseRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(actionEvent -> {
            saveNewWarehouse();
        });
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

        warehouseRestClient.saveWarehouse(warehouseDto, responseCode -> {
            Platform.runLater(() -> {
                waitingPopUp.close();

                if(responseCode.equals(HttpStatus.OK)) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Magazyn zostal zapisany do bazy danych :)",
                            () -> getStage().close());

                    infoPopUp.show();
                }else{
                    Stage errorPopUp = popUpFactory.createErrorPopUp("Wystapil blad", () -> getStage().close());
                    errorPopUp.show();
                }
            });
        });

    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }
}
