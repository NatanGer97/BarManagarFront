package com.example.barmanagarfront.Singeltones;

import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IInventoryObserver;

import java.util.ArrayList;
import java.util.List;

public final class CartOfDrinksManager
{
    private static volatile CartOfDrinksManager inventoryManagerInstance;
//    private  ApiDrinkService apiDrinkService;

    private ArrayList<BarDrink> drinksInventory;


    public ArrayList<BarDrink> getDrinksInventory()
    {
        return drinksInventory;
    }

    private List<IInventoryObserver> observers;

    private CartOfDrinksManager()
    {
        /*RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
        apiDrinkService = new ApiDrinkService(restTemplateBuilder);*/
        drinksInventory = new ArrayList<>();
        observers = new ArrayList<>();
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

    public static CartOfDrinksManager getInstance()
    {
        CartOfDrinksManager inventoryManager = inventoryManagerInstance;
        if ( inventoryManagerInstance != null )
            return inventoryManager;
        synchronized (CartOfDrinksManager.class)
        {
            if ( inventoryManagerInstance == null )
            {
                inventoryManagerInstance = new CartOfDrinksManager();
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
        System.out.println(drinksInventory.remove(barDrink));

        notifySizeChanged();
    }



}
