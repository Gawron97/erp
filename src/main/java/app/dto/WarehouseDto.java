package app.dto;

import lombok.Data;

@Data
public class WarehouseDto {

    private Integer idWarehouse;
    private String name;
    private AddressDto addressDto;


}
