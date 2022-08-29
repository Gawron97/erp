package app.rest;

import app.dto.ItemDto;
import app.dto.QuantityTypeDto;
import app.dto.WarehouseCBDto;
import app.dto.WarehouseDto;
import app.handler.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class WarehouseRestClient {

    private static final String WAREHOUSES_URL = "http://localhost:8080/warehouses";
    private static final String WAREHOUSES_CB_URL = "http://localhost:8080/warehouses_cb";

    private final RestTemplate restTemplate;

    public WarehouseRestClient(){
        restTemplate = new RestTemplate();
    }

    public void loadWarehouses(LoadWarehouseItemsHandler handler){
        Thread thread = new Thread(() -> processloadWarehouses(handler));
        thread.start();
    }

    public void processloadWarehouses(LoadWarehouseItemsHandler handler){

        ResponseEntity<WarehouseDto[]> responseWarehouses = restTemplate.getForEntity(WAREHOUSES_URL, WarehouseDto[].class);

        handler.handle(Arrays.asList(responseWarehouses.getBody()));

    }

    public void loadWarehouse(Integer idWarehouse, LoadWarehouseHandler handler){
        Thread thread = new Thread(() -> processloadingWarehouse(idWarehouse, handler));
        thread.start();
    }

    private void processloadingWarehouse(Integer idWarehouse, LoadWarehouseHandler handler) {

        String url = WAREHOUSES_URL + "/" + idWarehouse;

        ResponseEntity<WarehouseDto> warehouseResponse = restTemplate.getForEntity(url, WarehouseDto.class);

        if(HttpStatus.OK.equals(warehouseResponse.getStatusCode()))
            handler.handle(warehouseResponse.getBody());

    }

    public void loadWarehousesToCB(LoadWarehousesCBHandler handler){
        Thread thread = new Thread(() -> processLoadWarehousesToCB(handler));
        thread.start();
    }

    private void processLoadWarehousesToCB(LoadWarehousesCBHandler handler) {
        ResponseEntity<WarehouseCBDto[]> warehouseCBResponse = restTemplate.getForEntity(WAREHOUSES_CB_URL,
                WarehouseCBDto[].class);

        if(HttpStatus.OK.equals(warehouseCBResponse.getStatusCode()))
            handler.handle(Arrays.stream(warehouseCBResponse.getBody()).toList());
        else
            throw new RuntimeException("cos poszlo nie tak");

    }


}
