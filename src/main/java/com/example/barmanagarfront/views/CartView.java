package com.example.barmanagarfront.views;


import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.Singeltones.CartOfDrinksManager;
import com.example.barmanagarfront.factories.ImageFactory;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IRemoveDrinkFromCartObserver;
import com.example.barmanagarfront.services.InventoryService;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
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

@Route(value = "Cart",layout = BasicLayout.class)
@PageTitle("Bar | Cart of drinks")
public class CartView extends Main implements  HasComponents, HasStyle, IRemoveDrinkFromCartObserver
{
    private final InventoryService inventoryService;
    private final String titleMsg = "Selected drinks";

    private List<BarDrink> addedDrinks;
    private OrderedList imageContainer;
    private H1 title;
    private Button saveButton;

    public CartView(InventoryService service)
    {
        this.inventoryService = service;
        constructUI();
        displayDrinks();

    }

    private void displayDrinks()
    {
        imageContainer.removeAll();
        // get drinks that was added to inventory
        this.addedDrinks = CartOfDrinksManager.getInstance().getDrinksInventory();

        buildDrinksView();
    }

    private void buildDrinksView()
    {
        addedDrinks.forEach(barDrink ->
        {
            CartDrinkBarCard drinkBarCard = new CartDrinkBarCard(barDrink);
            drinkBarCard.addObserver(this);
            imageContainer.add(drinkBarCard);
        });
    }

    private void constructUI() {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");


        saveButton = new Button("Save");
        saveButton.addClickListener(buttonClickEvent -> {
            try
            {
                HttpStatus status = inventoryService.saveDrinksToInventory(addedDrinks);
                if ( status == HttpStatus.CREATED)
                {
                    Notification notification = getNotificationItem("Cart saved");
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
        Image image = ImageFactory.getInstance().getImage("selectedDrinksLogo.png");
//        image.setWidth("300px");
        title = new H1(image);

//        title = new H1(titleMsg);
        headerTitle.add(title);

        container.add(headerTitle, saveButton);
        add(container, imageContainer);

    }

    private Notification getNotificationItem(String notificationMsg)
    {
        Notification notification = Notification.show(notificationMsg);
        notification.setDuration(2000);
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setPosition(Notification.Position.TOP_STRETCH);
        return notification;
    }

    @Override
    public void onRemove()
    {
        displayDrinks();
    }
}
