package app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
public class WarehouseDto {

    private Integer idWarehouse;
    private String name;
    private AddressDto addressDto;
    private List<ItemDto> items;

    public static WarehouseDto of(String name, AddressDto addressDto){
        WarehouseDto warehouseDto = new WarehouseDto();
        warehouseDto.setName(name);
        warehouseDto.setAddressDto(addressDto);
        return warehouseDto;
    }


}
