package app.handler;

import app.dto.EmployeeDto;

import java.util.List;

public interface LoadEmployeeHandler {

    void handle(List<EmployeeDto> dtoList);

}
