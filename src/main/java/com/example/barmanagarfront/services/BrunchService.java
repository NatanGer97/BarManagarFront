package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.Branch;
import com.example.barmanagarfront.models.BrunchMapper;
import com.example.barmanagarfront.models.BrunchMapper.BrunchDto;
import com.example.barmanagarfront.models.EmployeeMapper;
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
public class BrunchService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public BrunchService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.logger = LoggerFactory.getLogger(BrunchService.class);
    }

    public ArrayList<BrunchDto> getBrunchesDtos()
    {
        ArrayList<BrunchDto> brunchDtoList;

        String url = "http://localhost:8080/brunches/info";
        ResponseEntity<BrunchMapper> response = restTemplate.getForEntity(url, BrunchMapper.class);
        try
        {
            brunchDtoList = Objects.requireNonNull(response.getBody()).get_embedded().getBrunchDtoList();
        }
        catch (NullPointerException exception)
        {
            brunchDtoList = new ArrayList<>();
        }

        return brunchDtoList;
    }

    public BrunchDto getBranch(String brunchId)
    {
        String url = String.format("http://localhost:8080/brunches/%s/info",brunchId);
        logger.info(url);
        ResponseEntity<BrunchDto> response = restTemplate.getForEntity(url, BrunchDto.class);

        return response.getBody();

    }

    public void addEmployeeToBranch(String branchId,String employeeId){
        String url = String.format(
                "http://localhost:8080/branches/updatedEmployees/add?employeeToAddId=%s&branchId=%s"
                ,employeeId,branchId);

        restTemplate.put(url, EmployeeMapper.EmployeeDto.class);
    }

    public void removeEmployeeFromBranch(String branchId, String employeeId){
        String url = String.format(
                "http://localhost:8080/branches/updatedEmployees/remove?employeeRemoveId=%s&branchId=%s"
                ,employeeId,branchId);

        restTemplate.put(url, EmployeeMapper.EmployeeDto.class);
    }

    public ResponseEntity<Branch> createBranch(Branch branch)
    {
        logger.info(branch.toString());
        String url = "http://localhost:8080/branches";
        ResponseEntity<Branch> responseEntityCustomer = restTemplate.postForEntity(url, branch,
                Branch.class);
        logger.info("Crested brunch: " + responseEntityCustomer.getStatusCodeValue());
        HttpStatus statusCode = responseEntityCustomer.getStatusCode();

        return responseEntityCustomer;
    }

    public void removeBranch(String branchIdToRemove)
    {
        String url = String.format("http://localhost:8080/branches/%s", branchIdToRemove);

        restTemplate.delete(url,Branch.class);
    }


}
