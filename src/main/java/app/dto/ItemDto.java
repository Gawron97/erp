package app.dto;

import lombok.Data;

@Data
public class ItemDto {

    private Integer idItem;
    private String name;
    private Double quantity;
    private String quantityType;

}
