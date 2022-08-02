package app.rest;

import app.dto.EmployeeDto;
import app.handler.LoadEmployeeHandler;
import app.handler.SaveEmployeeHandler;
import app.table.EmployeeTableModel;
import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class EmployeeRestClient {

    private static final String EMPLOYEES_URL = "http://localhost:8080/employees";

    private final RestTemplate restTemplate;

    public EmployeeRestClient(){
        restTemplate = new RestTemplate();
    }


    public void load(LoadEmployeeHandler handler){
        Thread thread = new Thread(() -> processLoading(handler));
        thread.setDaemon(true);
        thread.start();
    }

    private void processLoading(LoadEmployeeHandler handler){

        ResponseEntity<EmployeeDto[]> employees = restTemplate.getForEntity(EMPLOYEES_URL, EmployeeDto[].class);

        handler.handle(Arrays.stream(employees.getBody()).toList());
    }

    public void saveEmployee(EmployeeDto employeeDto, SaveEmployeeHandler saveEmployeeHandler){
        ResponseEntity<EmployeeDto> responseEmployee = restTemplate.postForEntity(EMPLOYEES_URL, employeeDto, EmployeeDto.class);

        if(HttpStatus.OK.equals(responseEmployee.getStatusCode())){
            saveEmployeeHandler.handle();
        }else{
            System.out.println("cos poszlo nie tak");
        }

    }


}
