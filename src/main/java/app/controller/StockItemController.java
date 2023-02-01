package app.controller;

import app.factory.PopUpFactory;
import app.rest.StockItemRestClient;
import app.table.ItemSumTableModel;
import app.table.StockItemTableModel;
import app.table.WarehouseTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
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
import java.util.List;
import java.util.ResourceBundle;

public class StockItemController implements Initializable {

    private static final String BUY_ITEM_URL = "/fxml/buy-item.fxml";

    private StockItemRestClient stockItemRestClient;
    private PopUpFactory popUpFactory;

    public StockItemController(){
        stockItemRestClient = new StockItemRestClient();
        popUpFactory = new PopUpFactory();
    }

    @FXML
    private Button buyButton;

    @FXML
    private TableView<StockItemTableModel> stockItemTV;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        initializeStockItemTV();
        initializeBuyButton();
    }

    private void initializeBuyButton() {
        buyButton.setOnAction(actionEvent -> {
            try{

                Stage waitingPopUp = popUpFactory.createWaitingPopUp("ladujemy okno kupowania");
                waitingPopUp.show();

                Stage buyItem = new Stage();
                buyItem.initModality(Modality.APPLICATION_MODAL);
                buyItem.initStyle(StageStyle.UNDECORATED);

                FXMLLoader loader = new FXMLLoader(getClass().getResource(BUY_ITEM_URL));
                Scene scene = new Scene(loader.load(), 500, 400);
                buyItem.setScene(scene);

                BuyItemController buyItemController = loader.getController();
                buyItemController.loadData(stockItemTV.getSelectionModel().getSelectedItem(), () -> {
                    waitingPopUp.close();
                    buyItem.show();
                });

            }catch (IOException e){
                e.printStackTrace();
            }
        });
    }

    private void initializeStockItemTV() {

        stockItemTV.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        TableColumn nameColumn = new TableColumn("name");
        nameColumn.setMinWidth(100);
        nameColumn.setCellValueFactory(new PropertyValueFactory<StockItemTableModel, String>("name"));

        TableColumn priceColumn = new TableColumn("price");
        priceColumn.setMinWidth(100);
        priceColumn.setCellValueFactory(new PropertyValueFactory<StockItemTableModel, Double>("price"));

        TableColumn quantityTypeColumn = new TableColumn("quantityType");
        quantityTypeColumn.setMinWidth(100);
        quantityTypeColumn.setCellValueFactory(new PropertyValueFactory<StockItemTableModel, String>("quantityType"));

        stockItemTV.getColumns().addAll(nameColumn, priceColumn, quantityTypeColumn);

        loadStockItems();
    }

    private void loadStockItems() {

        stockItemRestClient.loadStockItems(stockItemDtoList -> {
            Platform.runLater(() -> {
                System.out.println(stockItemDtoList.get(0).getPrice());
                stockItemTV.setItems(FXCollections.observableList(stockItemDtoList
                        .stream().map(stockItemDto -> StockItemTableModel.of(stockItemDto)).toList()));
            });
        });

    }
}
