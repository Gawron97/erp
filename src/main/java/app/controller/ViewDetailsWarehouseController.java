package app.controller;

import app.dto.WarehouseDto;
import app.handler.OnEndedAction;
import app.rest.WarehouseRestClient;
import app.table.WarehouseTableModel;
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

public class ViewDetailsWarehouseController implements Initializable {

    private WarehouseRestClient warehouseRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField cityTF;

    @FXML
    private TextField countryTF;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField numberOfItemsTF;

    @FXML
    private Button okButton;

    @FXML
    private TextField streetNumberTF;

    @FXML
    private TextField streetTF;

    public ViewDetailsWarehouseController(){
        warehouseRestClient = new WarehouseRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeOkButton();
    }

    private void initializeOkButton() {

        okButton.setOnAction(actionEvent -> getStage().close());

    }

    public void loadData(WarehouseTableModel warehouse, OnEndedAction onEndedAction){

        warehouseRestClient.loadWarehouse(warehouse.getIdWarehouse(), responseEntity -> {
            Platform.runLater(() -> {
                if(!HttpStatus.OK.equals(responseEntity.getStatusCode())) {
                    onEndedAction.action(false);
                } else {
                    fillWarehouseData((WarehouseDto) responseEntity.getBody());
                    onEndedAction.action(true);
                }
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

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
