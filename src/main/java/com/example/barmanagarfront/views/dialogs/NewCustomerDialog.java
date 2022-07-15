package com.example.barmanagarfront.views.dialogs;

import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.Customer;
import com.example.barmanagarfront.services.CustomerService;
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
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Configuration
public class NewCustomerDialog extends Dialog
{
    private final CustomerService customerService;

    private IntegerField idNumber;
    private TextField firstName;
    private TextField lastName;
    private Button saveButton;
    private Button closeButton;

    private Binder<Customer> customerBinder;
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }
    public NewCustomerDialog(CustomerService customerService)
    {
        this.customerService = customerService;
        this.setHeaderTitle("New Customer");
        VerticalLayout dialogLayout = createDialogLayout();

        customerBinder = new BeanValidationBinder<>(Customer.class);
        customerBinder.forMemberField(idNumber).asRequired();
        customerBinder.forMemberField(firstName).asRequired();
        customerBinder.forMemberField(lastName).asRequired();
        customerBinder.bindInstanceFields(this);


        add(dialogLayout,buildBottomToolbar());
    }

    private Component buildBottomToolbar()
    {
        saveButton = new Button("save",buttonClickEvent -> saveCustomer());
        saveButton.setEnabled(false);
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create(), event ->
                fireEvent(new ClosCustomerDialogEvent(this,false)));

        customerBinder.addStatusChangeListener(statusChangeEvent ->
                saveButton.setEnabled(customerBinder.isValid()));

        return new HorizontalLayout(saveButton);
    }

    private void saveCustomer()
    {
        Customer customer = new Customer(idNumber.getValue(),firstName.getValue(),lastName.getValue());
        ResponseEntity<Customer> responseEntity = customerService.saveCustomer(customer);
        if ( responseEntity.getStatusCode().equals(HttpStatus.CREATED) )
        {
            System.out.println(responseEntity.getStatusCode());
            Notification notification = new Notification(VaadinIcon.CHECK_CIRCLE.create());
            notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            notification.setDuration(3000);
            notification.open();
            notification.addDetachListener(detachEvent ->
                    fireEvent(new ClosCustomerDialogEvent(this,false)));
        }

    }

    private VerticalLayout createDialogLayout()
    {
        firstName = new TextField("First name");
        lastName = new TextField("Last name");
        idNumber = new IntegerField("Id");


        VerticalLayout dialogLayout = new VerticalLayout(idNumber,firstName,lastName);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }




}
