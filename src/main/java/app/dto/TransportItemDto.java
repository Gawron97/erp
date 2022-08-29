package app.dto;

import lombok.Data;

@Data
public class TransportItemDto {

    private Integer id;
    private String name;
    private double quantityToSend;
    private QuantityTypeDto quantityTypeDto;
    private String transportationType;
    private Integer newWarehouseId;

    public static TransportItemDto of(Integer id, String name, double newQuantity,
                                      QuantityTypeDto quantityTypeDto, String transportationType, Integer newWarehouseId){

        TransportItemDto dto = new TransportItemDto();

        dto.id = id;
        dto.name = name;
        dto.quantityToSend = newQuantity;
        dto.quantityTypeDto = quantityTypeDto;
        dto.transportationType = transportationType;
        dto.newWarehouseId = newWarehouseId;

        return dto;
    }

}
