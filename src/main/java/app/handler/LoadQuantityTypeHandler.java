package app.handler;

import app.dto.QuantityTypeDto;

import java.util.List;

@FunctionalInterface
public interface LoadQuantityTypeHandler {

    void handle(List<QuantityTypeDto> list);

}
