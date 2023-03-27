package app.controller;

import app.handler.ButtonInitializer;
import app.handler.ProcessFinishedHandler;
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

public class ViewEmployeeController implements Initializable {

    private EmployeesRestClient employeesRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button okButton;

    @FXML
    private TextField salaryTextField;

    @FXML
    private TextField surnameTextField;

    public ViewEmployeeController(){
        employeesRestClient = new EmployeesRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameTextField.setEditable(false);
        surnameTextField.setEditable(false);
        salaryTextField.setEditable(false);
        initializeOkButton();

    }

    public void loadEmployeeData(Integer idEmployee, ButtonInitializer initializer){
        employeesRestClient.loadEmployeeData(idEmployee, employeeDto -> {
            Platform.runLater(() -> {
                nameTextField.setText(employeeDto.getName());
                surnameTextField.setText(employeeDto.getSurname());
                salaryTextField.setText(employeeDto.getSalary());
                initializer.init();
            });
        });
    }

    private void initializeOkButton() {
        okButton.setOnAction(actionEvent -> {
            getStage().close();
        });
    }

    private Stage getStage(){
        return ((Stage) borderPane.getScene().getWindow());
    }

}
