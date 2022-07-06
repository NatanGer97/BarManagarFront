package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class ResponseOfInventoryDrink
{
    @JsonProperty("_embedded")
    public BarDrinkList barDrinkList;

    @Data
    @JsonRootName("Embedded")
    public static class BarDrinkList
    {
        public ArrayList<BarDrink> barDrinkList;
    }

/*    public class BarDrinkList{
        public String id;
        public String name;
        public String category;
        public String isAlcoholic;
        public ArrayList<String> ingredients;
        public String image;
        public double price;
        public String recommendedGlass;

    }*/

}
