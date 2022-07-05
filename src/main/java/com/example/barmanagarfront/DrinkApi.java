package com.example.barmanagarfront;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class DrinkApi
{
    public String idDrink;
    public String strDrink;
    public String strCategory;
    public String strAlcoholic;
    public String strGlass;
    public String strInstructions;
    public String strInstructionsDE;
    public String strInstructionsIT;
    public String strDrinkThumb;
    public String strIngredient1;
    public String strIngredient2;
    public String strIngredient3;
    public String strIngredient4;
    public String strMeasure1;
    public String strMeasure2;
    public String strMeasure3;
    public String strMeasure4;
    public String strCreativeCommonsConfirmed;
    public String dateModified;
    //        public Links _links;
    public String strIngredient5;
    public String strIngredient6;
    public String strIngredient7;
    public String strMeasure5;
    public String strMeasure6;
    public String strMeasure7;
    public String strInstructionsES;
    public String strTags;
    public String strImageSource;
    public String strIngredient8;
    public String strIngredient9;
    public String strMeasure8;
    public String strMeasure9;
    public String strVideo;
    public String strIBA;
    public String strImageAttribution;

    @Data
    public static class Root
    {
        public DrinkList _embedded;

    }


    @Data
    @JsonRootName("Embedded")
    public static class DrinkList{
        public ArrayList<DrinkApi> apiDrinkList;
    }



}
