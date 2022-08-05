package app.table;

import app.dto.WarehouseDto;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class WarehouseTableModel {

    private final Integer idWarehouse;
    private final SimpleStringProperty name;
    private final SimpleStringProperty city;
    private final SimpleStringProperty country;

    public WarehouseTableModel(Integer idWarehouse, String name, String city, String country){
        this.idWarehouse = idWarehouse;
        this.name = new SimpleStringProperty(name);
        this.city = new SimpleStringProperty(city);
        this.country = new SimpleStringProperty(country);
    }

    public static WarehouseTableModel of(WarehouseDto dto){
        return new WarehouseTableModel(dto.getIdWarehouse(), dto.getName(), dto.getAddressDto().getCity(),
                dto.getAddressDto().getCountryDto().getCountry());
    }

    public Integer getIdWarehouse() {
        return idWarehouse;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public String getCountry() {
        return country.get();
    }

    public SimpleStringProperty countryProperty() {
        return country;
    }

    public String getCity() {
        return city.get();
    }

    public SimpleStringProperty cityProperty() {
        return city;
    }

    //w przyszlosci dodanie ilosci rodzajow itemow albo zapelnienie magazynu

}
