package app.handler;

import app.dto.ItemSumDto;

import java.util.List;

public interface LoadItemSumHandler {

    void handle(List<ItemSumDto> list);

}
