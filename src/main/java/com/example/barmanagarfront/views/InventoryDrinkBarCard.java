package com.example.barmanagarfront.views;

import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.observers.IRemoveFromInventoryObserver;
import com.example.barmanagarfront.services.InventoryService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.TextFieldVariant;

import java.util.ArrayList;
import java.util.List;

public class InventoryDrinkBarCard extends ListItem
{
    private final BarDrink barDrink;

    List<IRemoveFromInventoryObserver> observers;


    public InventoryDrinkBarCard(BarDrink barDrink)
    {
        this.barDrink = barDrink;
        observers = new ArrayList<>();

        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        VerticalLayout headerLayout = new VerticalLayout();
        headerLayout.setAlignItems(FlexComponent.Alignment.STRETCH);

        Div divImage = new Div();
        divImage.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        divImage.setHeight("160px");


        Image image = new Image();
        image.setSizeFull();
        image.setSrc(this.barDrink.getImage());
        divImage.add(image);

        Span drinkName = createCardTitle(barDrink);

        headerLayout.add(drinkName,divImage);


        Button removeButton = new Button(VaadinIcon.CLOSE_SMALL.create());

        removeButton.addClickListener(buttonClickEvent -> onRemove());


        TextField priceSpan = createPriceField(barDrink);
        priceSpan.setReadOnly(true);



        Span alcoholicBadge = createAlcoholicLabel(barDrink);

        HorizontalLayout cardFooter = createCardFooter(priceSpan, alcoholicBadge);

        add(removeButton,headerLayout,cardFooter);

    }

    private Span createCardTitle(BarDrink barDrink)
    {
        Span drinkName = new Span();
        drinkName.addClassNames("text-xl", "font-semibold");
        drinkName.setText(barDrink.getName());
        return drinkName;
    }

    private HorizontalLayout createCardFooter(TextField priceSpan, Span alcoholicBadge)
    {
        HorizontalLayout cardFooter = new HorizontalLayout();
        cardFooter.setJustifyContentMode(FlexComponent.JustifyContentMode.EVENLY);
        cardFooter.add(alcoholicBadge, priceSpan);
        return cardFooter;
    }

    private Span createAlcoholicLabel(BarDrink barDrink)
    {
        Span alcoholicBadge = new Span();
        alcoholicBadge.getElement().setAttribute("theme", "badge pill");
        alcoholicBadge.setText(this.barDrink.getIsAlcoholic());
        return alcoholicBadge;
    }

    private TextField createPriceField(BarDrink barDrink)
    {
        TextField priceSpan = new TextField();
        priceSpan.setValue(String.format("%.2f",barDrink.getPrice()));
//        priceSpan.setValue(String.valueOf(this.barDrink.getPrice()));
        priceSpan.addThemeVariants(TextFieldVariant.LUMO_ALIGN_CENTER);
        priceSpan.setSuffixComponent(VaadinIcon.MONEY.create());
        return priceSpan;
    }


    private void notifyObservers()
    {
        if ( observers != null  )
        {
            observers.forEach(observers -> observers.onRemoveFromInventory(this.barDrink.getId()));
        }
    }

    public void addObserver(IRemoveFromInventoryObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(IRemoveFromInventoryObserver observer)
    {
        observers.remove(observer);
    }

    private void onRemove()
    {
        notifyObservers();
    }

}
