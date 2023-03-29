package app.rest;

import app.dto.ItemDto;
import app.dto.ItemSumDto;
import app.dto.TransportDto;
import app.dto.TransportItemDto;
import app.handler.*;
import app.util.config.CustomResponseErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class ItemRestClient {

    private static final String ITEMS_URL = "http://localhost:8080/items";
    private static final String ITEMS_SUM_URL = "http://localhost:8080/items_sum";
    private static final String TRANSPORT_URL = "http://localhost:8080/transport";

    private final RestTemplate restTemplate;

    public ItemRestClient(){
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }

    public void loadItems(LoadItemsHandler handler){
        Thread thread = new Thread(() -> processLoadItems(handler));
        thread.start();
    }

    public void processLoadItems(LoadItemsHandler handler){

        ResponseEntity<ItemDto[]> responseItems = restTemplate.getForEntity(ITEMS_URL, ItemDto[].class);

        handler.handle(Arrays.stream(responseItems.getBody()).toList());

    }

    public void loadItemsSum(ProcessFinishedHandler handler){
        Thread thread = new Thread(() -> processloadItemsSum(handler));
        thread.start();
    }

    public void processloadItemsSum(ProcessFinishedHandler handler){

        ResponseEntity<ItemSumDto[]> responseItems = restTemplate.getForEntity(ITEMS_SUM_URL, ItemSumDto[].class);

        handler.handle(responseItems);

    }

    public void loadItemSum(Integer idItemSum, ProcessFinishedHandler handler){
        Thread thread = new Thread(() -> processLoadItemSum(idItemSum, handler));
        thread.start();
    }

    private void processLoadItemSum(Integer idItemSum, ProcessFinishedHandler handler) {

        String url = ITEMS_SUM_URL + "/" + idItemSum;

        ResponseEntity<ItemSumDto> responseItemSum = restTemplate.getForEntity(url, ItemSumDto.class);

        handler.handle(responseItemSum);

    }

    public void saveItem(ItemDto itemDto, ProcessFinishedHandler handler) {

        Thread thread = new Thread(() -> processSaveItem(itemDto, handler));
        thread.start();

    }

    private void processSaveItem(ItemDto itemDto, ProcessFinishedHandler handler) {

        ResponseEntity<ItemDto> itemDtoResponse = restTemplate.postForEntity(ITEMS_URL, itemDto, ItemDto.class);

        handler.handle(itemDtoResponse);

    }

    public void transportItem(TransportItemDto dto, ProcessFinishedHandler handler){

        Thread thread = new Thread(() -> processtransportItem(dto, handler));
        thread.start();

    }

    private void processtransportItem(TransportItemDto dto, ProcessFinishedHandler handler) {

        ResponseEntity<ResponseEntity> responseEntity = restTemplate.postForEntity(TRANSPORT_URL, dto, ResponseEntity.class);

        handler.handle(responseEntity);

    }

    public void deleteItem(Integer idItem, ProcessFinishedHandler handler) {
        Thread thread = new Thread(() -> processDeleteItem(idItem, handler));
        thread.start();
    }

    private void processDeleteItem(Integer idItem, ProcessFinishedHandler handler) {

        String url = ITEMS_URL + "/" + idItem;
        restTemplate.delete(url);
        handler.handle(ResponseEntity.ok().build());

    }

    public void loadTransportData(Integer idItem, ProcessFinishedHandler handler) {
        Thread thread = new Thread(() -> processLoadingTransportData(idItem, handler));
        thread.start();
    }

    private void processLoadingTransportData(Integer idItem, ProcessFinishedHandler handler) {

        String url = TRANSPORT_URL + "/" + idItem;

        ResponseEntity<TransportDto> itemResponse = restTemplate.getForEntity(url, TransportDto.class);

        handler.handle(itemResponse);
    }
}
