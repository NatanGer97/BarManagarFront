package com.example.barmanagarfront.views.dialogs;

import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.Branch;
import com.example.barmanagarfront.models.Employee;
import com.example.barmanagarfront.services.BrunchService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class NewBranchDialog extends Dialog
{
    private final BrunchService brunchService;
    private TextField branchName,country,city;
    private Button saveButton, closeButton;
    private Binder<Branch> branchBinder;


    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }

    public NewBranchDialog(BrunchService brunchService)
    {
        this.brunchService = brunchService;
        this.setHeaderTitle("New Branch");
        VerticalLayout dialogLayout = createDialogLayout();
        branchBinder = new BeanValidationBinder<>(Branch.class);
        branchBinder.forMemberField(city).asRequired();
        branchBinder.forMemberField(country).asRequired();
        branchBinder.forMemberField(branchName).asRequired();
        branchBinder.bindInstanceFields(this);

        add(dialogLayout,buildBottomToolbar());
    }

    private VerticalLayout createDialogLayout()
    {
        branchName = new TextField("Branch Name");
        country = new TextField("Country");
        city = new TextField("City");

        VerticalLayout dialogLayout = new VerticalLayout(branchName,country,city);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }

    private Component buildBottomToolbar()
    {
        saveButton = new Button("Create",buttonClickEvent -> createBrunch());
        saveButton.setEnabled(false);
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create(), event ->
                fireEvent(new ClosCustomerDialogEvent(this,false)));
        branchBinder.addStatusChangeListener(statusChangeEvent -> {
           saveButton.setEnabled(branchBinder.isValid());
        });

        return new HorizontalLayout(saveButton);
    }

    private void createBrunch()
    {
        Branch branch = new Branch(branchName.getValue(),country.getValue(),city.getValue());
        System.out.println(branch);
        ResponseEntity<Branch> responseEntity = brunchService.createBranch(branch);
        if ( responseEntity.getStatusCode().equals(HttpStatus.CREATED) )
        {
            System.out.println(responseEntity.getStatusCode());
            Notification notification = new Notification(VaadinIcon.CHECK_CIRCLE.create());
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(3000);
            notification.open();
            notification.addDetachListener(detachEvent ->
                    fireEvent(new ClosCustomerDialogEvent(this, false)));
            fireEvent(new ClosCustomerDialogEvent(this, false));
        }
    }
}
