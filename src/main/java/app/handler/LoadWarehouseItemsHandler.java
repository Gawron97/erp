package app.handler;

import app.dto.WarehouseDto;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface LoadWarehouseItemsHandler {

    void handle(ResponseEntity responseEntity);

}
