package app.controller;

import app.dto.EmployeeDto;
import app.factory.PopUpFactory;
import app.rest.EmployeeRestClient;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AddEmployeeController implements Initializable {

    private PopUpFactory popUpFactory;
    private EmployeeRestClient employeeRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField salaryTextField;

    @FXML
    private Button saveButton;

    @FXML
    private TextField surnameTextField;

    public AddEmployeeController(){
        popUpFactory = new PopUpFactory();
        employeeRestClient = new EmployeeRestClient();
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
        Stage stage = popUpFactory.createWaitingPopUp("Zapisujemy w bazach danych");
        stage.show();

        String name = nameTextField.getText();
        String surname = surnameTextField.getText();
        String salary = salaryTextField.getText();

        EmployeeDto employeeDto = EmployeeDto.of(name, surname, salary);

        employeeRestClient.saveEmployee(employeeDto, () -> {
            stage.close();
            Stage infoPopUp = popUpFactory.createInfoPopUp("Pracownik zostal zapisany do bazy danych :)", () -> {
                getStage().close();
            });
            infoPopUp.show();
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
