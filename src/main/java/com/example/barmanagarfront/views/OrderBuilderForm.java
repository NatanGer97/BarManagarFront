package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.services.CustomerService;
import com.example.barmanagarfront.services.InventoryService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.details.Details;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
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
    private List<BarDrink> barDrinks;
    private VirtualList<BarDrink> list;
    private final CustomerService customerService;

    public OrderBuilderForm(InventoryService inventoryService, CustomerService service)
    {
        this.inventoryService = inventoryService;
        this.customerService = service;
        barDrinks = this.inventoryService.getInventoryItems();
        customerService.getCustomers();

        VerticalLayout titleLayout = initTitle("Order Screen");
        add(titleLayout,getContent());

    }

    private VirtualList<BarDrink> initList()
    {
        list = new VirtualList<>();
//        list.setSizeFull();
//        list.setHeight("500px");
//        list.setWidth("500px");
        list.setItems(barDrinks);
        list.setRenderer(personCardRenderer);
        return list;

    }

    private Component getContent()
    {

        HorizontalLayout content = new HorizontalLayout(initList());
//        content.setFlexGrow(2, list);
        content.setFlexGrow(1, content);
        content.setSizeFull();
        return content;
    }

    private ComponentRenderer<Component, BarDrink> personCardRenderer = new ComponentRenderer<>(drink -> {
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


}
