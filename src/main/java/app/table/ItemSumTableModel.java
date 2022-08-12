package app.table;

import app.dto.ItemSumDto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class ItemSumTableModel {

    private final Integer idItemSum;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty quantity;
    private final SimpleStringProperty quantityType;

    public ItemSumTableModel(Integer idItemSum, String name, double quantity, String quantityType) {
        this.idItemSum = idItemSum;
        this.name = new SimpleStringProperty(name);
        this.quantity = new SimpleDoubleProperty(quantity);
        this.quantityType = new SimpleStringProperty(quantityType);
    }

    public static ItemSumTableModel of(ItemSumDto dto){
        return new ItemSumTableModel(dto.getId(), dto.getName(), dto.getQuantity(), dto.getQuantityType());
    }

    public Integer getIdItemSum() {
        return idItemSum;
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
