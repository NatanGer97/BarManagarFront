package com.example.barmanagarfront;


import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.services.ApiDrinkService;
import com.example.barmanagarfront.views.DrinkBarCard;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "Inventory",layout = MainLayout.class)
@PageTitle("Bar | inventory of drinks")
public class InventoryView extends Main implements  HasComponents, HasStyle
{
    private final ApiDrinkService apiDrinkService;

    private List<BarDrink> barDrinks;
    private OrderedList imageContainer;
    private H1 title;
    private Button saveButton;



    public InventoryView(ApiDrinkService apiDrinkService)
    {
        this.apiDrinkService = apiDrinkService;
        this.barDrinks = BarInventoryManager.getInstance().getDrinksInventory();
        constructUI();
        displayDrinks();

    }

    private void displayDrinks()
    {
        barDrinks.forEach(barDrink -> imageContainer.add(new DrinkBarCard(barDrink)));
    }

    private void constructUI() {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        saveButton = new Button("Save");
        saveButton.addClickListener(buttonClickEvent -> {

            barDrinks.forEach(barDrink -> apiDrinkService.saveDrinkToInventory(barDrink));
        });

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        HorizontalLayout footerLayout = new HorizontalLayout();
        footerLayout.add(saveButton);


        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();

        headerTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(headerTitle);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");

        title = new H1("Drinks in bar");
        headerTitle.add(title);



        container.add(headerTitle, sortBy);
        add(container, imageContainer,footerLayout);

    }

}
