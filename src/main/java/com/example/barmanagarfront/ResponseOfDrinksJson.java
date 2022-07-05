package com.example.barmanagarfront;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ResponseOfDrinksJson
{
    @JsonProperty("_embedded")
    public DrinkList drinkList;

    @Data
    @JsonRootName("Embedded")
    public static class DrinkList
    {
        public ArrayList<ApiDrink> apiDrinkList;


    }


}
