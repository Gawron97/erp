package app.rest;

import app.dto.StockItemDto;
import app.handler.LoadStockItemsHandler;
import app.handler.ProcessFinishedHandler;
import app.util.config.CustomResponseErrorHandler;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class StockItemRestClient {

    private final RestTemplate restTemplate;

    private final static String STOCK_ITEMS_URL = "http://localhost:8080/stock_item";

    public StockItemRestClient(){
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }


    public void loadStockItems(ProcessFinishedHandler handler) {

        Thread thread = new Thread(() -> processLoadStockItems(handler));
        thread.start();
    }

    private void processLoadStockItems(ProcessFinishedHandler handler) {

        ResponseEntity<StockItemDto[]> stockItemResponse = restTemplate.getForEntity(STOCK_ITEMS_URL, StockItemDto[].class);

        handler.handle(stockItemResponse);
    }
}
