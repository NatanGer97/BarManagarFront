package com.example.barmanagarfront.views;

import com.example.barmanagarfront.models.BarDrink;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class DrinkBarCard extends ListItem
{
    private BarDrink barDrink;

    public DrinkBarCard(BarDrink barDrink)
    {
        this.barDrink = barDrink;

        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

        Div div = new Div();
        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
                "rounded-m w-full");
        div.setHeight("160px");

        Image image = new Image();
        image.setSizeFull();
//        image.setWidth("80%");
        image.setSrc(this.barDrink.getImage());


        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(barDrink.getName());

        Span priceSpan = new Span();
        priceSpan.addClassNames("text-s", "text-secondary");

        priceSpan.setText(String.valueOf(barDrink.getPrice()));


        Span alcoholicBadge = new Span();
        alcoholicBadge.getElement().setAttribute("theme", "badge");
        alcoholicBadge.setText(barDrink.getIsAlcoholic());

        HorizontalLayout cardFooter = new HorizontalLayout();
        cardFooter.add(alcoholicBadge);

     /*   addClickListener(listItemClickEvent ->
        {
            System.out.println(this.apiDrink);

        });*/
        add(div, header, priceSpan,cardFooter);

    }
}
