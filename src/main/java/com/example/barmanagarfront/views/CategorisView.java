package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.enums.eFilterType;
import com.example.barmanagarfront.services.SupplierService;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;

import java.util.ArrayList;
import java.util.List;

@PageTitle("Categories Form")
@Route(value = "List", layout = MainLayout.class)
@RouteAlias(value = "list", layout = MainLayout.class)
public class CategorisView extends Main implements HasComponents, HasStyle {

    private final SupplierService apiDrinkService;
    private List<String> categories;

    private OrderedList imageContainer;

    public CategorisView(SupplierService apiDrinkService) {
        this.apiDrinkService = apiDrinkService;

        constructUI();

        initCategories(apiDrinkService);
        categories.forEach(category -> imageContainer.add(new FormViewCard(category, eFilterType.Category)));
    }

    private void initCategories(SupplierService apiDrinkService)
    {
        try
        {
             categories = apiDrinkService.getDrinksCategories();
        }
        catch (NullPointerException exception)
        {
            exception.printStackTrace();
            categories = new ArrayList<>();
        }
    }

    private void constructUI() {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        HorizontalLayout contentContainer = new HorizontalLayout();
        contentContainer.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitleContainer = new VerticalLayout();
        headerContainer.setAlignItems(FlexComponent.Alignment.CENTER);

        headerTitleContainer.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(headerTitleContainer);

        H1 title = new H1(new Text("Categories"));
        headerTitleContainer.add(title);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");

        //contentContainer.add(headerTitleContainer,sortBy);
        add(contentContainer, imageContainer);

    }
}