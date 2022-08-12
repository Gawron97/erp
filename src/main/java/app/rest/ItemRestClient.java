package app.rest;

import app.dto.ItemDto;
import app.dto.ItemSumDto;
import app.handler.LoadItemSumHandler;
import app.handler.LoadItemsHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ItemRestClient {

    private static final String ITEMS_URL = "http://localhost:8080/items";
    private static final String ITEMS_SUM_URL = "http://localhost:8080/items_sum";

    private final RestTemplate restTemplate;

    public ItemRestClient(){
        restTemplate = new RestTemplate();
    }

    public void loadItems(LoadItemsHandler handler){
        Thread thread = new Thread(() -> processloadItems(handler));
        thread.start();
    }

    public void processloadItems(LoadItemsHandler handler){

        ResponseEntity<ItemDto[]> responseItems = restTemplate.getForEntity(ITEMS_URL, ItemDto[].class);

        handler.handle(Arrays.stream(responseItems.getBody()).toList());

    }

    public void loadItemsSum(LoadItemSumHandler handler){
        Thread thread = new Thread(() -> processloadItemsSum(handler));
        thread.start();
    }

    public void processloadItemsSum(LoadItemSumHandler handler){

        ResponseEntity<ItemSumDto[]> responseItems = restTemplate.getForEntity(ITEMS_SUM_URL, ItemSumDto[].class);

        handler.handle(Arrays.stream(responseItems.getBody()).toList());

    }

}
