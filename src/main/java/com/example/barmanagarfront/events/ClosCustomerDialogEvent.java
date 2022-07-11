package com.example.barmanagarfront.events;

import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;
import com.vaadin.flow.component.dialog.Dialog;

public class ClosCustomerDialogEvent extends CustomerDialogEvent
{

    public ClosCustomerDialogEvent(Dialog source, boolean fromClient)
    {
        super(source, false);
    }
}
