package app.rest;

import app.dto.WarehouseDto;
import app.handler.LoadWarehousesHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class WarehousesRestClient {

    private static final String WAREHOUSES_URL = "http://localhost:8080/warehouses";

    private final RestTemplate restTemplate;

    public WarehousesRestClient(){
        restTemplate = new RestTemplate();
    }

    public void loadWarehouses(LoadWarehousesHandler handler){
        Thread thread = new Thread(() -> processloadWarehouses(handler));
        thread.start();
    }

    public void processloadWarehouses(LoadWarehousesHandler handler){

        ResponseEntity<WarehouseDto[]> responseWarehouses = restTemplate.getForEntity(WAREHOUSES_URL, WarehouseDto[].class);

        System.out.println(Arrays.stream(responseWarehouses.getBody()).toList().get(0));

        handler.handle(Arrays.asList(responseWarehouses.getBody()));

    }

}
