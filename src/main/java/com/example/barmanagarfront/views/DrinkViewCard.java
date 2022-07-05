package com.example.barmanagarfront.views;

import com.example.barmanagarfront.ApiDrink;
import com.example.barmanagarfront.BarInventoryManager;
import com.example.barmanagarfront.models.BarDrink;
import com.example.barmanagarfront.services.ApiDrinkService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


public class DrinkViewCard extends ListItem {


    private ApiDrink apiDrink;
    private NumberField drinkCost;
    private Button plusButton;

    public DrinkViewCard(ApiDrink drink) {

        apiDrink = drink;

        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Div div = new Div();
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.setHeight("160px");

        Image image = new Image();
        image.setSizeFull();
//        image.setWidth("80%");
        image.setSrc(drink.getStrDrinkThumb());


        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(drink.getStrDrink());

        Span subtitle = new Span();
        subtitle.addClassNames("text-s", "text-secondary");
//        subtitle.add(new RouterLink("Open",DrinksByCategoryView.class,
//                new RouteParameters("category",category)));
//        subtitle.setText("14$");

        TextArea description = new TextArea();
        description.addClassName("my-m");
        description.setMinHeight("50px");
        description.setMaxHeight("150px");
        description.setValue(drink.getStrInstructions());

        Span alcoholicBadge = new Span();
        alcoholicBadge.getElement().setAttribute("theme", "badge");
        alcoholicBadge.setText(drink.getStrAlcoholic());

        plusButton = new Button(new Icon(VaadinIcon.PLUS));
        plusButton.setEnabled(false);
        plusButton.addThemeVariants(ButtonVariant.LUMO_ICON);
        plusButton.getElement().setAttribute("aria-label", "Add item");
        plusButton.addClickListener(buttonClickEvent ->
        {
            BarDrink barDrink = new BarDrink();
            barDrink.setId(drink.getIdDrink());
            barDrink.setName(drink.getStrDrink());
            barDrink.setCategory(drink.getStrCategory());
            barDrink.setIsAlcoholic(drink.getStrAlcoholic());
            barDrink.setImage(drink.getStrDrinkThumb());
            barDrink.setPrice(drinkCost.getValue());
            barDrink.setRecommendedGlass(drink.getStrGlass());
            BarInventoryManager.getInstance().addToInventory(barDrink);
            System.out.println(barDrink);
        });

        drinkCost = new NumberField();
        drinkCost.setValue(0.0);
        drinkCost.setStep(0.1);
        drinkCost.setMin(0.0);
        drinkCost.setHasControls(true);
        drinkCost.addValueChangeListener(event -> {
            plusButton.setEnabled(event.getValue() != 0.0);
        });



        HorizontalLayout cardFooter = new HorizontalLayout();
        cardFooter.add(alcoholicBadge,drinkCost,plusButton);

     /*   addClickListener(listItemClickEvent ->
        {
            System.out.println(this.apiDrink);

        });*/
        add(div, header, subtitle, description,cardFooter);

    }
}
