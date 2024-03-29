package app.rest;

import app.dto.WarehouseCBDto;
import app.dto.WarehouseDto;
import app.handler.*;
import app.util.config.CustomResponseErrorHandler;
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
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }

    public void loadWarehouses(LoadWarehouseItemsHandler handler){
        Thread thread = new Thread(() -> processLoadWarehouses(handler));
        thread.start();
    }

    public void processLoadWarehouses(LoadWarehouseItemsHandler handler){

        ResponseEntity<WarehouseDto[]> responseWarehouses = restTemplate.getForEntity(WAREHOUSES_URL, WarehouseDto[].class);

        handler.handle(responseWarehouses);

    }

    public void loadWarehouse(Integer idWarehouse, ProcessFinishedHandler handler){
        Thread thread = new Thread(() -> processLoadWarehouse(idWarehouse, handler));
        thread.start();
    }

    private void processLoadWarehouse(Integer idWarehouse, ProcessFinishedHandler handler) {

        String url = WAREHOUSES_URL + "/" + idWarehouse;

        ResponseEntity<WarehouseDto> warehouseResponse = restTemplate.getForEntity(url, WarehouseDto.class);

        handler.handle(warehouseResponse);

    }

    public void loadWarehousesToCB(ProcessFinishedHandler handler){
        Thread thread = new Thread(() -> processLoadWarehousesToCB(handler));
        thread.start();
    }

    private void processLoadWarehousesToCB(ProcessFinishedHandler handler) {
        ResponseEntity<WarehouseCBDto[]> warehouseCBResponse = restTemplate.getForEntity(WAREHOUSES_CB_URL,
                WarehouseCBDto[].class);

        handler.handle(warehouseCBResponse);

    }

    public void saveWarehouse(WarehouseDto warehouseDto, ProcessFinishedHandler handler){
        Thread thread = new Thread(() -> processSaveWarehouse(warehouseDto, handler));
        thread.start();
    }

    private void processSaveWarehouse(WarehouseDto warehouseDto, ProcessFinishedHandler handler) {
        ResponseEntity responseWarehouse = restTemplate.postForEntity(WAREHOUSES_URL, warehouseDto,
                ResponseEntity.class);

        handler.handle(responseWarehouse);
    }

    public void deleteWarehouse(Integer idWarehouse, ProcessFinishedHandler handler){

        Thread thread = new Thread(() -> processDeleteWarehouse(idWarehouse, handler));
        thread.start();

    }

    private void processDeleteWarehouse(Integer idWarehouse, ProcessFinishedHandler handler){
        String url = WAREHOUSES_URL + "/" + idWarehouse;
        restTemplate.delete(url);
        handler.handle(ResponseEntity.ok().build());
    }

}
