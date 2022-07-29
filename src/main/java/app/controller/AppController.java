package app.controller;

import app.dto.EmployeeDto;
import app.rest.EmployeeRestClient;
import app.table.EmployeeTableModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class AppController implements Initializable {

    private final EmployeeRestClient employeeRestClient;

    @FXML
    private TableView<EmployeeTableModel> employeeTableView;

    public AppController(){
        employeeRestClient = new EmployeeRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        employeeTableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("Name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String>("name"));

        TableColumn surnameColumn = new TableColumn("Surname");
        surnameColumn.setMinWidth(100);
        surnameColumn.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String>("surname"));

        TableColumn salaryColumn = new TableColumn("Salary");
        salaryColumn.setMinWidth(100);
        salaryColumn.setCellValueFactory(new PropertyValueFactory<EmployeeTableModel, String>("salary"));

        employeeTableView.getColumns().addAll(nameColumn, surnameColumn, salaryColumn);

        ObservableList<EmployeeTableModel> data = FXCollections.observableArrayList();

        prepareEmployeeData(data);

        employeeTableView.setItems(data);

    }


    private void prepareEmployeeData(ObservableList<EmployeeTableModel> data){

        Thread thread = new Thread(() -> {
            loadEmployees(data);
        });

        thread.start();

    }

    private void loadEmployees(ObservableList<EmployeeTableModel> data) {
        List<EmployeeDto> employees = employeeRestClient.getEmployees();
        data.addAll(employees.stream().map(employeeDto -> EmployeeTableModel.of(employeeDto)).collect(Collectors.toList()));
    }


}
