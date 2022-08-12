package app.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Integer idItem;
    private String name;
    private double quantity;
    private String quantityType;
    private String warehouseName;

}
