package app.dto;

import lombok.Data;

@Data
public class EmployeeDto {

    private Integer idEmployee;
    private String name;
    private String surname;
    private String salary;

    public static EmployeeDto of(String name, String surname, String salary){
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.name = name;
        employeeDto.surname = surname;
        employeeDto.salary = salary;

        return employeeDto;
    }

}
