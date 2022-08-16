package app.rest;

import app.dto.EmployeeDto;
import app.handler.ProcessFinishedHandler;
import app.handler.EmployeeLoadingHandler;
import app.handler.LoadEmployeeHandler;
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
    }


    public void loadEmployees(LoadEmployeeHandler handler){
        Thread thread = new Thread(() -> processLoadingEmployees(handler));
        thread.setDaemon(true);
        thread.start();
    }

    private void processLoadingEmployees(LoadEmployeeHandler handler){

        ResponseEntity<EmployeeDto[]> employees = restTemplate.getForEntity(EMPLOYEES_URL, EmployeeDto[].class);

        handler.handle(Arrays.stream(employees.getBody()).toList());
    }

    public void saveEmployee(EmployeeDto employeeDto, ProcessFinishedHandler saveEmployeeHandler){

        Thread thread = new Thread(() -> processSaveEmployee(employeeDto, saveEmployeeHandler));
        thread.start();

    }

    private void processSaveEmployee(EmployeeDto employeeDto, ProcessFinishedHandler saveEmployeeHandler){
        ResponseEntity<EmployeeDto> responseEmployee = restTemplate.postForEntity(EMPLOYEES_URL, employeeDto, EmployeeDto.class);

        if(HttpStatus.OK.equals(responseEmployee.getStatusCode())){
            saveEmployeeHandler.handle();
        }else{
            System.out.println("cos poszlo nie tak :)");
        }
    }

    public void loadEmployeeData(Integer idEmployee, EmployeeLoadingHandler handler){

        Thread thread = new Thread(() -> processLoadEmployeeData(idEmployee, handler));
        thread.start();

    }

    private void processLoadEmployeeData(Integer idEmployee, EmployeeLoadingHandler handler) {
        String url = EMPLOYEES_ACTION_URL + "/" + idEmployee;
        ResponseEntity<EmployeeDto> responseEmployee = restTemplate.getForEntity(url, EmployeeDto.class);

        if(HttpStatus.OK.equals(responseEmployee.getStatusCode())){
            handler.handle(responseEmployee.getBody());
        }else{
            System.out.println("cos poszlo nie tak :)");
        }

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
        handler.handle();
    }

}
