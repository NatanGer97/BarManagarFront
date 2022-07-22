package com.example.barmanagarfront.views;
import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.factories.ImageFactory;
import com.example.barmanagarfront.views.dialogs.AnimationDialog;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.StreamResource;

@Route(value = "main",layout = BasicLayout.class)
@PageTitle("Main")
public class HomeLayout extends VerticalLayout
{


    public HomeLayout()
    {
        add(createTitleContainer());
    }

    private VerticalLayout createTitleContainer()
    {

        Image barLogoImage = ImageFactory.getInstance().getImage("bar_logo.gif");
        VerticalLayout layout = new VerticalLayout(barLogoImage);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);
        Image cocktailGif = ImageFactory.getInstance().getImage("cocktailGif.gif");
        Image helloGif = ImageFactory.getInstance().getImage("helloBot.gif");
        HorizontalLayout animationsLayout = new HorizontalLayout();
        animationsLayout.add(cocktailGif,helloGif);
        layout.add(animationsLayout);

        return layout;
    }


}
