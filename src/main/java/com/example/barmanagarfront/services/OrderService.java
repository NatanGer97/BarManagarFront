package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.QueryResult;
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
import java.util.Arrays;
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
        logger.info("Open orders:" + Objects.requireNonNull(forEntity.getBody()).get_embedded().getOrderDtoList().size());
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

    public List<QueryResult> getProfitsByYear(int year){
        String url = String.format("http://localhost:8080/orders/profits?year=%s", year);
        ResponseEntity<QueryResult[]> response = restTemplate.getForEntity(url, QueryResult[].class);

        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public List<QueryResult> getMostPopularDrinks(){
        String url = "http://localhost:8080/orders/drinkPopularity";
        ResponseEntity<QueryResult[]> response = restTemplate.getForEntity(url, QueryResult[].class);

        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public OrderResponseObject.OrderDto getOrderBySeatNumber(int seatNumber)
    {
        String url = String.format
                ("http://localhost:8080/orders/closeBySeat?seatNumber=%s&orderStatus=Open",seatNumber);
        ResponseEntity<OrderResponseObject.OrderDto> response =
                restTemplate.getForEntity(url, OrderResponseObject.OrderDto.class);
        if ( !response.hasBody() )
        {
            return new OrderResponseObject.OrderDto();
        }
        return response.getBody();
    }
    public ArrayList<OrderResponseObject.OrderDto> getDtoOrders()
    {
        String url = "http://localhost:8080/orders/info";
        ResponseEntity<OrderResponseObject> response = restTemplate.
                getForEntity(url, OrderResponseObject.class);
        ArrayList<OrderResponseObject.OrderDto> orderDtoList;
        try
        {
           orderDtoList = Objects.requireNonNull(response.getBody()).get_embedded().getOrderDtoList();

        }
        catch (NullPointerException exception){
            orderDtoList = new ArrayList<>();
        }

        return orderDtoList;

    }
}
