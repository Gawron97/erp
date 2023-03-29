package app.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@FunctionalInterface
public interface ProcessFinishedHandler {

    void handle(ResponseEntity responseEntity);

}
