package app.controller;

import app.dto.EmployeeDto;
import app.factory.PopUpFactory;
import app.handler.OnEndedAction;
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

public class EditEmployeeController implements Initializable {

    private EmployeesRestClient employeesRestClient;
    private PopUpFactory popUpFactory;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button cancelButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField salaryTextField;

    @FXML
    private Button editButton;

    @FXML
    private TextField surnameTextField;

    private Integer idEmployee;

    public EditEmployeeController(){
        employeesRestClient = new EmployeesRestClient();
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
        editButton.setOnAction(actionEvent -> saveNewEmployee());
    }

    private void saveNewEmployee() {
        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Zapisujemy w bazach danych");
        waitingPopUp.show();

        String name = nameTextField.getText();
        String pesel = peselTextField.getText();
        String surname = surnameTextField.getText();
        String salary = salaryTextField.getText();

        EmployeeDto employeeDto = EmployeeDto.of(name, pesel, surname, salary);
        employeeDto.setIdEmployee(idEmployee);

        employeesRestClient.saveEmployee(employeeDto, response -> {
            Platform.runLater(() -> {
                waitingPopUp.close();

                if(HttpStatus.OK.equals(response.getStatusCode())) {
                    Stage infoPopUp = popUpFactory.createInfoPopUp("Employee edited",
                            () -> getStage().close());
                    infoPopUp.show();
                }else {
                    Stage errorPopUp = popUpFactory.createErrorPopUp("Failure during editing employee", () -> getStage().close());
                    errorPopUp.show();
                }
            });
        });

    }

    public void loadEmployeeData(Integer idEmployee, OnEndedAction onEndedAction){
        employeesRestClient.loadEmployeeData(idEmployee, response -> {
            Platform.runLater(() -> {
                if(!HttpStatus.OK.equals(response.getStatusCode())) {
                    onEndedAction.action(false);
                } else {
                    EmployeeDto employeeDto = (EmployeeDto) response.getBody();
                    this.idEmployee = employeeDto.getIdEmployee();
                    peselTextField.setText(employeeDto.getPesel());
                    nameTextField.setText(employeeDto.getName());
                    surnameTextField.setText(employeeDto.getSurname());
                    salaryTextField.setText(employeeDto.getSalary());
                    onEndedAction.action(true);
                }
            });
        });
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }
}
