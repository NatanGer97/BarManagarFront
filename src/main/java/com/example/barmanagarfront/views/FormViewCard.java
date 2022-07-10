package com.example.barmanagarfront.views;

import com.example.barmanagarfront.services.SupplierService;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.QueryParameters;
import com.vaadin.flow.router.RouteParameters;
import com.vaadin.flow.router.RouterLink;
import com.example.barmanagarfront.enums.eFilterType;

import java.util.Map;

public class FormViewCard extends ListItem{

    private final String originalCategoryName;
    private String modifiedCategoryName;

    public FormViewCard(String name, eFilterType type) {
        originalCategoryName = name;
        modifyCategoryName();
        addClassNames("bg-contrast-5", "flex", "flex-col", "items-start", "p-m", "rounded-l");

//        Div div = new Div();
//        div.addClassNames("bg-contrast", "flex items-center", "justify-center", "mb-m", "overflow-hidden",
//                "rounded-m w-full");
//        div.setHeight("160px");
//
//        Image image = new Image();
//        image.setSizeFull();
////        image.setWidth("80%");
//        image.setSrc("https://www.thecocktaildb.com/images/media/drink/5noda61589575158.jpg");
//
//
//        div.add(image);

        Span header = new Span();
        header.addClassNames("text-xl", "font-semibold");
        header.setText(name);

//        Span subtitle = new Span();
//        subtitle.addClassNames("text-s", "text-secondary");
//        subtitle.add(new RouterLink("Open",DrinksByCategoryView.class,
//                new RouteParameters("category",name)));
//        subtitle.setText("14$");

        Paragraph description = new Paragraph();
        description.addClassName("my-m");

        Span badge = new Span();
        badge.getElement().setAttribute("theme", "badge");
        badge.setText(type.name());

        add(header, description, badge);

//        addClickListener(listItemClickEvent ->
//        {
//            //System.out.println(category);
//            this.getUI().ifPresent(ui -> ui.navigate(
//                    DrinksByCategoryView.class,
//                    new RouteParameters("category",modifiedCategoryName)
//            ));
//        });
    }


    private void modifyCategoryName()
    {
        if ( this.originalCategoryName.contains(" / ") )
        {
            modifiedCategoryName = originalCategoryName.replace(" / ","%");
//            modifiedCategoryName = originalCategoryName.replaceFirst("/\\s\\W+\\s/gm","sb");
        }
        else if ( this.originalCategoryName.contains("/") )
        {
            modifiedCategoryName = originalCategoryName.replace("/","$");
        }
        else
        {
            modifiedCategoryName = originalCategoryName;
        }
    }

    public String getModifiedCategoryName() {
        return modifiedCategoryName;
    }
}
