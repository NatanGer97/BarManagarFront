package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.Branch;
import com.example.barmanagarfront.models.BranchMapper;
import com.example.barmanagarfront.models.BranchMapper.BranchDto;
import com.example.barmanagarfront.models.EmployeeMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Objects;

@Service
public class BranchService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public BranchService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.logger = LoggerFactory.getLogger(BranchService.class);
    }

    public ArrayList<BranchDto> getBranchesDtos()
    {
        ArrayList<BranchDto> brunchDtoList;

        String url = "http://localhost:8080/branches/info";
        ResponseEntity<BranchMapper> response = restTemplate.getForEntity(url, BranchMapper.class);
        try
        {
            brunchDtoList = Objects.requireNonNull(response.getBody()).get_embedded().getBranchDtoList();
        }
        catch (NullPointerException exception)
        {
            brunchDtoList = new ArrayList<>();
        }

        return brunchDtoList;
    }

    public BranchDto getBranch(String brunchId)
    {
        String url = String.format("http://localhost:8080/branches/%s/info",brunchId);
        logger.info(url);
        ResponseEntity<BranchDto> response = restTemplate.getForEntity(url, BranchDto.class);

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

        return responseEntityCustomer;
    }

    public void removeBranch(String branchIdToRemove)
    {
        String url = String.format("http://localhost:8080/branches/%s", branchIdToRemove);

        restTemplate.delete(url,Branch.class);
    }


}
