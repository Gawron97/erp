package app.handler;

import app.dto.ItemDto;

public interface ItemLoadingHandler {

    void handle(ItemDto itemDto);

}
