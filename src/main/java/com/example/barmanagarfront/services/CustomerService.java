package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.CustomerResponseObject;
import org.springframework.boot.web.client.RestTemplateBuilder;
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
//        String url = "http://localhost:8080/customers/62c7009db4e5390a41a95099/info";
        String url = "http://localhost:8080/customers/info";
        CustomerResponseObject forObject =
                restTemplate.getForObject(url, CustomerResponseObject.class);
        System.out.println(forObject);
    }


}
