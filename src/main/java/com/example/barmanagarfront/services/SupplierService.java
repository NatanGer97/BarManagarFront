package com.example.barmanagarfront.services;

import com.example.barmanagarfront.models.*;
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
public class SupplierService {
    private final Logger logger;
    private RestTemplate restTemplate;

    public SupplierService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
        logger = LoggerFactory.getLogger(SupplierService.class);
    }


    public ArrayList<ApiDrink> getDrinksByCategory(String categoryName) {
        System.out.println(categoryName);
        String url = String.format("http://localhost:8080/supplier/category?category=%s", categoryName);
        System.out.println(url);
        ResponseEntity<ResponseOfDrinksJson> forEntity = restTemplate.getForEntity(
                url, ResponseOfDrinksJson.class);

        return forEntity.getBody().getDrinkList().getApiDrinkList();
    }

    public ArrayList<ApiDrink> getDrinksByIngredient(String ingredientName) {
        String url = String.format("http://localhost:8080/supplier/ingredient?ingredient=%s", ingredientName);
        ResponseEntity<ResponseOfDrinksJson> entity = restTemplate.getForEntity(url, ResponseOfDrinksJson.class);

        return entity.getBody().getDrinkList().getApiDrinkList();
    }

    public ArrayList<ApiDrink> getDrinksByAlcoholicFilter(String alcoholicFilter) {
        String url = String.format("http://localhost:8080/supplier/alcoholic?alcoholicFilter=%s", alcoholicFilter);
        ResponseEntity<ResponseOfDrinksJson> entity = restTemplate.getForEntity(url, ResponseOfDrinksJson.class);

        return entity.getBody().getDrinkList().getApiDrinkList();
    }

    public List<String> getDrinksCategories() throws NullPointerException {
        String url = "http://localhost:8080/supplier/getCategories";
        ResponseEntity<String[]> forEntity = restTemplate.getForEntity(url, String[].class);
        return Arrays.asList(Objects.requireNonNull(forEntity.getBody()));
    }

    public List<String> getIngredients() throws NullPointerException {
        String url = "http://localhost:8080/supplier/getIngredients";
        ResponseEntity<String[]> response = restTemplate.getForEntity(url, String[].class);
        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }

    public void deleteDrinkFromInventory(String id) {
        String url = "http://localhost:8080/inventory/%s";
        restTemplate.delete(String.format(url, id));
    }
}
