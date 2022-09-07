package app.dto;

import lombok.Data;

@Data
public class StockItemDto {

    private Integer idStockItem;
    private String name;
    private double price;
    private QuantityTypeDto quantityTypeDto;

}
