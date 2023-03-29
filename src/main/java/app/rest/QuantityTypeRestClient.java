package app.rest;

import app.dto.QuantityTypeDto;
import app.handler.LoadQuantityTypeHandler;
import app.handler.ProcessFinishedHandler;
import app.util.config.CustomResponseErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class QuantityTypeRestClient {

    private static final String QUANTITY_TYPES_URL = "http://localhost:8080/quantity_types";

    private final RestTemplate restTemplate;

    public QuantityTypeRestClient(){
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }

    public void loadQuantityTypes(LoadQuantityTypeHandler handler){
        Thread thread = new Thread(() -> processLoadQuantityTypes(handler));
        thread.start();
    }

    private void processLoadQuantityTypes(LoadQuantityTypeHandler handler) {
        ResponseEntity<QuantityTypeDto[]> quantityTypeResponse = restTemplate.getForEntity(QUANTITY_TYPES_URL,
                QuantityTypeDto[].class);

        if(HttpStatus.OK.equals(quantityTypeResponse.getStatusCode()))
            handler.handle(Arrays.stream(quantityTypeResponse.getBody()).toList());
        else
            throw new RuntimeException("cos poszlo nie tak");

    }


}
