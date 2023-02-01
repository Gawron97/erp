package app.handler;

import org.springframework.http.HttpStatus;

@FunctionalInterface
public interface ProcessFinishedHandler {

    void handle(HttpStatus httpStatus);

}
