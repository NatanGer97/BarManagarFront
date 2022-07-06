package com.example.barmanagarfront;


import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IRemoveDrinkObserver;
import com.example.barmanagarfront.services.ApiDrinkService;
import com.example.barmanagarfront.views.DrinkBarCard;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.http.HttpStatus;

import java.util.List;

@Route(value = "Cart",layout = MainLayout.class)
@PageTitle("Bar | Cart of drinks")
public class CartView extends Main implements  HasComponents, HasStyle, IRemoveDrinkObserver
{
    private final ApiDrinkService apiDrinkService;
    private final String emptyMsg = "You didnt add new drink";
    private final String titleMsg = "Selected drinks";

    private List<BarDrink> addedDrinks;
    private OrderedList imageContainer;
    private H1 title;
    private Button saveButton;



    public CartView(ApiDrinkService apiDrinkService)
    {
        this.apiDrinkService = apiDrinkService;
        constructUI();
        displayDrinks();

    }

    private void displayDrinks()
    {
        imageContainer.removeAll();
        this.addedDrinks = CartOfDrinksManager.getInstance().getDrinksInventory();

        addedDrinks.forEach(barDrink ->
        {
            DrinkBarCard drinkBarCard = new DrinkBarCard(barDrink);
            drinkBarCard.addObserver(this);
            imageContainer.add(drinkBarCard);
        });
    }

    private void constructUI() {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");


        saveButton = new Button("Save");
        saveButton.addClickListener(buttonClickEvent -> {

//            addedDrinks.forEach(barDrink -> apiDrinkService.saveDrinkToInventory(barDrink));
            try
            {
                HttpStatus status = apiDrinkService.saveDrinksToInventory(addedDrinks);
                if ( status == HttpStatus.CREATED)
                {
                    System.out.println("ok");
                    Notification notification = Notification.show("Cart saved");
                    notification.setDuration(2000);
                    notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                    notification.setPosition(Notification.Position.TOP_STRETCH);
                    notification.open();
                    CartOfDrinksManager.getInstance().getDrinksInventory().clear();
                    displayDrinks();

                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
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

        title = new H1(titleMsg);
        headerTitle.add(title);

        container.add(headerTitle, saveButton);
        add(container, imageContainer,footerLayout);

    }

    @Override
    public void onRemove()
    {
        displayDrinks();
    }
}
