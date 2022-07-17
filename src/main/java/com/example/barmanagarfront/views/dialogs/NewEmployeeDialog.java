package com.example.barmanagarfront.views.dialogs;

import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.Customer;
import com.example.barmanagarfront.models.Employee;
import com.example.barmanagarfront.models.EmployeeMapper;
import com.example.barmanagarfront.models.EmployeeMapper.EmployeeDto;
import com.example.barmanagarfront.services.EmployeeService;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import javax.swing.*;

public class NewEmployeeDialog extends Dialog
{
    private final EmployeeService employeeService;
    private NumberField salaryPerHour;
    private IntegerField idNumber;
    private TextField firstName;
    private TextField lastName;
    private Button createButton;
    private Button closeButton;
    private Binder<Employee> employeeBinder;
    private String branchId;
    private EmployeeDto employeeDto;
    public <T extends ComponentEvent<?>> Registration addListener(Class<T> eventType,
                                                                  ComponentEventListener<T> listener)
    {
        return getEventBus().addListener(eventType, listener);
    }
    public NewEmployeeDialog(EmployeeService employeeService, String branchId)
    {
        this.employeeService = employeeService;
        this.setHeaderTitle("New Employee");
        VerticalLayout dialogLayout = createDialogLayout();
        this.branchId = branchId;
        initBinder();


        add(dialogLayout,buildBottomToolbar());

    }
    public NewEmployeeDialog(EmployeeService employeeService, EmployeeDto employeeDto)
    {
        this.employeeService = employeeService;
        this.setHeaderTitle("New Employee");
        this.employeeDto = employeeDto;
        VerticalLayout dialogLayout = createDialogLayout();
        this.branchId = null;
        initBinder();


        add(dialogLayout,buildBottomToolbar());

    }

    private void initBinder()
    {
        employeeBinder = new BeanValidationBinder<>(Employee.class);
        employeeBinder.forMemberField(idNumber).asRequired();
        employeeBinder.forMemberField(salaryPerHour).asRequired();
        employeeBinder.forMemberField(firstName).asRequired();
        employeeBinder.forMemberField(lastName).asRequired();
        employeeBinder.bindInstanceFields(this);
    }


    private VerticalLayout createDialogLayout()
    {
        VerticalLayout dialogLayout;
        if ( employeeDto == null ){
            firstName = new TextField("First name");
            lastName = new TextField("Last name");
            idNumber = new IntegerField("Id");
            salaryPerHour = new NumberField("salary");
            dialogLayout = new VerticalLayout(idNumber,firstName,lastName,salaryPerHour);

        }
        else {
            String[] splitName = StringUtils.split(employeeDto.getFullName());
            firstName = new TextField("First name");
            firstName.setValue(splitName[0]);
            lastName = new TextField("Last name");
            lastName.setValue(splitName[1]);
            idNumber = new IntegerField();
            salaryPerHour = new NumberField();
            salaryPerHour.setValue(employeeDto.getSalaryPerHour());
            dialogLayout = new VerticalLayout(firstName,lastName,salaryPerHour);

        }

        configureSalaryField();


//         dialogLayout = new VerticalLayout(idNumber,firstName,lastName,salaryPerHour);
        dialogLayout.setPadding(false);
        dialogLayout.setSpacing(false);
        dialogLayout.setAlignItems(FlexComponent.Alignment.STRETCH);
        dialogLayout.getStyle().set("width", "18rem").set("max-width", "100%");

        return dialogLayout;
    }



    private void configureSalaryField()
    {
        salaryPerHour.setMin(0.0);
        salaryPerHour.setStep(0.10);
        salaryPerHour.setHasControls(true);
    }


    private Component buildBottomToolbar()
    {
        createButton = new Button("Create",buttonClickEvent -> createEmployee());
        createButton.setEnabled(false);
        closeButton = new Button(VaadinIcon.CLOSE_SMALL.create(), event ->
                fireEvent(new ClosCustomerDialogEvent(this,false)));

        employeeBinder.addStatusChangeListener(statusChangeEvent ->
                createButton.setEnabled(employeeBinder.isValid()));

        Button updateButton = new Button("update");
        updateButton.setEnabled(employeeDto != null);
        updateButton.addClickListener(buttonClickEvent -> updateEmployee());

        return new HorizontalLayout(createButton,updateButton);
    }

    private void updateEmployee()
    {
        Employee employee = new Employee(firstName.getValue(),lastName.getValue(),salaryPerHour.getValue(),
                0);
        employeeService.updateEmployee(employee,employeeDto.getId());
        fireEvent(new ClosCustomerDialogEvent(this,false));

    }

    private void createEmployee()
    {
         Employee employee = new Employee(firstName.getValue(),lastName.getValue(),salaryPerHour.getValue(),
                idNumber.getValue());
        ResponseEntity<Employee> responseEntity = employeeService.saveEmployee(employee,branchId);
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
