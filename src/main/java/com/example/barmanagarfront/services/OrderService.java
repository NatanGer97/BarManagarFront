package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.Customer;
import com.example.barmanagarfront.models.Order;
import com.example.barmanagarfront.models.OrderResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class OrderService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public OrderService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        logger = LoggerFactory.getLogger(OrderService.class);
    }

    public ResponseEntity<Order> saveOrder(Order order)
    {
        String url = "http://localhost:8080/orders";
        ResponseEntity<Order> responseEntityCustomer = restTemplate.postForEntity(url, order,
                Order.class);
        logger.info("Crested Order: " + responseEntityCustomer.getStatusCodeValue());
        HttpStatus statusCode = responseEntityCustomer.getStatusCode();

        return responseEntityCustomer;
    }

    public List<OrderResponseObject.OrderDto> getOpenOrders()
    {
        String url = "http://localhost:8080/orders/openOrders";
        ResponseEntity<OrderResponseObject> forEntity =
                restTemplate.getForEntity(url, OrderResponseObject.class);
        return forEntity.getBody().get_embedded().getOrderDtoList();
    }

}
