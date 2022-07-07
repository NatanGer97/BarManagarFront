package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.models.ResponseOfInventoryDrink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
public class InventoryService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public InventoryService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        logger = LoggerFactory.getLogger(SupplierService.class);
    }

    public ArrayList<BarDrink> getInventoryItems()
    {
        String url = "http://localhost:8080/inventory";
        ResponseEntity<ResponseOfInventoryDrink> response = restTemplate.getForEntity(
                url,ResponseOfInventoryDrink.class);
        System.out.println(response.getBody().getBarDrinkList());
        return response.getBody().getBarDrinkList().getBarDrinkList();

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


}
