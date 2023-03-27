package app.controller;

import app.factory.PopUpFactory;
import app.rest.WarehouseRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteWarehouseController implements Initializable {

    private final WarehouseRestClient warehouseRestClient;
    private final PopUpFactory popUpFactory;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private Button deleteButton;

    private Integer idWarehouse;

    public DeleteWarehouseController(){
        warehouseRestClient = new WarehouseRestClient();
        popUpFactory = new PopUpFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(actionEvent -> {
            deleteWarehouse();
        });
    }

    private void deleteWarehouse(){

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Usuwamy Magazyn");
        waitingPopUp.show();

        warehouseRestClient.deleteWarehouse(idWarehouse, httpStatus -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                if(httpStatus.equals(HttpStatus.OK)) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Magazyn zostal usuniety", () -> getStage().close());
                    infoPopUp.show();
                }else {
                    Stage errorPopUp = popUpFactory.createErrorPopUp("Blad przy usuwaniu magazynu", () -> getStage().close());
                    errorPopUp.show();
                }
            });
        });
    }

    public void setWarehouseIdToDelete(Integer idWarehouse){
        this.idWarehouse = idWarehouse;
    }

    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> getStage().close());
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
