package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.models.Customer;
import com.example.barmanagarfront.models.CustomerAsDto;
import com.example.barmanagarfront.models.Order;
import com.example.barmanagarfront.services.CustomerService;
import com.example.barmanagarfront.services.InventoryService;
import com.example.barmanagarfront.services.OrderService;
import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

@Route(value = "BuildOrder/:seat/:orderId?", layout = MainLayout.class)
@PageTitle("Bar | Order Screen")
public class OrderBuilderForm extends VerticalLayout implements BeforeEnterObserver
{
    private final InventoryService inventoryService;
    private final CustomerService customerService;
    private final OrderService orderService;

    private Map<BarDrink, Integer> orderedDrinksWithAmount;
    private ComboBox<CustomerAsDto> customerComboBox;
    private Grid<BarDrink> drinksGrid;
    private ArrayList<CustomerAsDto> customers;
    private Grid<BarDrink> orderedDrinkGrid;

    private NumberField totalNumberField;
    private Order order;
    private int seatNumber;
    private Button placeOrderButton;

    public OrderBuilderForm(InventoryService inventoryService, CustomerService service, OrderService orderService)
    {
        this.inventoryService = inventoryService;
        this.customerService = service;
        this.orderService = orderService;
        try
        {
            customers = this.customerService.getCustomers();
        }
        catch (NullPointerException exception)
        {
            customers = new ArrayList<>();
        }

        orderedDrinksWithAmount = new HashMap<>();
        initOrder();

        initTotalNumberField();

//        setSizeFull();
        initDrinksGrid();
        setupOrderView();
        VerticalLayout titleLayout = initTitle("Order Screen");

        add(titleLayout,setUpSelectContainer() ,getContent());

    }

    private void initOrder()
    {
        if ( order == null )
        {
            order = new Order();
            order.setSeatNumber(seatNumber);
        }
    }

    private void initTotalNumberField()
    {
        totalNumberField = new NumberField();
        totalNumberField.setValue(0.00);
        totalNumberField.setReadOnly(true);
        totalNumberField.setHelperText("Bill");

    }

    private void setupOrderView()
    {
        orderedDrinkGrid = new Grid<>(BarDrink.class,false);
        orderedDrinkGrid.setAllRowsVisible(true);
        orderedDrinkGrid.setWidth("50em");

        orderedDrinkGrid.addColumn(BarDrink::getName).setHeader("Item")
                .setAutoWidth(true);
        orderedDrinkGrid.addColumn(drink -> createAmountColumn(drink)).setHeader("amount");
        orderedDrinkGrid.addColumn(BarDrink::getPrice).setHeader("price");
        orderedDrinkGrid.addColumn(drink -> drink.getPrice() * createAmountColumn(drink)).setHeader("total");
        /*orderedDrinkGrid.addComponentColumn(drink -> createButtonsOrderGrid(drink));*/
    }

    private double calcTotalSum()
    {
         DecimalFormat decimalFormat = new DecimalFormat("0.00");

        double sum = 0;
        for ( Map.Entry<BarDrink, Integer> entry : orderedDrinksWithAmount.entrySet() )
        {
            BarDrink drink = entry.getKey();
            Integer amount = entry.getValue();
            sum += drink.getPrice() * amount;
        }
        return Double.parseDouble(decimalFormat.format(sum));
    }

    private int createAmountColumn(BarDrink drink)
    {
        return orderedDrinksWithAmount.get(drink);
    }

 /*   private Component createButtonsOrderGrid(BarDrink drink)
    {
        Button removeDrinkButton = new Button(VaadinIcon.TRASH.create());
        removeDrinkButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
        removeDrinkButton.addClickListener(event -> removeFromOrder());

        HorizontalLayout horizontalLayout = new HorizontalLayout(removeDrinkButton);
        return horizontalLayout;
    }
*/


    private void initDrinksGrid()
    {
        drinksGrid = new Grid<>(BarDrink.class,false);
        drinksGrid.setEnabled(false);
        drinksGrid.setHeight("500px");
        drinksGrid.setAllRowsVisible(true);
        drinksGrid.addComponentColumn(drinksGrid -> createDrinkAvatarColumn(drinksGrid));
        drinksGrid.addColumn(barDrink -> barDrink.getPrice()).setHeader("Price") ;
        drinksGrid.addColumn(barDrink -> barDrink.getCategory()).setHeader("Category") ;
        drinksGrid.addComponentColumn(barDrink -> createIsAlcoholicColumn(barDrink)).setHeader("Is Alcoholic") ;
        drinksGrid.addComponentColumn(barDrink -> createButtons(barDrink)).setHeader("Actions");

        drinksGrid.getColumns().forEach(barDrinkColumn -> barDrinkColumn.setTextAlign(ColumnTextAlign.CENTER));
        updateDrinksGrid();
    }

    private Span createIsAlcoholicColumn(BarDrink drink)
    {
        Span isAlcoholicSpan = new Span(drink.getIsAlcoholic());

        if ( drink.getIsAlcoholic().equals("Alcoholic") )
        {
            isAlcoholicSpan.getElement().getThemeList().add("badge contrast");
        }

        isAlcoholicSpan.getElement().getThemeList().add("badge");

        return isAlcoholicSpan;
    }
    private Component createButtons(BarDrink barDrink)
    {
        Button openButton = new Button(VaadinIcon.PLUS.create());
        openButton.addClickListener(buttonClickEvent -> addToOrder(barDrink));

        Button removeButton = new Button(VaadinIcon.MINUS.create());
        removeButton.addClickListener(buttonClickEvent -> removeFromOrder(barDrink));
        HorizontalLayout horizontalLayout = new HorizontalLayout(openButton, removeButton);
        return horizontalLayout;
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
        order.getOrderedDrinks().add(barDrink);
        updateOrderGrid();

    }

