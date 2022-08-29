package app.handler;

import app.dto.TransportDto;

public interface TransportLoadingHandler {

    void handle(TransportDto transportDto);

}
