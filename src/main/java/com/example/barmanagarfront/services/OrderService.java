package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.Order;
import com.example.barmanagarfront.models.OrderResponseObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
        logger.info("Open orders:" + forEntity);
        ArrayList<OrderResponseObject.OrderDto> orderDtoList =
                Objects.requireNonNull(forEntity.getBody()).get_embedded().getOrderDtoList();

        return orderDtoList;
    }

    public Order getOrder(String orderId)
    {
        String url = String.format("http://localhost:8080/orders/%s",orderId);
        ResponseEntity<Order> forEntity = restTemplate.getForEntity(url, Order.class);
        System.out.println(forEntity.getBody());
        return forEntity.getBody();
    }

    public void setOrderClose(String id)
    {
        String url = String.format("http://localhost:8080/orders/%s",id);
        restTemplate.put(url, OrderResponseObject.OrderDto.class);

    }

    public OrderResponseObject.OrderDto getOrderBySeatNumber(int seatNumber)
    {
        String url = String.format
                ("http://localhost:8080/orders/closeBySeat?seatNumber=%s&orderStatus=Open",seatNumber);
        ResponseEntity<OrderResponseObject.OrderDto> response =
                restTemplate.getForEntity(url, OrderResponseObject.OrderDto.class);
        logger.info(response.getBody().toString());
        return response.getBody();
    }

}
