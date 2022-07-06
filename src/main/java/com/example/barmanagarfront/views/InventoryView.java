package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.services.ApiDrinkService;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "Inventory",layout = MainLayout.class)
@PageTitle("Bar | Cart of drinks")
public class InventoryView extends Main implements HasComponents, HasStyle
{
    private final ApiDrinkService apiDrinkService;
    private List<BarDrink> menuDrinks;
    private OrderedList imageContainer;
    private H1 title;


    public InventoryView(ApiDrinkService apiDrinkService)
    {
        this.apiDrinkService = apiDrinkService;
        menuDrinks = apiDrinkService.getInventoryItems();

        constructUI();
        displayMenuDrinks();



    }

    private void displayMenuDrinks()
    {
        menuDrinks.forEach(barDrink -> imageContainer.add(new DrinkBarCard(barDrink)));
    }

    private void constructUI()
    {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");


        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();

        headerTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(headerTitle);

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");


        title = new H1("Menu");
        headerTitle.add(title);
        container.add(headerTitle);
        add(container, imageContainer);

    }
}
