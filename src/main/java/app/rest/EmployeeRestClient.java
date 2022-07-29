package app.rest;

import app.dto.EmployeeDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;

public class EmployeeRestClient {

    private static final String GET_EMPLOYEES_URL = "http://localhost:8080/employees";

    private final RestTemplate restTemplate;

    public EmployeeRestClient(){
        restTemplate = new RestTemplate();
    }

    public List<EmployeeDto> getEmployees(){
        ResponseEntity<EmployeeDto[]> employees = restTemplate.getForEntity(GET_EMPLOYEES_URL, EmployeeDto[].class);
        return Arrays.stream(employees.getBody()).toList();
    }

}
