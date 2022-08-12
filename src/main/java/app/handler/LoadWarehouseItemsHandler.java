package app.handler;

import app.dto.WarehouseDto;

import java.util.List;

public interface LoadWarehouseItemsHandler {

    void handle(List<WarehouseDto> list);

}
