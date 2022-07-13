package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.BrunchMapper;
import com.example.barmanagarfront.models.EmployeeMapper;
import com.example.barmanagarfront.models.EmployeeMapper.EmployeeDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

public class EmployeeService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public EmployeeService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        this.logger = LoggerFactory.getLogger(BrunchService.class);
    }

//    public ArrayList<EmployeeDto> getBrunchesDtos()
//    {
//        String url = "http://localhost:8080/brunches/info";
//        ResponseEntity<BrunchMapper> response = restTemplate.getForEntity(url, EmployeeMapper.class);
//        ArrayList<EmployeeDto> brunchDtoList = response.getBody().get_();
//
//        return brunchDtoList;
//
//    }
}
