package com.example.barmanagarfront.views.dialogs;

import com.example.barmanagarfront.Singeltones.SeatsManager;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.models.Order;
import com.example.barmanagarfront.models.OrderResponseObject;
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
    private final OrderResponseObject.OrderDto order;
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

    public OrderBillDialog(OrderResponseObject.OrderDto order)
    {
        RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();

        this.order = order;
        this.orderService = new OrderService(restTemplateBuilder);
        this.orderGrid = new Grid<>(BarDrink.class,false);
        initDrinksAmountMap();
        initGrid();
        initBillButton();
        initBillNumberField();

        add(createDialogLayout());
    }

    private void initDrinksAmountMap()
    {
        this.orderedDrinksWithAmount = new HashMap<>();
        ArrayList<BarDrink> orderedDrinks = order.getOrderedItems();
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
        orderGrid.setSizeFull();

        updateGrid();
    }



    private void updateGrid()
    {
        this.orderGrid.setItems(this.orderedDrinksWithAmount.keySet());
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
        calcButton.addClickListener(buttonClickEvent ->  closeOrder());

    }

    private void closeOrder()
    {
        orderService.setOrderClose(order.getOrderId());
        SeatsManager.getInstance().updateSet(order.getSeatNumber());
        fireEvent(new ClosCustomerDialogEvent(this,false));
    }

    private void initBillNumberField()
    {
        billNumberField = new NumberField();
        billNumberField.setValue(order.getOrderBill());

    }
}
