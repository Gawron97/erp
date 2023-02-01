package app.controller;

import app.dto.EmployeeDto;
import app.factory.PopUpFactory;
import app.rest.EmployeesRestClient;
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

public class AddEmployeeController implements Initializable {

    private PopUpFactory popUpFactory;
    private EmployeesRestClient employeesRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField salaryTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField surnameTextField;

    public AddEmployeeController(){
        popUpFactory = new PopUpFactory();
        employeesRestClient = new EmployeesRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeCancelButton();
        initializeSaveButton();
    }

    private void initializeSaveButton() {
        saveButton.setOnAction(actionEvent -> {
            saveNewEmployee();
        });
    }

    private void saveNewEmployee() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Zapisujemy w bazach danych");
        waitingPopUp.show();

        String name = nameTextField.getText();
        String pesel = peselTextField.getText();
        String surname = surnameTextField.getText();
        String salary = salaryTextField.getText();

        EmployeeDto employeeDto = EmployeeDto.of(name, pesel, surname, salary);

        employeesRestClient.saveEmployee(employeeDto, httpStatus -> {
            Platform.runLater(() -> {
                waitingPopUp.close();

                if(httpStatus.equals(HttpStatus.OK)) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Pracownik zostal zapisany do bazy danych :)",
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
