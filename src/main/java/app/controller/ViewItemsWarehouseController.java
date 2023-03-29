package app.controller;

import app.dto.ItemDto;
import app.dto.WarehouseDto;
import app.factory.PopUpFactory;
import app.handler.OnEndedAction;
import app.rest.WarehouseRestClient;
import app.table.ItemTableModel;
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
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ViewItemsWarehouseController implements Initializable {

    private static final String TRANSPORT_FXML = "/fxml/transport-item.fxml";
    private static final String URL_WAREHOUSE = "/fxml/warehouse.fxml";
    private static final String APP_FXML = "/fxml/app.fxml";

    private WarehouseRestClient warehouseRestClient;
    private PopUpFactory popUpFactory;

    @FXML
    private TableView<ItemTableModel> itemsTV;
    @FXML
    private Button exitButton;
    @FXML
    private Button transportButton;
    @FXML
    private TextField warehouseTF;

    private WarehouseTableModel warehouse;

    public ViewItemsWarehouseController(){
        warehouseRestClient = new WarehouseRestClient();
        popUpFactory = new PopUpFactory();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        warehouseTF.setEditable(false);
        initializeItemsTV();
        initializeTransportButton();
    }

    private void initializeTransportButton() {
        transportButton.setOnAction(actionEvent -> {
            try {

                Stage waitingPopUp = popUpFactory.createWaitingPopUp("wczytujemy dane potrzebne o przedmiocie i transporcie");
                waitingPopUp.show();

                Stage transportStage = new Stage();
                transportStage.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(TRANSPORT_FXML));
                Scene scene = new Scene(loader.load(), 1024, 768);
                transportStage.setScene(scene);

                TransportItemController transportItemController = loader.getController();

                transportItemController.loadTransportData(itemsTV.getSelectionModel().getSelectedItem().getIdItem(),
                        warehouse.getIdWarehouse(), isDone -> {
                    waitingPopUp.close();
                    if(isDone) {
                        transportStage.show();
                    } else {
                        Stage errorPopUp = popUpFactory.createErrorPopUp("Something went wrong with initializing data");
                        errorPopUp.show();
                    }

                });

            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void initializeItemsTV() {

        itemsTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("name"));

        TableColumn quantityColumn = new TableColumn("quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, Double>("quantity"));

        TableColumn quantityTypeColumn = new TableColumn("quantityType");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<ItemTableModel, String>("quantityType"));

        itemsTV.getColumns().addAll(nameColumn, quantityColumn, quantityTypeColumn);

    }

    public void loadData(WarehouseTableModel warehouse, OnEndedAction initializer){
        ObservableList<ItemTableModel> data = FXCollections.observableArrayList();

        this.warehouse = warehouse;

        warehouseRestClient.loadWarehouse(warehouse.getIdWarehouse(), response -> {
            Platform.runLater(() -> {
                if(!HttpStatus.OK.equals(response.getStatusCode())) {
                    initializer.action(false);
                } else {
                    WarehouseDto warehouseDto = (WarehouseDto) response.getBody();
                    List<ItemDto> itemDtoList = warehouseDto.getItems();
                    data.addAll(itemDtoList.stream().map(itemDto -> ItemTableModel.of(itemDto)).collect(Collectors.toList()));
                    itemsTV.setItems(data);
                    warehouseTF.setText(warehouseDto.getName());
                    initializer.action(true);
                }
            });

        });
    }


    public void initializeExitButton(Pane appPain){
        exitButton.setOnAction(actionEvent -> {
            appPain.getChildren().clear();

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource(URL_WAREHOUSE));
                BorderPane warehouseModule = new BorderPane(loader.load());
                appPain.getChildren().add(warehouseModule);

                WarehouseController warehouseController = loader.getController();
                warehouseController.initializeViewItemsButton(appPain);

            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }


}
