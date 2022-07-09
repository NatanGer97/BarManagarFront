package com.example.barmanagarfront.events;

import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;

public class ClosCustomerDialogEvent extends CustomerDialogEvent
{

    public ClosCustomerDialogEvent(NewCustomerDialog source, boolean fromClient)
    {
        super(source, false);
    }
}
