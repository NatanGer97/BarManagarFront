package com.example.barmanagarfront.views;

import com.example.barmanagarfront.CartOfDrinksManager;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IRemoveDrinkObserver;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import java.util.ArrayList;
import java.util.List;

public class DrinkBarCard extends ListItem
{
    private BarDrink barDrink;
    private List<IRemoveDrinkObserver> removeDrinkObservers;

    public DrinkBarCard(BarDrink barDrink)
    {
        removeDrinkObservers = new ArrayList<>();
        this.barDrink = barDrink;

        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Div divImage = new Div();
        divImage.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        divImage.setHeight("160px");


        Image image = new Image();
        image.setSizeFull();
//        image.setWidth("80%");
        image.setSrc(this.barDrink.getImage());
        divImage.add(image);

        Span drinkName = new Span();
        drinkName.addClassNames("text-xl", "font-semibold");
        drinkName.setText(barDrink.getName());

        headerLayout.add(drinkName,divImage);

        VerticalLayout topButtonBar = new VerticalLayout();
        Button removeButton = new Button(VaadinIcon.CLOSE_SMALL.create());
        topButtonBar.add(removeButton);

        removeButton.addClickListener(buttonClickEvent ->
        {
            CartOfDrinksManager.getInstance().removeFromInventory(barDrink);
            doWhenRemoved();
        });


        TextField priceSpan = new TextField();
        priceSpan.setValue(String.valueOf(barDrink.getPrice()));
        priceSpan.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        priceSpan.setSuffixComponent(VaadinIcon.MONEY.create());


        Span alcoholicBadge = new Span();
        alcoholicBadge.getElement().setAttribute("theme", "badge pill");
        alcoholicBadge.setText(barDrink.getIsAlcoholic());

        HorizontalLayout cardFooter = new HorizontalLayout();
        cardFooter.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        cardFooter.add(alcoholicBadge,priceSpan);

//        add(divImage, drinkName,cardFooter);
        add(removeButton,headerLayout,cardFooter);

    }

    private void doWhenRemoved()
    {
        if ( removeDrinkObservers != null )
        {
            notifyObservers();
        }
    }

    public void addObserver(IRemoveDrinkObserver observer)
    {
        removeDrinkObservers.add(observer);
    }

    public void notifyObservers()
    {
        removeDrinkObservers.forEach(IRemoveDrinkObserver::onRemove);
    }
}
