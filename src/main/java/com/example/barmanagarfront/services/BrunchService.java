package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.BrunchMapper;
import com.example.barmanagarfront.models.BrunchMapper.BrunchDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

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
        String url = "http://localhost:8080/brunches/info";
        ResponseEntity<BrunchMapper> response = restTemplate.getForEntity(url, BrunchMapper.class);
        ArrayList<BrunchDto> brunchDtoList = response.getBody().get_embedded().getBrunchDtoList();

        return brunchDtoList;

    }


}