    /***
     * function that removing or decreasing amount of ordered item
     * @param barDrink
     */
    private void removeFromOrder(BarDrink barDrink)
    {
        if ( orderedDrinksWithAmount.containsKey(barDrink))
        {
            // if the item ordered more than once -> decrease quantity by 1
            if (  orderedDrinksWithAmount.get(barDrink) > 1 ){
                orderedDrinksWithAmount.put(barDrink, orderedDrinksWithAmount.get(barDrink) - 1);
            }
            else { // if the drink quantity is 1, so it needs to be deleted
                orderedDrinksWithAmount.remove(barDrink);
            }

            order.getOrderedDrinks().remove(barDrink);
            updateOrderGrid();
        }


    }

    private HorizontalLayout createDrinkAvatarColumn(BarDrink barDrink)
    {
        Avatar avatar = new Avatar(barDrink.getName(), barDrink.getImage());
        Span span = new Span(barDrink.getName());
        HorizontalLayout layout = new HorizontalLayout();
        layout.add(avatar,span);
        avatar.setHeight("50px");
        avatar.setWidth("50px");
        return layout;
    }
    private void updateDrinksGrid()
    {
        drinksGrid.setItems(inventoryService.getInventoryItems());
    }

    private void updateOrderGrid()
    {
        orderedDrinkGrid.setItems(orderedDrinksWithAmount.keySet());
        totalNumberField.setValue(calcTotalSum());
        order.setBill(totalNumberField.getValue());

    }


    private void updateCustomersComboBox()
    {
        this.customerComboBox.clear();
        this.customerComboBox.setItems(customerService.getCustomers());
    }

    private HorizontalLayout setUpSelectContainer() {

        this.customerComboBox = new ComboBox<>();
        customerComboBox.setItems(customers);
        customerComboBox.setItemLabelGenerator(CustomerAsDto::showCustomer);
        customerComboBox.setPlaceholder("Select Customer");

        placeOrderButton = new Button("place order");
        placeOrderButton.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
        placeOrderButton.addClickListener(buttonClickEvent -> placeOrder());
        customerComboBox.addValueChangeListener(event ->{
            drinksGrid.setEnabled(!(event.getValue() == null));

            if ( event.getValue() != null )
            {
                CustomerAsDto customerAsDto = event.getValue();
                String[] fullNameSplit = customerAsDto.getFullName().split(" ");
                Customer customer = new Customer(customerAsDto.getIdNumber(),
                        fullNameSplit[0],fullNameSplit[1]);
                order.setCustomer(customer);
            }
        });

        Button addNewCustomerButton = new Button("New customer");
        addNewCustomerButton.addThemeVariants();
        addNewCustomerButton.addClickListener(buttonClickEvent -> createCustomer());

        HorizontalLayout layout = new HorizontalLayout(customerComboBox,addNewCustomerButton,
               totalNumberField,placeOrderButton);
        layout.setFlexGrow(2, customerComboBox);

        return layout;
    }


    private void placeOrder()
    {


        if ( !order.getOrderedDrinks().isEmpty() )
        {
            ResponseEntity<Order> orderResponseEntity = orderService.saveOrder(order);

            if ( orderResponseEntity.getStatusCode() == HttpStatus.CREATED )
            {
                showNotification("Order sent",false);
            }

            this.getUI().ifPresent(ui -> ui.navigate(SeatView.class));
        }
        else
        {
            showNotification("Cant send empty order",true);
        }
    }

    private void showNotification(String msg, boolean isErrorOrNot)
    {
        Notification notification;
        notification = Notification.show(msg);
        notification.setDuration(2000);

        if ( !isErrorOrNot ) notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        else notification.addThemeVariants(NotificationVariant.LUMO_ERROR);

        notification.setPosition(Notification.Position.TOP_CENTER);
        notification.open();
    }

    private void createCustomer()
    {
        NewCustomerDialog newCustomerDialog = new NewCustomerDialog(customerService);
        newCustomerDialog.addListener(ClosCustomerDialogEvent.class, closeDialogEvent -> {
            updateCustomersComboBox();
            newCustomerDialog.close();
        });
        newCustomerDialog.open();
    }

    private Component getContent()
    {

        HorizontalLayout content = new HorizontalLayout(drinksGrid, orderedDrinkGrid);
        content.setFlexGrow(2, drinksGrid);
        content.setFlexGrow(1, content);
        content.setSizeFull();
        return content;
    }


    private VerticalLayout initTitle(String title)
    {
        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();
        headerTitle.setAlignItems(FlexComponent.Alignment.CENTER);
        headerTitle.addClassNames("mb-0", "mt-xs", "text-xl");
        headerContainer.add(headerTitle);

        headerTitle.add((new Text(title)));
        return headerContainer;
    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
        if  (beforeEnterEvent.getRouteParameters().get("seat").isPresent())
        {
            seatNumber = Integer.parseInt(beforeEnterEvent.getRouteParameters().get("seat").get());
            order.setSeatNumber(seatNumber);
        }

    }
}
