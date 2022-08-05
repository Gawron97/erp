package app.rest;

import app.dto.ItemDto;
import app.dto.WarehouseDto;
import app.handler.LoadItemsHandler;
import app.handler.LoadWarehousesHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ItemRestClient {

    private static final String ITEMS_URL = "http://localhost:8080/items";

    private final RestTemplate restTemplate;

    public ItemRestClient(){
        restTemplate = new RestTemplate();
    }

    public void loadItems(LoadItemsHandler handler){
        Thread thread = new Thread(() -> processloadWarehouses(handler));
        thread.start();
    }

    public void processloadWarehouses(LoadItemsHandler handler){

        ResponseEntity<ItemDto[]> responseItems = restTemplate.getForEntity(ITEMS_URL, ItemDto[].class);

        System.out.println(Arrays.stream(responseItems.getBody()).toList().get(0));

        handler.handle(Arrays.stream(responseItems.getBody()).toList());

    }

}
