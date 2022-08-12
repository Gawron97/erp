package app.handler;

import app.dto.ItemDto;

import java.util.List;

public interface ItemsLoadingHandler {

    void handle(List<ItemDto> list);

}
