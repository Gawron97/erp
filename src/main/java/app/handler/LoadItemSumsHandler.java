package app.handler;

import app.dto.ItemSumDto;

import java.util.List;

public interface LoadItemSumsHandler {

    void handle(List<ItemSumDto> list);

}
