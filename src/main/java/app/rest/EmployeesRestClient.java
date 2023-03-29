package app.rest;

import app.dto.EmployeeDto;
import app.handler.ProcessFinishedHandler;
import app.handler.EmployeeLoadingHandler;
import app.handler.LoadEmployeeHandler;
import app.util.config.CustomResponseErrorHandler;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

public class EmployeesRestClient {

    private static final String EMPLOYEES_URL = "http://localhost:8080/employees";
    private static final String EMPLOYEES_ACTION_URL = "http://localhost:8080/employees";

    private final RestTemplate restTemplate;

    public EmployeesRestClient(){
        restTemplate = new RestTemplate();
        restTemplate.setErrorHandler(new CustomResponseErrorHandler());
    }


    public void loadEmployees(ProcessFinishedHandler handler){
        Thread thread = new Thread(() -> processLoadingEmployees(handler));
        thread.setDaemon(true);
        thread.start();
    }

    private void processLoadingEmployees(ProcessFinishedHandler handler){

        ResponseEntity<EmployeeDto[]> employees = restTemplate.getForEntity(EMPLOYEES_URL, EmployeeDto[].class);

        handler.handle(employees);
    }

    public void saveEmployee(EmployeeDto employeeDto, ProcessFinishedHandler saveEmployeeHandler){

        Thread thread = new Thread(() -> processSaveEmployee(employeeDto, saveEmployeeHandler));
        thread.start();

    }

    private void processSaveEmployee(EmployeeDto employeeDto, ProcessFinishedHandler saveEmployeeHandler){
        ResponseEntity responseEmployee = restTemplate.postForEntity(EMPLOYEES_URL, employeeDto, ResponseEntity.class);

        saveEmployeeHandler.handle(responseEmployee);
    }

    public void loadEmployeeData(Integer idEmployee, ProcessFinishedHandler handler){

        Thread thread = new Thread(() -> processLoadEmployeeData(idEmployee, handler));
        thread.start();

    }

    private void processLoadEmployeeData(Integer idEmployee, ProcessFinishedHandler handler) {
        String url = EMPLOYEES_ACTION_URL + "/" + idEmployee;
        ResponseEntity<EmployeeDto> responseEmployee = restTemplate.getForEntity(url, EmployeeDto.class);

        handler.handle(responseEmployee);

    }

    public void deleteEmployee(Integer idEmployee, ProcessFinishedHandler handler){

        Thread thread = new Thread(() -> {
                processDeleteEmployee(idEmployee, handler);
        });
        thread.start();

    }

    private void processDeleteEmployee(Integer idEmployee, ProcessFinishedHandler handler){
        String url = EMPLOYEES_ACTION_URL + "/" + idEmployee;
        restTemplate.delete(url);
        handler.handle(ResponseEntity.ok().build());
    }

}
