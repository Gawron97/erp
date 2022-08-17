package app.rest;

import app.dto.ItemDto;
import app.dto.ItemSumDto;
import app.handler.ItemLoadingHandler;
import app.handler.LoadItemSumHandler;
import app.handler.LoadItemsHandler;
import app.handler.ProcessFinishedHandler;
import org.springframework.http.HttpStatus;
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

    public void saveItem(ItemDto itemDto, ProcessFinishedHandler handler) {

        Thread thread = new Thread(() -> processSaveItem(itemDto, handler));
        thread.start();

    }

    private void processSaveItem(ItemDto itemDto, ProcessFinishedHandler handler) {

        ResponseEntity<ItemDto> itemDtoResponse = restTemplate.postForEntity(ITEMS_URL, itemDto, ItemDto.class);

        if(HttpStatus.OK.equals(itemDtoResponse.getStatusCode()))
            handler.handle();
        else
            System.out.println("cos poszlo nie tak");

    }

    public void deleteItem(Integer idItem, ProcessFinishedHandler handler) {
        Thread thread = new Thread(() -> processDeleteItem(idItem, handler));
        thread.start();
    }

    private void processDeleteItem(Integer idItem, ProcessFinishedHandler handler) {

        String url = ITEMS_URL + "/" + idItem;

        restTemplate.delete(url);

        handler.handle();


    }

    public void loadItem(Integer idItem, ItemLoadingHandler handler) {
        Thread thread = new Thread(() -> processLoadItem(idItem, handler));
        thread.start();
    }

    private void processLoadItem(Integer idItem, ItemLoadingHandler handler) {

        String url = ITEMS_URL + "/" + idItem;

        ResponseEntity<ItemDto> itemResponse = restTemplate.getForEntity(url, ItemDto.class);

        handler.handle(itemResponse.getBody());


    }
}
