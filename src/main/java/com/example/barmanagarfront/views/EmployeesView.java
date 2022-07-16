package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.EmployeeMapper.EmployeeDto;
import com.example.barmanagarfront.services.EmployeeService;
import com.example.barmanagarfront.views.dialogs.NewEmployeeDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

@Route(value = "Employees",layout = MainLayout.class)
@PageTitle("Employees ")
public class EmployeesView extends VerticalLayout
{
    private final EmployeeService employeeService;
    private List<EmployeeDto> employeeDtoList;
    private Grid<EmployeeDto> employeeDtoGrid;

    public EmployeesView(EmployeeService employeeService)
    {
        this.employeeService = employeeService;
        this.employeeDtoList = employeeService.getAllEmployees();
        this.employeeDtoGrid = new Grid<>(EmployeeDto.class,false);

        initGrid();

        add(employeeDtoGrid);
    }

    private void initGrid()
    {
        employeeDtoGrid.addColumn(employeeDto -> StringUtils.split(employeeDto.getFullName())[0])
                .setHeader("first name");
        employeeDtoGrid.addColumn(employeeDto -> StringUtils.split(employeeDto.getFullName())[1])
                .setHeader("last name");
        employeeDtoGrid.addComponentColumn(employeeDto -> createButtons(employeeDto));

        updateGrid();
    }

    private void updateGrid()
    {
        this.employeeDtoGrid.setItems(employeeService.getAllEmployees());
    }

    private Component createButtons(EmployeeDto employeeDto)
    {
        Button openEmployeeDialog = new Button("Update", VaadinIcon.POINTER.create());
        openEmployeeDialog.addClickListener(buttonClickEvent -> openDialog(employeeDto));

        Button removeEmployee = new Button(VaadinIcon.TRASH.create());
        removeEmployee.addThemeVariants(ButtonVariant.LUMO_ERROR);
        removeEmployee.addClickListener(buttonClickEvent -> removeEmployee(employeeDto));


        HorizontalLayout horizontalLayout = new HorizontalLayout(openEmployeeDialog,removeEmployee);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }

    private void removeEmployee(EmployeeDto employeeDto)
    {
        System.out.println("Delete");
        employeeService.deleteEmployee(employeeDto.getId());
        updateGrid();
    }

    private void openDialog(EmployeeDto employeeDto)
    {
        NewEmployeeDialog newEmployeeDialog = new NewEmployeeDialog(employeeService,employeeDto);
        newEmployeeDialog.open();
        newEmployeeDialog.addListener(ClosCustomerDialogEvent.class, closeDialogEvent -> {
            updateGrid();
            newEmployeeDialog.close();
        });
    }
}
