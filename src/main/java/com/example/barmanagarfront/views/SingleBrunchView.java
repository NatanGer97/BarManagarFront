package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

@Route(value = "Branches/:branchName",layout = MainLayout.class)
//@PageTitle("Bar | Brunch ")
public class SingleBrunchView extends VerticalLayout implements HasDynamicTitle, BeforeEnterObserver
{
    private String title;


    @Override
    public String getPageTitle()
    {
        return title;
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
        if  (beforeEnterEvent.getRouteParameters().get("branchName").isPresent())
        {
            title = beforeEnterEvent.getRouteParameters().get("branchName").get();
        }
    }
}
