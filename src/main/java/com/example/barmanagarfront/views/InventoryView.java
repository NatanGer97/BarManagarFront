package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IRemoveFromInventoryObserver;
import com.example.barmanagarfront.services.InventoryService;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "Inventory", layout = MainLayout.class)
@PageTitle("Bar | Cart of drinks")
public class InventoryView extends Main implements HasComponents, HasStyle, IRemoveFromInventoryObserver
{
    private final InventoryService inventoryService;
    private final String DELETE_MSG = "Successfully deleted";
    private List<BarDrink> menuDrinks;
    private OrderedList imageContainer;
    private H1 title;


    public InventoryView(InventoryService service)
    {
        this.inventoryService = service;
        constructUI();
        displayMenuDrinks();
    }

    private void displayMenuDrinks()
    {
        imageContainer.removeAll();
        menuDrinks = this.inventoryService.getInventoryItems();
        menuDrinks.forEach(barDrink -> buildNewDrinkCard(barDrink));
    }

    private void buildNewDrinkCard(BarDrink barDrink)
    {

        InventoryDrinkBarCard card = new InventoryDrinkBarCard(barDrink);
        card.addObserver(this);
        imageContainer.add(card);
    }

    private void constructUI()
    {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");


        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();
        headerTitle.setAlignItems(FlexComponent.Alignment.CENTER);

        headerTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(headerTitle);

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");

        title = new H1(new Text("Menu"));

        headerTitle.add(title);
        container.add(headerTitle);
        add(container, imageContainer);

    }

    /***
     *  function for sending delete req vie service
     *  to remove drink with given id
     * @param id id of target drink for deletion
     */
    @Override
    public void onRemoveFromInventory(String id)
    {
        // send req vie  service
        inventoryService.deleteDrinkFromInventory(id);


        // create and show notification
        Notification notification = new Notification();
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        Icon icon = VaadinIcon.TRASH.create();
        Div infoDiv = new Div(new Text(DELETE_MSG + id));
        HorizontalLayout layout = new HorizontalLayout(icon, infoDiv);

        layout.setAlignItems(FlexComponent.Alignment.CENTER);
        notification.add(layout);
        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.setDuration(3 * 1000);

        notification.addDetachListener(detachEvent -> displayMenuDrinks());

        notification.open();


    }
}
