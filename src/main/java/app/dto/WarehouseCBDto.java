package app.dto;

import lombok.Data;

@Data
public class WarehouseCBDto {

    private Integer idWarehouse;
    private String name;

    @Override
    public String toString(){
        return name;
    }

}
