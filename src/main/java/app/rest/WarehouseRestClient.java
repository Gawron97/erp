package app.rest;

import app.dto.ItemDto;
import app.dto.WarehouseDto;
import app.handler.ItemsLoadingHandler;
import app.handler.LoadWarehouseHandler;
import app.handler.LoadWarehouseItemsHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class WarehouseRestClient {

    private static final String WAREHOUSES_URL = "http://localhost:8080/warehouses";

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


}
