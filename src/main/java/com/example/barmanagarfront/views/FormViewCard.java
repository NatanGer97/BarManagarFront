package com.example.barmanagarfront.views;

import com.vaadin.flow.component.html.*;
import com.example.barmanagarfront.enums.eFilterType;

public class FormViewCard extends ListItem{

    private final String originalName;
    private String modifiedName;

    public FormViewCard(String name, eFilterType type) {
        originalName = name;
        modifyName();
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


    private void modifyName()
    {
        if ( this.originalName.contains(" / ") )
        {
            modifiedName = originalName.replace(" / ","%");
//            modifiedCategoryName = originalCategoryName.replaceFirst("/\\s\\W+\\s/gm","sb");
        }
        else if ( this.originalName.contains("/") )
        {
            modifiedName = originalName.replace("/","$");
        }
        else
        {
            modifiedName = originalName;
        }
    }

    public String getModifiedName() {
        return modifiedName;
    }
}
