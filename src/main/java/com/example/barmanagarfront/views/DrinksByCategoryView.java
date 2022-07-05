package com.example.barmanagarfront.views;

import com.example.barmanagarfront.ApiDrink;
import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.services.ApiDrinkService;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.example.barmanagarfront.ResponseOfDrinksJson.DrinkList.*;

@Route(value = "Inventory/:category",layout = MainLayout.class)
@PageTitle("Choose New Drinks")
public class DrinksByCategoryView extends Main
        implements BeforeEnterObserver,HasComponents, HasStyle
{
    private final ApiDrinkService drinkService;
    String categoryName;

    private OrderedList imageContainer;

    private H1 title;
    ArrayList<ApiDrink> drinksByCategory;

    public DrinksByCategoryView(ApiDrinkService drinkService)
    {
        this.drinkService = drinkService;
        constructUI();


    }



    private void constructUI() {
        addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames("items-center", "justify-between");

        VerticalLayout headerContainer = new VerticalLayout();
        VerticalLayout headerTitle = new VerticalLayout();

        headerTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
//        Paragraph description = new Paragraph("Royalty free photos and pictures, courtesy of Unsplash");
//        description.addClassNames("mb-xl", "mt-0", "text-secondary");
        headerContainer.add(headerTitle);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Popularity", "Newest first", "Oldest first");
        sortBy.setValue("Popularity");

        imageContainer = new OrderedList();
        imageContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");

        title = new H1();
        headerTitle.add(title);

        Icon cardIcon = createIcon(VaadinIcon.LIST,"inventory");
        cardIcon.getElement().getThemeList().add("badge");

        container.add(headerTitle,cardIcon, sortBy);
        add(container, imageContainer);

    }

    private String decodeCategoryName(String value)
    {
        if(value.contains("%"))
        {
            return value.replace("%"," / ");
        }

        if ( value.contains("$") )
        {
            return  value.replace("$","/");
        }

        else
        {
            return value;
        }
    }
    private Icon createIcon(VaadinIcon vaadinIcon, String label) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", label);
        // Tooltip
        icon.getElement().setAttribute("title", label);
        return icon;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event)
    {

        categoryName = decodeCategoryName(event.getRouteParameters().get("category").orElse("none"));

        drinksByCategory = drinkService.getDrinksByCategory(categoryName);
        drinksByCategory.forEach(drink -> imageContainer.add(new DrinkViewCard(drink)));

        title.setText(categoryName);
    }


}
