package app.controller;

import app.factory.PopUpFactory;
import app.rest.EmployeesRestClient;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

import java.net.URL;
import java.util.ResourceBundle;

public class DeleteEmployeeController implements Initializable {

    private EmployeesRestClient employeesRestClient;
    private PopUpFactory popUpFactory;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button cancelButton;

    private Integer idEmployee;

    public DeleteEmployeeController(){
        employeesRestClient = new EmployeesRestClient();
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

        employeesRestClient.deleteEmployee(idEmployee, httpStatus -> {
            Platform.runLater(() -> {
                waitingPopUp.close();
                if(httpStatus.equals(HttpStatus.OK)) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Pracownik zostal usuniety", () -> getStage().close());
                    infoPopUp.show();
                }else {
                    Stage errorPopUp = popUpFactory.createErrorPopUp("Blad przy usuwaniu pracownika", () -> getStage().close());
                    errorPopUp.show();
                }
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
