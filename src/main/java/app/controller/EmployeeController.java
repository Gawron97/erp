package app.controller;

import app.dto.EmployeeDto;
import app.factory.PopUpFactory;
import app.rest.EmployeeRestClient;
import app.table.EmployeeTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.skin.TableHeaderRow;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeeController implements Initializable {

    private final EmployeeRestClient employeeRestClient;
    private static final String ADD_FXML = "/fxml/add-employee.fxml";
    private PopUpFactory popUpFactory;

    @FXML
    private Button addButton;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button viewButton;

    @FXML
    private Button refreshButton;

    @FXML
    private TableView<EmployeeTableModel> employeeTableView;

    public EmployeeController(){
        employeeRestClient = new EmployeeRestClient();
        popUpFactory = new PopUpFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTable();
        initializeAddButton();
        initializeRefreshButton();

    }

    private void initializeRefreshButton() {
        refreshButton.setOnAction(actionEvent -> {
            loadEmployees();
        });
    }

    private void initializeAddButton() {
        addButton.setOnAction(actionEvent -> {
            Stage addStage = new Stage();
            try{
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource(ADD_FXML)), 500, 400);
                addStage.setScene(scene);
            }catch (IOException e){
                e.printStackTrace();
            }
            addStage.initStyle(StageStyle.UNDECORATED);
            addStage.initModality(Modality.APPLICATION_MODAL);
            addStage.show();

        });
    }

    private void initializeTable(){
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

        loadEmployees();

    }


    private void loadEmployees() {

        ObservableList<EmployeeTableModel> data = FXCollections.observableArrayList();

        Stage stage = popUpFactory.createWaitingPopUp("Pobieranie danych o pracownikach");
        stage.show();
        employeeRestClient.load((employees) -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(employees.stream().map(employeeDto -> EmployeeTableModel.of(employeeDto)).collect(Collectors.toList()));
                stage.close();
                employeeTableView.setItems(data);
            });
        });

    }

}
