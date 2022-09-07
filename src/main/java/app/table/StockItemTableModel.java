package app.table;

import app.dto.QuantityTypeDto;
import app.dto.StockItemDto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class StockItemTableModel {

    private final Integer idStockItem;
    private final Integer idQuantityType;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty price;
    private final SimpleStringProperty quantityType;

    public StockItemTableModel(Integer idStockItem, String name, double price, QuantityTypeDto quantityTypeDto){
        this.idStockItem = idStockItem;
        this.name = new SimpleStringProperty(name);
        this.price = new SimpleDoubleProperty(price);
        this.idQuantityType = quantityTypeDto.getIdQuantityType();
        this.quantityType = new SimpleStringProperty(quantityTypeDto.getName());
    }

    public static StockItemTableModel of(StockItemDto dto){
        return new StockItemTableModel(dto.getIdStockItem(), dto.getName(), dto.getPrice(), dto.getQuantityTypeDto());
    }

    public Integer getIdStockItem() {
        return idStockItem;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public double getPrice() {
        return price.get();
    }

    public SimpleDoubleProperty priceProperty() {
        return price;
    }

    public Integer getIdQuantityType() {
        return idQuantityType;
    }

    public String getQuantityType() {
        return quantityType.get();
    }

    public SimpleStringProperty quantityTypeProperty() {
        return quantityType;
    }
}
