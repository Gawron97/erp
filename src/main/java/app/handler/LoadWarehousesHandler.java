package app.handler;

import app.dto.WarehouseDto;

import java.util.List;

public interface LoadWarehousesHandler {

    void handle(List<WarehouseDto> list);

}
