package app.table;

import app.dto.TruckDto;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class TruckTableModel {

    private final Integer idTruck;
    private final SimpleStringProperty name;
    private final SimpleDoubleProperty capacity;

    public TruckTableModel(Integer idTruck, String name, double capacity){
        this.idTruck = idTruck;
        this.name = new SimpleStringProperty(name);
        this.capacity = new SimpleDoubleProperty(capacity);
    }

    public static TruckTableModel of(TruckDto truckDto){
        return new TruckTableModel(truckDto.getIdTruck(), truckDto.getName(), truckDto.getCapacity());
    }

    public Integer getIdTruck() {
        return idTruck;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public double getCapacity() {
        return capacity.get();
    }

    public SimpleDoubleProperty capacityProperty() {
        return capacity;
    }
}

