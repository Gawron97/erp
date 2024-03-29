package app.table;

import app.dto.EmployeeDto;
import javafx.beans.property.SimpleStringProperty;

public class EmployeeTableModel {

    private final Integer idEmployee;
    private final SimpleStringProperty name;
    private final SimpleStringProperty surname;
    private final SimpleStringProperty salary;

    public EmployeeTableModel(Integer idEmployee, String name, String surname, String salary){
        this.idEmployee = idEmployee;
        this.name = new SimpleStringProperty(name);
        this.surname = new SimpleStringProperty(surname);
        this.salary = new SimpleStringProperty(salary);
    }

    public static EmployeeTableModel of(EmployeeDto employeeDto){
        return new EmployeeTableModel(employeeDto.getIdEmployee(), employeeDto.getName(), employeeDto.getSurname(), employeeDto.getSalary());
    }

    public Integer getIdEmployee() {
        return idEmployee;
    }

    public String getName() {
        return name.get();
    }

    public SimpleStringProperty nameProperty() {
        return name;
    }

    public void setName(String name) {
        this.name.set(name);
    }

    public String getSurname() {
        return surname.get();
    }

    public SimpleStringProperty surnameProperty() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname.set(surname);
    }

    public String getSalary() {
        return salary.get();
    }

    public SimpleStringProperty salaryProperty() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary.set(salary);
    }
}
