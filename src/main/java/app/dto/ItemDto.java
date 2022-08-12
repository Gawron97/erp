package app.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Integer idItem;
    private String name;
    private double quantity;
    private String quantityType;
    private String warehouseName;

    public static ItemDto of(String name, double quantity, String quantityType, String warehouseName){
        ItemDto dto = new ItemDto();

        dto.name = name;
        dto.quantity = quantity;
        dto.quantityType = quantityType;
        dto.warehouseName = warehouseName;

        return dto;
    }

}
