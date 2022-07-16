package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BrunchMapper.BrunchDto;
import com.example.barmanagarfront.models.EmployeeMapper.EmployeeDto;
import com.example.barmanagarfront.services.BrunchService;
import com.example.barmanagarfront.services.EmployeeService;
import com.example.barmanagarfront.views.dialogs.NewEmployeeDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Route(value = "Branches/:branchId",layout = MainLayout.class)
@PageTitle("Bar | Brunch ")
//@CssImport("frontend/themes/myapp/views/Single-Branch-View.css")
public class SingleBrunchView extends VerticalLayout implements BeforeEnterObserver, HasComponents, HasStyle
{
    private final Logger logger;
    private final BrunchService brunchService;
    private final EmployeeService employeeService;
    private String branchId;
    private Grid<EmployeeDto> employeeDtoGrid;
    private Grid<EmployeeDto> notInBranchEmployee;
    private  BrunchDto brunchDto;
    private List<EmployeeDto> employeeDtoList;
    private VerticalLayout employeesLayout;
    private Button newEmployeeButton;

    public SingleBrunchView(BrunchService brunchService, EmployeeService employeeService)
    {

        logger = LoggerFactory.getLogger(SingleBrunchView.class);
        this.brunchService = brunchService;
        this.employeeService = employeeService;
        employeeDtoGrid = new Grid<>(EmployeeDto.class,false);
        notInBranchEmployee = new Grid<>(EmployeeDto.class,false);
        initNewEmployeeButton();

        employeesLayout = new VerticalLayout();
        employeesLayout.setAlignItems(Alignment.CENTER);


        add(employeesLayout,getContent());
    }

    private void initNewEmployeeButton()
    {
        newEmployeeButton = new Button("New Employee");
        newEmployeeButton.setWidthFull();
        newEmployeeButton.addClickListener(event -> createNewEmployee());
    }

    private void createNewEmployee()
    {
        NewEmployeeDialog newEmployeeDialog = new NewEmployeeDialog(employeeService,branchId);
        newEmployeeDialog.addListener(ClosCustomerDialogEvent.class, closeDialogEvent -> {
            updateEmployeeGrid();
            updateNotInBranchGrid();
            newEmployeeDialog.close();
        });
        newEmployeeDialog.open();

    }


    private Component getContent()
    {
        VerticalLayout notInBranchEmployeeLayout = new VerticalLayout();
        H3 availableEmployeesTitle = new H3("Available employees");
        notInBranchEmployeeLayout.add(availableEmployeesTitle,notInBranchEmployee);
        VerticalLayout branchEmployeeLayout = new VerticalLayout();
        branchEmployeeLayout.add(new H3("Employees in branch"),employeeDtoGrid);

        HorizontalLayout content = new HorizontalLayout(branchEmployeeLayout,newEmployeeButton,
                notInBranchEmployeeLayout);
//        content.setFlexGrow(2, employeeDtoGrid);
        content.setFlexGrow(1, content);
        content.setSizeFull();

        return content;
    }

    private void setupNotInBranchGrid()
    {
        notInBranchEmployee.setAllRowsVisible(true);
        notInBranchEmployee.addColumn(employeeDto -> employeeDto.getFullName())
                .setHeader("Employee")
                .setTextAlign(ColumnTextAlign.CENTER);
        notInBranchEmployee.addComponentColumn(this::createAddButtonColumn);
        updateNotInBranchGrid();

    }

    private Component createAddButtonColumn(EmployeeDto employeeDto)
    {
        Button addEmployee = new Button("Add", VaadinIcon.PLUS.create());

        addEmployee.addClickListener(buttonClickEvent -> addEmployeeToBranch(employeeDto));

        HorizontalLayout horizontalLayout = new HorizontalLayout(addEmployee);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }

    private Component createRemoveButtonColumn(EmployeeDto employeeDto)
    {
        Button addEmployee = new Button("Remove", VaadinIcon.TRASH.create());

        addEmployee.addClickListener(buttonClickEvent -> {
            System.out.println(buttonClickEvent);
            removeEmployeeFromBranch(employeeDto);
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(addEmployee);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }

    private void removeEmployeeFromBranch(EmployeeDto employeeDto)
    {
        brunchService.removeEmployeeFromBranch(branchId,employeeDto.getId());
        updateNotInBranchGrid();
        updateEmployeeGrid();
    }

    private void addEmployeeToBranch(EmployeeDto employeeDto)
    {
        brunchService.addEmployeeToBranch(branchId,employeeDto.getId());
        updateNotInBranchGrid();
        updateEmployeeGrid();
    }

    private void updateNotInBranchGrid()
    {
        notInBranchEmployee.setItems(employeeService.getAllEmployees(branchId));
    }


    private void initEmployeesGrid()
    {
        employeeDtoGrid.setAllRowsVisible(true);
        employeeDtoGrid.addColumn(employeeDto -> employeeDto.getFullName()).setHeader("Employee")
                .setTextAlign(ColumnTextAlign.CENTER);
        employeeDtoGrid.addComponentColumn(employeeDto -> createRemoveButtonColumn(employeeDto));
        updateEmployeeGrid();

    }

    private void updateEmployeeGrid()
    {
        try{
            this.employeeDtoList = employeeService.getBranchEmployees(branchId);

        } catch (NullPointerException exception)
        {
            this.employeeDtoList = new ArrayList<>();
        }
        finally
        {
            employeeDtoGrid.setItems(this.employeeDtoList);
        }


    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
        System.out.println("before");
        branchId = beforeEnterEvent.getRouteParameters().get("branchId").get();
        this.brunchDto = brunchService.getBranch(branchId);
      /*  try
        {
            employeeDtoList = employeeService.getBranchEmployees(brunchDto.getBrunchId());
        }
        catch (NullPointerException exception)
        {
            this.employeeDtoList = new ArrayList<>();
        }*/
        initEmployeesGrid();
        setupNotInBranchGrid();
        employeesLayout.add(new Text(brunchDto.getBranchName()));
    }


}
