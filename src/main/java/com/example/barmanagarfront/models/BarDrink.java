package com.example.barmanagarfront.models;

import com.example.barmanagarfront.ResponseOfDrinksJson;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
public class BarDrink{
    public String id;
    public String name;
    public String category;
    public String isAlcoholic;
    public String[] ingredients;
    public String image;
    public Double price;
    public String recommendedGlass;

//    public  BarDrink(ResponseOfDrinksJson.DrinkList.ApiDrink)

    public BarDrink(String id, String name, String category, String isAlcoholic,
                    String[] ingredients, String image, Double price, String recommendedGlass) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isAlcoholic = isAlcoholic;
        this.ingredients = ingredients;
        this.image = image;
        this.price = price;
        this.recommendedGlass = recommendedGlass;
    }
}
