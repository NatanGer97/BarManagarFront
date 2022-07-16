package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.Employee;
import com.example.barmanagarfront.models.EmployeeMapper;
import com.example.barmanagarfront.models.EmployeeMapper.EmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class EmployeeService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public EmployeeService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.logger = LoggerFactory.getLogger(BrunchService.class);
    }

    public ArrayList<EmployeeDto> getBranchEmployees(String idOfBranch) throws NullPointerException
    {
        String url = String.format("http://localhost:8080/employees/findByBranches/%s",idOfBranch);
        ResponseEntity<EmployeeMapper> response = restTemplate.getForEntity(url, EmployeeMapper.class);
        ArrayList<EmployeeDto> employeeDtos = Objects.requireNonNull(response.getBody()).get_embedded().employeeDtoList;


        return employeeDtos;
    }

    public ArrayList<EmployeeDto> getAllEmployeesByBranch(String branchId)
    {
        ArrayList<EmployeeDto> employeeDtos;
        String url = String.format("http://localhost:8080/employees/filterByBranch?brunchId=%s",branchId);
        ResponseEntity<EmployeeMapper> response = restTemplate.getForEntity(url, EmployeeMapper.class);
        try{

            employeeDtos = response.getBody().get_embedded().employeeDtoList;
        }catch (NullPointerException exception)
        {
            employeeDtos = new ArrayList<>();
        }
        logger.info(employeeDtos.toString());

        return employeeDtos;
    }

    public ResponseEntity<Employee> saveEmployee(Employee employee, String brunchId)
    {
        String url = String.format("http://localhost:8080/employees?branchId=%s",brunchId);
        ResponseEntity<Employee> response = restTemplate.postForEntity(url, employee,
                Employee.class);
        logger.info("Crested employee: " + response.getStatusCodeValue());
        HttpStatus statusCode = response.getStatusCode();

        return response;
    }

    public ArrayList<EmployeeDto> getAllEmployees()
    {
        ArrayList<EmployeeDto> employeeDtoList;
        String url = "http://localhost:8080/employees/info";
        ResponseEntity<EmployeeMapper> response = restTemplate.getForEntity(url, EmployeeMapper.class);

        try
        {
            employeeDtoList= Objects.requireNonNull(response.getBody()).get_embedded().employeeDtoList;
        }
        catch (NullPointerException exception)
        {
            employeeDtoList = new ArrayList<>();
        }
        return employeeDtoList;
    }

    public void updateEmployee(Employee employee,String employeeId){
        String url = String.format(
                "http://localhost:8080/employees/%s"
                ,employeeId);

        restTemplate.put(url,employee, Employee.class);
    }

    public void deleteEmployee(String idToRemove)
    {
        String url = String.format("http://localhost:8080/employees/%s",idToRemove);

        restTemplate.delete(url,Employee.class);

    }

}
