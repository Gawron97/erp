package app.controller;

import app.handler.ProcessFinishedHandler;
import app.rest.ItemRestClient;
import app.table.ItemSumTableModel;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ViewItemSumController implements Initializable {

    private ItemRestClient itemRestClient;

    @FXML
    private TextField nameTF;

    @FXML
    private TextField quantityTF;

    @FXML
    private TextField quantityTypeTF;

    @FXML
    private ListView<String> warehousesLV;

    public ViewItemSumController(){
        itemRestClient = new ItemRestClient();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }


    public void loadItemSumData(ItemSumTableModel itemSumTableModel, ProcessFinishedHandler handler) {

        nameTF.setEditable(false);
        quantityTypeTF.setEditable(false);
        quantityTF.setEditable(false);

        itemRestClient.loadItemSum(itemSumTableModel.getIdItemSum(), itemSumDto -> {
            Platform.runLater(() -> {
                nameTF.setText(itemSumDto.getName());
                quantityTypeTF.setText(itemSumDto.getQuantityType());
                quantityTF.setText(Double.toString(itemSumDto.getQuantity()));
                warehousesLV.setItems(FXCollections.observableList(itemSumDto.getWarehouseNames()));
                handler.handle();
            });
        });

    }
}
