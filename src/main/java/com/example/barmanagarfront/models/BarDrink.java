package com.example.barmanagarfront.models;

import com.example.barmanagarfront.ResponseOfDrinksJson;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@Data
public class BarDrink{
    public String id;
    public String name;
    public String category;
    public String isAlcoholic;
    public List<String> ingredients;
//    public String[] ingredients;
    public String image;
    public Double price;
    public String recommendedGlass;

    public BarDrink()
    {
        ingredients = new ArrayList<>();
    }

    public void addIngredient(String ingredient)
    {
        this.ingredients.add(ingredient);
    }
    public void removeIngredient(String ingredient)
    {
        this.ingredients.remove(ingredient);
    }

/*  public BarDrink(String id, String name, String category, String isAlcoholic,
                    String[] ingredients, String image, Double price, String recommendedGlass) {
        this.id = id;
        this.name = name;
        this.category = category;
        this.isAlcoholic = isAlcoholic;
        this.ingredients = ingredients;
        this.image = image;
        this.price = price;
        this.recommendedGlass = recommendedGlass;
    }*/
}
