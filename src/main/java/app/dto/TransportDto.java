package app.dto;

import lombok.Data;

import java.util.List;

@Data
public class TransportDto {

    private Integer idItem;
    private String name;
    private String warehouseName;
    private double quantity;
    private QuantityTypeDto quantityTypeDto;
    private List<TruckDto> truckDtoList;


}
