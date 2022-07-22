package com.example.barmanagarfront.views.dialogs;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.observers.ICloseWelcomeImageObserver;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.server.Command;
import com.vaadin.flow.shared.Registration;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class AnimationDialog extends Dialog
{
    private final Image image;


    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }


    public AnimationDialog(Image image)
    {

        this.image = image;

        VerticalLayout contentLayout = new VerticalLayout();

        contentLayout.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        contentLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        contentLayout.add(createHeader(),image);

        add(createHeader(),image);

    }

    private HorizontalLayout createHeader()
    {
        HorizontalLayout header = new HorizontalLayout();
       Button closeButton = new Button(VaadinIcon.CLOSE.create());
        closeButton.addClickListener(event -> this.close());

        header.setJustifyContentMode(FlexComponent.JustifyContentMode.END);
        header.setAlignItems(FlexComponent.Alignment.END);
        header.add(closeButton);
        return header;
    }
}
