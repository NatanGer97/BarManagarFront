package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.CustomerResponseObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomerService
{
    private final RestTemplate restTemplate;

    public CustomerService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
    }

    public void getCustomers()
    {
        String url = "http://localhost:8080/customers/info";
        ResponseEntity<CustomerResponseObject> forEntity = restTemplate.getForEntity(url, CustomerResponseObject.class);
        System.out.println(forEntity.getBody().get_embedded().getCustomerDtoList());

    }


}
