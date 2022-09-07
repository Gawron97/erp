package app.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Integer idItem;
    private String name;
    private double quantity;
    private double price;
    private QuantityTypeDto quantityTypeDto;
    private Integer idWarehouse;

    public static ItemDto of(String name, double quantity, double price, QuantityTypeDto quantityTypeDto, Integer idWarehouse){
        ItemDto dto = new ItemDto();

        dto.name = name;
        dto.quantity = quantity;
        dto.price = price;
        dto.quantityTypeDto = quantityTypeDto;
        dto.idWarehouse = idWarehouse;

        return dto;
    }

}
