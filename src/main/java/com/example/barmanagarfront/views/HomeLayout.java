package com.example.barmanagarfront.views;
import com.example.barmanagarfront.BasicLayout;
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
    Image splashImage;

    public HomeLayout()
    {
        add(createTitleContainer());
    }

    private VerticalLayout createTitleContainer()
    {
        StreamResource imageResourceForAnimation = new StreamResource("welcomeAnimation",
                () -> getClass().getResourceAsStream("/images/bar_logo.gif"));
        splashImage = new Image(imageResourceForAnimation, "animation");
        VerticalLayout layout = new VerticalLayout(splashImage);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        layout.setAlignItems(Alignment.CENTER);

        return layout;
    }

    private void splashScreen()
    {
        StreamResource imageResourceForAnimation = new StreamResource("welcomeAnimation",
                () -> getClass().getResourceAsStream("/images/helloBot.gif"));
        splashImage = new Image(imageResourceForAnimation, "animation");
        AnimationDialog animationDialog = new AnimationDialog(splashImage);
        animationDialog.open();


    }
}
