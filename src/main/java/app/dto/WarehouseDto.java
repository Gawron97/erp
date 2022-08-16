package app.dto;

import lombok.Data;

import java.util.List;

@Data
public class WarehouseDto {

    private Integer idWarehouse;
    private String name;
    private AddressDto addressDto;
    private List<ItemDto> items;


}
