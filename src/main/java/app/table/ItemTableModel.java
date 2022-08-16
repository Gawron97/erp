package app.table;

import app.dto.ItemDto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemTableModel {

    private final Integer idItem;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty quantity;
    private final SimpleStringProperty quantityType;

    public ItemTableModel(Integer idItem, String name, Double quantity, String quantityType){

        this.idItem = idItem;
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.quantityType = new SimpleStringProperty(quantityType);

    }

    public static ItemTableModel of(ItemDto itemDto){
        ItemTableModel item = new ItemTableModel(itemDto.getIdItem(), itemDto.getName(), itemDto.getQuantity(),
                itemDto.getQuantityTypeDto().getName());

        return item;
    }

    public Integer getIdItem() {
        return idItem;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public double getQuantity() {
        return quantity.get();
    }

    public SimpleDoubleProperty quantityProperty() {
        return quantity;
    }

    public String getQuantityType() {
        return quantityType.get();
    }

    public SimpleStringProperty quantityTypeProperty() {
        return quantityType;
    }
}
