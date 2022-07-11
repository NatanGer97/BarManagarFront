package com.example.barmanagarfront.events;

import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.dialog.Dialog;

import java.awt.*;

public class CustomerDialogEvent extends ComponentEvent<com.vaadin.flow.component.dialog.Dialog>
{
    public CustomerDialogEvent(Dialog source, boolean fromClient)
    {
        super(source, false);
    }
}
