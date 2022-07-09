package com.example.barmanagarfront.events;

import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;
import com.vaadin.flow.component.ComponentEvent;

public class CustomerDialogEvent extends ComponentEvent<NewCustomerDialog>
{
    public CustomerDialogEvent(NewCustomerDialog source, boolean fromClient)
    {
        super(source, false);
    }
}
