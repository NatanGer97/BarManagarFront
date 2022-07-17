package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.models.QueryResult;
import com.example.barmanagarfront.models.ResponseOfInventoryDrink;
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
public class InventoryService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public InventoryService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        logger = LoggerFactory.getLogger(InventoryService.class);
    }

    public ArrayList<BarDrink> getInventoryItems()
    {
        String url = "http://localhost:8080/inventory";
        ResponseEntity<ResponseOfInventoryDrink> response = restTemplate.getForEntity(
                url,ResponseOfInventoryDrink.class);

        return response.getBody().getBarDrinkList().getBarDrinkList();
    }

    public BarDrink getDrinkById(String id){
        String url = String.format("http://localhost:8080//inventory/%s", id);
        ResponseEntity<BarDrink> response = restTemplate.getForEntity(url, BarDrink.class);

        return response.getBody();
    }

    public HttpStatus saveDrinkToInventory(BarDrink barDrink)
    {
        String url = "http://localhost:8080/inventory/";

        ResponseEntity<BarDrink> savedDrink = restTemplate.postForEntity(url, barDrink, BarDrink.class);
        logger.info("Saved drink: " + savedDrink);
        return savedDrink.getStatusCode();
    }

    public HttpStatus saveDrinksToInventory(List<BarDrink> barDrinks )
    {
        HttpStatus status = null;
        for ( BarDrink barDrink : barDrinks )
        {
            status = saveDrinkToInventory(barDrink);

        }
        return status;
    }

    public void deleteDrinkFromInventory(String id)
    {
        String url = "http://localhost:8080/inventory/%s";
        restTemplate.delete(String.format(url,id));
    }

    public List<QueryResult> getInventoryCountByCategory(){
        String url = "http://localhost:8080/inventory/groupCountByCategory/";
        ResponseEntity<QueryResult[]> response = restTemplate.getForEntity(
                url, QueryResult[].class);
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }

    public List<QueryResult> getInventoryCountByIngredient(){
        String url = "http://localhost:8080/inventory/getIngredientsCount/";
        ResponseEntity<QueryResult[]> response = restTemplate.getForEntity(
                url, QueryResult[].class);
        return Arrays.stream(Objects.requireNonNull(response.getBody())).toList();
    }
}
