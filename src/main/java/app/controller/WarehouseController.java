package app.controller;

import app.factory.PopUpFactory;
import app.rest.WarehouseRestClient;
import app.table.EmployeeTableModel;
import app.table.WarehouseTableModel;
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
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

//view Items chce zeby pojawialo sie w oknie tym samym co warehouse(zamiast niego) dlatego implementacja otwierania
//napisana w appController, ktory jest odpowiedzialny za przypisywanie widoku do glownego panelu
//reszta, jak mp. add bedzie implementowana jako osobne okno tutaj
public class WarehouseController implements Initializable {

    private static final String VIEW_DETAILS_WAREHOUSE = "/fxml/view-details-warehouse.fxml";
    private static final String URL_WAREHOUSE_VIEW_ITEMS = "/fxml/view-items-warehouse.fxml";
    private static final String ADD_WAREHOUSE_FXML ="/fxml/add-warehouse.fxml";
    private static final String EDIT_WAREHOUSE_FXML ="/fxml/edit-warehouse.fxml";
    private static final String DELETE_WAREHOUSE_FXML ="/fxml/delete-warehouse.fxml";

    private PopUpFactory popUpFactory;
    private WarehouseRestClient warehouseRestClient;

    @FXML
    private Button addButton;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button deleteButton;

    @FXML
    private Button editButton;

    @FXML
    private Button refreshButton;

    @FXML
    private Button viewDetailsButton;

    @FXML
    private Button viewItemsButton;

    @FXML
    private TableView<WarehouseTableModel> warehousesTV;

    public WarehouseController(){
        popUpFactory = new PopUpFactory();
        warehouseRestClient = new WarehouseRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        initializeAddButton();
        initializeEditButton();
        initializeDeleteButton();
        initializeViewDetailsButton();
    }

    private void initializeAddButton() {

        addButton.setOnAction(actionEvent -> {
            try{
                Stage addStage = new Stage();
                Scene scene = new Scene(FXMLLoader.load(getClass().getResource(ADD_WAREHOUSE_FXML)), 500, 400);
                addStage.setScene(scene);
                addStage.initStyle(StageStyle.UNDECORATED);
                addStage.initModality(Modality.APPLICATION_MODAL);
                addStage.show();
            }catch (IOException e){
                e.printStackTrace();
            }
        });

    }

    private void initializeEditButton() {
        editButton.setOnAction(actionEvent -> {
            WarehouseTableModel selectedWarehouse = warehousesTV.getSelectionModel().getSelectedItem();
            if(selectedWarehouse == null)
                return;
            else {
                try {
                    Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o magazynie");
                    waitingPopUp.show();
                    Stage warehouseEdit = new Stage();
                    warehouseEdit.initStyle(StageStyle.UNDECORATED);
                    warehouseEdit.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(EDIT_WAREHOUSE_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    warehouseEdit.setScene(scene);
                    EditWarehouseController editController = loader.<EditWarehouseController>getController();
                    editController.loadWarehouseData(selectedWarehouse.getIdWarehouse(), () -> {
                        waitingPopUp.close();
                        warehouseEdit.show();
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeViewDetailsButton() {

        viewDetailsButton.setOnAction(actionEvent -> {
            WarehouseTableModel selectedWarehouse = warehousesTV.getSelectionModel().getSelectedItem();
            if (selectedWarehouse != null) {
                try {
                    Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o magazynie");
                    waitingPopUp.show();
                    Stage warehouseDetailsView = new Stage();
                    warehouseDetailsView.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_DETAILS_WAREHOUSE));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    warehouseDetailsView.setScene(scene);
                    ViewDetailsWarehouseController viewController = loader.getController();

                    viewController.loadData(selectedWarehouse, () -> {
                        waitingPopUp.close();
                        warehouseDetailsView.show();
                    });

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void initializeDeleteButton() {
        deleteButton.setOnAction(actionEvent -> {
            WarehouseTableModel selectedWarehouse = warehousesTV.getSelectionModel().getSelectedItem();
            if(selectedWarehouse == null)
                return;
            else {
                try {
                    Stage warehouseDelete = new Stage();
                    warehouseDelete.initStyle(StageStyle.UNDECORATED);
                    warehouseDelete.initModality(Modality.APPLICATION_MODAL);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource(DELETE_WAREHOUSE_FXML));
                    Scene scene = new Scene(loader.load(), 500, 400);
                    warehouseDelete.setScene(scene);
                    DeleteWarehouseController deleteController = loader.<DeleteWarehouseController>getController();
                    deleteController.setWarehouseIdToDelete(selectedWarehouse.getIdWarehouse());
                    warehouseDelete.show();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void initializeTableView() {

        warehousesTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<WarehouseTableModel, String>("name"));

        TableColumn cityColumn = new TableColumn("city");
        cityColumn.setMinWidth(100);
        cityColumn.setCellValueFactory(new PropertyValueFactory<WarehouseTableModel, String>("city"));

        TableColumn countryColumn = new TableColumn("country");
        countryColumn.setMinWidth(100);
        countryColumn.setCellValueFactory(new PropertyValueFactory<WarehouseTableModel, String>("country"));


        warehousesTV.getColumns().addAll(nameColumn, cityColumn, countryColumn);

        loadWarehouses();
    }

    private void loadWarehouses() {

        ObservableList<WarehouseTableModel> data = FXCollections.observableArrayList();

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o magazynach");
        waitingPopUp.show();

        warehouseRestClient.loadWarehouses(warehouses -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(warehouses.stream().map(warehouseDto -> WarehouseTableModel.of(warehouseDto)).collect(Collectors.toList()));
                warehousesTV.setItems(data);
                waitingPopUp.close();
            });

        });

    }

    public void initializeViewItemsButton(Pane appPain) {

        viewItemsButton.setOnAction(actionEvent -> {

            WarehouseTableModel warehouseTableModel = warehousesTV.getSelectionModel().getSelectedItem();
            if(warehouseTableModel == null)
                return;

            try{
                appPain.getChildren().clear();
                FXMLLoader loader2 = new FXMLLoader(getClass().getResource(URL_WAREHOUSE_VIEW_ITEMS));
                BorderPane itemsOfWarehouse = new BorderPane(loader2.load());
                appPain.getChildren().add(itemsOfWarehouse);

                Stage waitingPopUp = popUpFactory.createWaitingPopUp("Ladujemy dane o przedmiotach w wybranym magazynie");
                waitingPopUp.show();
                ViewItemsWarehouseController viewItemsWarehouseController = loader2.getController();
                viewItemsWarehouseController.loadData(warehouseTableModel, () -> {
                    waitingPopUp.close();
                });
                viewItemsWarehouseController.initializeExitButton(appPain);

            }catch (IOException e){
                e.printStackTrace();
            }
        });

    }
}
