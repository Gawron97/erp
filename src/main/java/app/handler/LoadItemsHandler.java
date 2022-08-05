package app.handler;

import app.dto.ItemDto;

import java.util.List;

public interface LoadItemsHandler {

    void handle(List<ItemDto> list);

}
