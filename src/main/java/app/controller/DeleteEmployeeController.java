package app.controller;

import app.factory.PopUpFactory;
import app.handler.EmployeeLoadedHandler;
import app.rest.EmployeeRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteEmployeeController implements Initializable {

    private EmployeeRestClient employeeRestClient;
    private PopUpFactory popUpFactory;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private Integer idEmployee;

    public DeleteEmployeeController(){
        employeeRestClient = new EmployeeRestClient();
        popUpFactory = new PopUpFactory();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeDeleteButton();
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(actionEvent -> {
            deleteEmployee();
        });
    }

    private void deleteEmployee(){

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Usuwamy pracownika");
        waitingPopUp.show();

        employeeRestClient.deleteEmployee(idEmployee, () -> {
            Platform.runLater(() -> {
                Stage infoPopUp = popUpFactory.createInfoPopUp("Pracownik zostal usuniety", () -> getStage().close());
                waitingPopUp.close();
                infoPopUp.show();
            });
        });
    }

    public void setEmployeeToDelete(Integer idEmployee){
        this.idEmployee = idEmployee;
    }


    private void initializeCancelButton() {
        cancelButton.setOnAction(actionEvent -> getStage().close());

    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
