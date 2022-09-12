package app.controller;

import app.factory.PopUpFactory;
import app.rest.ItemRestClient;
import app.table.ItemSumTableModel;
import app.table.ItemTableModel;
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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class ItemController implements Initializable {

    private static final String VIEW_ITEM_SUM_FXML  = "/fxml/view-item-sum.fxml";

    private PopUpFactory popUpFactory;
    private ItemRestClient itemRestClient;

    @FXML
    private BorderPane borderPane;

    @FXML
    private Button viewButton;

    @FXML
    private TableView<ItemSumTableModel> itemSumsTV;

    public ItemController(){
        popUpFactory = new PopUpFactory();
        itemRestClient = new ItemRestClient();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeTableView();
        initializeViewButton();
    }

    private void initializeTableView() {
        itemSumsTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<ItemSumTableModel, String>("name"));

        TableColumn quantityColumn = new TableColumn("quantity");
        quantityColumn.setMinWidth(100);
        quantityColumn.setCellValueFactory(new PropertyValueFactory<ItemSumTableModel, Double>("quantity"));

        TableColumn quantityTypeColumn = new TableColumn("quantityType");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<ItemSumTableModel, String>("quantityType"));

        itemSumsTV.getColumns().addAll(nameColumn, quantityColumn, quantityTypeColumn);

        loadItems();

    }

    private void loadItems() {
        ObservableList<ItemSumTableModel> data = FXCollections.observableArrayList();

        Stage waitingPopUp = popUpFactory.createWaitingPopUp("Pobieranie danych o przedmiotach");
        waitingPopUp.show();

        itemRestClient.loadItemsSum(itemSums -> {
            Platform.runLater(() -> {
                data.clear();
                data.addAll(itemSums.stream().map(itemSumDto -> ItemSumTableModel.of(itemSumDto)).collect(Collectors.toList()));
                itemSumsTV.setItems(data);
                waitingPopUp.close();
            });

        });


    }

    private void initializeViewButton() {

        viewButton.setOnAction(actionEvent -> {

            ItemSumTableModel itemSumTableModel = itemSumsTV.getSelectionModel().getSelectedItem();
            if(itemSumTableModel == null)
                return;

            try{

                Stage waitingPopUp = popUpFactory.createWaitingPopUp("ladujemy informacje o przedmiocie");
                waitingPopUp.show();

                Stage viewItemSum = new Stage();
                viewItemSum.initModality(Modality.APPLICATION_MODAL);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(VIEW_ITEM_SUM_FXML));
                Scene scene = new Scene(loader.load(), 500, 400);

                ViewItemSumController viewItemSumController = loader.getController();
                viewItemSumController.loadItemSumData(itemSumTableModel, () -> {
                    viewItemSum.setScene(scene);
                    waitingPopUp.close();
                    viewItemSum.show();
                });

            }catch (IOException e){
                e.printStackTrace();
            }
        });

    }

}
