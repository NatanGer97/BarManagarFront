package com.example.barmanagarfront.services;

import com.example.barmanagarfront.ApiDrink;
import com.example.barmanagarfront.ResponseOfDrinksJson;
import com.example.barmanagarfront.models.BarDrink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ServerErrorException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class ApiDrinkService
{
    private final Logger logger;
    private RestTemplate restTemplate;

    public ApiDrinkService(RestTemplateBuilder restTemplateBuilder)
    {
        this.restTemplate = restTemplateBuilder.build();
        logger = LoggerFactory.getLogger(ApiDrinkService.class);
    }

    public void getInventoryItems()
    {
        String url = "http://localhost:8080/inventory";

    }

  public List<String> getDrinksCategories() throws NullPointerException
  {
        String url = "http://localhost:8080/supplier/getCategories";
      ResponseEntity<String[]> forEntity = restTemplate.getForEntity(url, String[].class);
      return Arrays.asList(Objects.requireNonNull(forEntity.getBody()));
  }

  public ArrayList<ApiDrink> getDrinksByCategory(String categoryName)
  {
      System.out.println(categoryName);
      String url = String.format("http://localhost:8080/supplier/category?category=%s",categoryName);
      System.out.println(url);
      ResponseEntity<ResponseOfDrinksJson> forEntity = restTemplate.getForEntity(
              url, ResponseOfDrinksJson.class);

      return forEntity.getBody().getDrinkList().getApiDrinkList();
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



}
