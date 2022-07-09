package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.services.CustomerService;
import com.example.barmanagarfront.services.InventoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "BuildOrder", layout = MainLayout.class)
@PageTitle("Bar | Order Screen")
public class OrderBuilderForm extends Div
{
    private final InventoryService inventoryService;
    private final CustomerService customerService;

    private List<BarDrink> barDrinks;
    private VirtualList<BarDrink> list;
    private Select<String>  stringSelect;
    private Grid<BarDrink> drinksGrid;


    public OrderBuilderForm(InventoryService inventoryService, CustomerService service)
    {
        this.inventoryService = inventoryService;
        this.customerService = service;
        barDrinks = this.inventoryService.getInventoryItems();
        customerService.getCustomers();
        setSizeFull();
        initDrinksGrid();

        VerticalLayout titleLayout = initTitle("Order Screen");
        add(titleLayout,initCustomersContainer(),getContent());

    }

    private void initDrinksGrid()
    {
        drinksGrid = new Grid<>(BarDrink.class,false);
        drinksGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        drinksGrid.setSizeFull();
        drinksGrid.setAllRowsVisible(true);
        drinksGrid.addComponentColumn(drinksGrid -> createAvatarColumn(drinksGrid));
        drinksGrid.addColumn(barDrink -> barDrink.getPrice()).setHeader("Price") ;
        drinksGrid.addColumn(barDrink -> barDrink.getCategory()).setHeader("Category") ;
        drinksGrid.addComponentColumn(barDrink -> createIsAlcoholicColumn(barDrink)).setHeader("Is Alcoholic") ;
        drinksGrid.addComponentColumn(barDrink -> createButtons(barDrink)).setHeader("Actions");
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
        Button openButton = new Button("Open");
        openButton.addClickListener(buttonClickEvent -> {

        });

        Button removeButton = new Button("Remove");


        HorizontalLayout horizontalLayout = new HorizontalLayout(openButton, removeButton);
        return horizontalLayout;
    }
    private HorizontalLayout createAvatarColumn(BarDrink barDrink)
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

    private VirtualList<BarDrink> initList()
    {
        list = new VirtualList<>();
//        list.setSizeFull();
//        list.setHeight("500px");
//        list.setWidth("500px");
        list.setItems(barDrinks);
        list.setRenderer(drinkCardRenderer);
        return list;

    }



    private Component getContent()
    {

        HorizontalLayout content = new HorizontalLayout(drinksGrid);
//        HorizontalLayout content = new HorizontalLayout(initList());
//        content.setFlexGrow(2, list);
        content.setFlexGrow(1, content);
        content.setSizeFull();
        return content;
    }

    private ComponentRenderer<Component, BarDrink> drinkCardRenderer = new ComponentRenderer<>(drink -> {
        HorizontalLayout cardLayout = new HorizontalLayout();
        cardLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        cardLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        cardLayout.setMargin(true);

        Avatar avatar = new Avatar(drink.getName(), drink.getImage());
        avatar.setHeight("100px");
        avatar.setWidth("100px");

        VerticalLayout infoLayout = new VerticalLayout();
        infoLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        infoLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        infoLayout.setSpacing(false);
        infoLayout.setPadding(true);

        infoLayout.getElement().appendChild(ElementFactory.createStrong(drink.getName()));
        infoLayout.add(new Div(new Text(String.valueOf(drink.getPrice()))));

        VerticalLayout moreInfoLayout = new VerticalLayout();
        moreInfoLayout.setSpacing(false);
        moreInfoLayout.setPadding(false);
        moreInfoLayout.add(new Div(new Text(drink.getIsAlcoholic())));
        moreInfoLayout.add(new Div(new Text(drink.getCategory())));
        infoLayout.add(new Details("More information", moreInfoLayout));

        cardLayout.add(avatar, infoLayout);
        return cardLayout;
    });

    private VerticalLayout initTitle(String title)
    {
        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();
        headerTitle.setAlignItems(FlexComponent.Alignment.CENTER);
        headerTitle.addClassNames("mb-0", "mt-xs", "text-xl");
        headerContainer.add(headerTitle);

        headerTitle.add(new H3(new Text(title)));
        return headerContainer;
    }

    private HorizontalLayout initCustomersContainer()
    {
        stringSelect = new Select<>();
        HorizontalLayout horizontalLayout = new HorizontalLayout();
        horizontalLayout.add(stringSelect);
//        horizontalLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
//        horizontalLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        horizontalLayout.setFlexGrow(5,stringSelect);
        horizontalLayout.getThemeList().add("spacing-l");
        horizontalLayout.setPadding(true);
        return horizontalLayout;
    }


}
