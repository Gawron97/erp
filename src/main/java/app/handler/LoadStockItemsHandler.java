package app.handler;

import app.dto.StockItemDto;

import java.util.List;

public interface LoadStockItemsHandler {

    void handle(List<StockItemDto> stockItemDtoList);

}
