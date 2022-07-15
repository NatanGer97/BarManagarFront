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

    public ArrayList<EmployeeDto> getBranchEmployees(String idOfBranch)
    {
        String url = String.format("http://localhost:8080/employees/findByBranches/%s",idOfBranch);
        ResponseEntity<EmployeeMapper> response = restTemplate.getForEntity(url, EmployeeMapper.class);
        ArrayList<EmployeeDto> employeeDtos = response.getBody().get_embedded().employeeDtoList;

        return employeeDtos;
    }

    public ArrayList<EmployeeDto> getAllEmployees(String branchId)
    {
        String url = String.format("http://localhost:8080/employees/filterByBranch?brunchId=%s",branchId);
        ResponseEntity<EmployeeMapper> response = restTemplate.getForEntity(url, EmployeeMapper.class);
        ArrayList<EmployeeDto> employeeDtos = response.getBody().get_embedded().employeeDtoList;
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

}
