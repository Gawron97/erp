package app.handler;

import app.dto.WarehouseCBDto;

import java.util.List;

public interface LoadWarehousesCBHandler {

    void handle(List<WarehouseCBDto> warehouseCBDtos);

}
