package com.example.barmanagarfront.views;

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

@Route(value = "")
@PageTitle("Welcome")
public class OpenLayout extends VerticalLayout
{
    private Image image;
    private Image logo;
    AnimationDialog animationDialog;;

    public OpenLayout()
    {
        this.setAlignItems(Alignment.CENTER);

        logo = ImageFactory.getInstance().getImage("bar_logo.gif");

        image = ImageFactory.getInstance().getImage("helloBot.gif");

        add(createHeader(),createContent(image));

    }
    private HorizontalLayout createHeader()
    {

        H1 title = new H1(logo);
        title.addClassNames("text-l","m-m");

        Button homeButton = new Button(VaadinIcon.HOME.create());
        homeButton.addClickListener(buttonClickEvent -> {
            homeButton.getUI().ifPresent(ui -> ui.navigate(HomeLayout.class));
        });

        HorizontalLayout header = new HorizontalLayout(title);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.CENTER);
        header.setWidth("100%");
        header.addClassNames("py-0","px-m");
        return header;
    }


    public VerticalLayout createContent(Image image){
        VerticalLayout content = new VerticalLayout();
        content.setAlignItems(Alignment.CENTER);
        content.setJustifyContentMode(JustifyContentMode.CENTER);
        Button enterButton = new Button("Enter");
        enterButton.setWidth("300px");
        enterButton.setIcon(VaadinIcon.POINTER.create());
        enterButton.setIconAfterText(true);
        enterButton.addClassNames("rounded-l w-full");
        enterButton.addClickListener(buttonClickEvent -> {
            enterButton.getUI().ifPresent(ui -> ui.navigate(HomeLayout.class));
        });

        content.add(enterButton,image);

        return content;

    }


}