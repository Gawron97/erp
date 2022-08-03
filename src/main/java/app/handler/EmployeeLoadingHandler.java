package app.handler;

import app.dto.EmployeeDto;

@FunctionalInterface
public interface EmployeeLoadingHandler {

    void handle(EmployeeDto employeeDto);

}
