package com.example.barmanagarfront;

import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IInventoryObserver;
import com.example.barmanagarfront.services.ApiDrinkService;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

public final class BarInventoryManager
{
    private static volatile BarInventoryManager inventoryManagerInstance;
    private  ApiDrinkService apiDrinkService;

    private ArrayList<BarDrink> drinksInventory;


    public ArrayList<BarDrink> getDrinksInventory()
    {
        return drinksInventory;
    }

    private List<IInventoryObserver> observers;

    private BarInventoryManager()
    {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        apiDrinkService = new ApiDrinkService(restTemplateBuilder);
        drinksInventory = new ArrayList<>();
        observers = new ArrayList<>();
        System.out.println(apiDrinkService.getDrinksCategories().get(0) + "!!!!!!!!!!");
    }
    public void addObserver(IInventoryObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(IInventoryObserver observer)
    {
        observers.remove(observer);
    }

    private void notifySizeChanged()
    {
        System.out.println(observers);
        observers.forEach(observer -> observer.OnSizeChanged(drinksInventory.size()));
    }

    public static BarInventoryManager getInstance()
    {
        BarInventoryManager inventoryManager = inventoryManagerInstance;
        if ( inventoryManagerInstance != null )
            return inventoryManager;
        synchronized (BarInventoryManager.class)
        {
            if ( inventoryManagerInstance == null )
            {
                inventoryManagerInstance = new BarInventoryManager();
            }
            return inventoryManagerInstance;
        }
    }

    public void addToInventory(BarDrink barDrink)
    {
        drinksInventory.add(barDrink);

        notifySizeChanged();

    }

    public void removeFromInventory(BarDrink barDrink)
    {
        drinksInventory.remove(barDrink);
        notifySizeChanged();
    }



}
