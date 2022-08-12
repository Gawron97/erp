package app.dto;

import app.table.WarehouseTableModel;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseDto {

    private Integer idWarehouse;
    private String name;
    private AddressDto addressDto;
    private List<ItemDto> items;


}
