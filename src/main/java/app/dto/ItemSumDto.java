package app.dto;

import lombok.Data;

import java.util.List;

@Data
public class ItemSumDto {

    private Integer id;
    private String name;
    private double quantity;
    private String quantityType;
    private List<String> warehouseNames;


}
