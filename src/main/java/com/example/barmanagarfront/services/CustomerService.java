package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.Customer;
import com.example.barmanagarfront.models.CustomerAsDto;
import com.example.barmanagarfront.models.CustomerResponseObject;
import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;

@Service
public class CustomerService
{
    private final RestTemplate restTemplate;
    private final Logger logger = LoggerFactory.getLogger(CustomerService.class);


    public CustomerService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ArrayList<CustomerAsDto> getCustomers()
    {
        String url = "http://localhost:8080/customers/info";
        ResponseEntity<CustomerResponseObject> forEntity = restTemplate.getForEntity(url, CustomerResponseObject.class);
        return forEntity.getBody().get_embedded().getCustomerDtoList();

    }

    public ResponseEntity<Customer> saveCustomer(Customer customer)
    {
        String url = "http://localhost:8080/customers";
        ResponseEntity<Customer> responseEntityCustomer = restTemplate.postForEntity(url, customer,
                Customer.class);
        logger.info("Crested customer: " + responseEntityCustomer.getStatusCodeValue());
        HttpStatus statusCode = responseEntityCustomer.getStatusCode();

        return responseEntityCustomer;
    }


}
