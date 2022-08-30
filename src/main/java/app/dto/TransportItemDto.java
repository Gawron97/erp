package app.dto;

import lombok.Data;

@Data
public class TransportItemDto {

    private Integer idItem;
    private double quantityToSend;
    private String transportationType;
    private Integer newWarehouseId;
    private Integer idTruck;

    public static TransportItemDto of(Integer idItem, double quantityToSend, String transportationType, Integer newWarehouseId,
                                      Integer idTruck){

        TransportItemDto dto = new TransportItemDto();

        dto.idItem = idItem;
        dto.quantityToSend = quantityToSend;
        dto.transportationType = transportationType;
        dto.newWarehouseId = newWarehouseId;
        dto.idTruck = idTruck;

        return dto;
    }

}
