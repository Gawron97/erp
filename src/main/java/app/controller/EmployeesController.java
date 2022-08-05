package app.controller;

import app.factory.PopUpFactory;
import app.rest.EmployeesRestClient;
import app.table.EmployeeTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class EmployeesController implements Initializable {

    private final EmployeesRestClient employeesRestClient;
    private static final String ADD_FXML = "/fxml/add-employee.fxml";
    private static final String VIEW_FXML = "/fxml/view-employee.fxml";
    private static final String EDIT_FXML = "/fxml/edit-employee.fxml";
    private static final String DELETE_FXML = "/fxml/delete-employee.fxml";
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

    public EmployeesController(){
        employeesRestClient = new EmployeesRestClient();
        popUpFactory = new PopUpFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        initializeTable();
        initializeAddButton();
        initializeRefreshButton();
        initializeViewButton();
        initializeEditButton();
        initializeDeleteButton();

    }

    private void initializeEditButton() {
        editButton.setOnAction(actionEvent -> {
            EmployeeTableModel selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee == null)
                return;
            else {
                try {
                    Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o pracowniku");
                    waitingPopUp.show();
                    Stage employeeView = new Stage();
                    employeeView.initStyle(StageStyle.UNDECORATED);
                    employeeView.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    employeeView.setScene(scene);
                    EditEmployeeController editController = loader.<EditEmployeeController>getController();
                    editController.loadEmployeeData(selectedEmployee.getIdEmployee(), () -> {
                        waitingPopUp.close();
                        employeeView.show();
                    });


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(actionEvent -> {
            EmployeeTableModel selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee == null)
                return;
            else {
                try {
                    Stage employeeDelete = new Stage();
                    employeeDelete.initStyle(StageStyle.UNDECORATED);
                    employeeDelete.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(DELETE_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    employeeDelete.setScene(scene);
                    DeleteEmployeeController deleteController = loader.<DeleteEmployeeController>getController();
                    deleteController.setEmployeeToDelete(selectedEmployee.getIdEmployee());
                    employeeDelete.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeViewButton() {
        viewButton.setOnAction(actionEvent -> {
            EmployeeTableModel selectedEmployee = employeeTableView.getSelectionModel().getSelectedItem();
            if(selectedEmployee == null)
                return;
            else {
                try {
                    Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o pracowniku");
                    waitingPopUp.show();
                    Stage employeeView = new Stage();
                    employeeView.initStyle(StageStyle.UNDECORATED);
                    employeeView.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    employeeView.setScene(scene);
                    ViewEmployeeController viewController = loader.<ViewEmployeeController>getController();
                    viewController.loadEmployeeData(selectedEmployee.getIdEmployee(), () -> {
                        waitingPopUp.close();
                        employeeView.show();
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeRefreshButton() {
        refreshButton.setOnAction(actionEvent -> {
            loadEmployees();
        });
    }

    private void initializeAddButton() {
        addButton.setOnAction(actionEvent -> {
            try{
                Stage addStage = new Stage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource(ADD_FXML)), 500, 400);
                addStage.setScene(scene);
                addStage.initStyle(StageStyle.UNDECORATED);
                addStage.initModality(Modality.APPLICATION_MODAL);
                addStage.show();
            }catch (IOException e){
                e.printStackTrace();
            }


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
        employeesRestClient.loadEmployees(employees -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(employees.stream().map(employeeDto -> EmployeeTableModel.of(employeeDto)).collect(Collectors.toList()));
                stage.close();
                employeeTableView.setItems(data);
            });
        });

    }

}
