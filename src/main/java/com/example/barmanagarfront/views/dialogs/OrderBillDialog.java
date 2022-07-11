package com.example.barmanagarfront.views.dialogs;

import com.example.barmanagarfront.Singeltones.SeatsManager;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.models.Order;
import com.example.barmanagarfront.services.OrderService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.shared.Registration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class OrderBillDialog extends Dialog
{
    private final Order order;
    private final OrderService orderService;
    private final Logger logger = LoggerFactory.getLogger(OrderBillDialog.class);
    private Map<BarDrink, Integer> orderedDrinksWithAmount;

    private Grid<BarDrink> orderGrid;
    private Button calcButton;
    private NumberField billNumberField;


    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }

    public OrderBillDialog(Order order)
    {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        this.order = order;
        logger.info(String.valueOf(order.getOrderedDrinks().size()));
        this.orderService = new OrderService(restTemplateBuilder);
        this.orderGrid = new Grid<>(BarDrink.class,false);
        initDrinksAmountMap();
        initGrid();
        initBillButton();
        initBillNumberField(order);

        add(createDialogLayout());
    }

    private void initDrinksAmountMap()
    {
        this.orderedDrinksWithAmount = new HashMap<>();
        ArrayList<BarDrink> orderedDrinks = order.getOrderedDrinks();
        logger.info("Ordered Drink size" + orderedDrinks.size());
        for ( BarDrink orderedDrink : orderedDrinks )
        {
            addToOrder(orderedDrink);
        }
    }

    /***
     *  function that add new item to order or increase his amount
     * @param barDrink
     */
    private void addToOrder(BarDrink barDrink)
    {
        Integer amountInOrderOfThiDrink = orderedDrinksWithAmount.putIfAbsent(barDrink, 1);
        if ( amountInOrderOfThiDrink != null ){
            orderedDrinksWithAmount.put(barDrink, amountInOrderOfThiDrink + 1);
        }
//        order.getOrderedDrinks().add(barDrink);

        updateGrid();
    }



    private void initGrid()
    {
        orderGrid.setAllRowsVisible(true);
        orderGrid.addColumn(drink -> drink.getName()).setHeader("Item");
        /*orderGrid.addColumn(drink -> drink.getPrice()).setHeader("price");
        orderGrid.addColumn(drink -> createAmountColumn(drink)).setHeader("amount");*/
        orderGrid.setSizeFull();

        updateGrid();
    }

    private int createAmountColumn(BarDrink drink)
    {
        return orderedDrinksWithAmount.get(drink);
    }


    private void updateGrid()
    {
        this.orderGrid.setItems(this.orderedDrinksWithAmount.keySet());
//        this.orderGrid.setItems(order.getOrderedDrinks());
    }

    private VerticalLayout createDialogLayout()
    {


        VerticalLayout dialogLayout = new VerticalLayout(billNumberField,orderGrid,calcButton);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }


    private void initBillButton()
    {
        calcButton = new Button("Pay");
//        calcButton.addClickListener(event -> fireEvent(new ClosCustomerDialogEvent(this,false)));
        calcButton.addClickListener(buttonClickEvent ->  closeOrder());

    }

    private void closeOrder()
    {
        System.out.println(order);
        orderService.setOrderClose(order);
        SeatsManager.getInstance().updateSet(order.getSeatNumber());
//        this.close();
        fireEvent(new ClosCustomerDialogEvent(this,false));
    }

    private void initBillNumberField(Order order)
    {
        billNumberField = new NumberField();
        billNumberField.setValue(order.getBill());

    }
}
