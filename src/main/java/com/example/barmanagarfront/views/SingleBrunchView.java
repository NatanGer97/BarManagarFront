package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BrunchMapper.BrunchDto;
import com.example.barmanagarfront.models.EmployeeMapper.EmployeeDto;
import com.example.barmanagarfront.services.BrunchService;
import com.example.barmanagarfront.services.EmployeeService;
import com.example.barmanagarfront.views.dialogs.NewCustomerDialog;
import com.example.barmanagarfront.views.dialogs.NewEmployeeDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.virtuallist.VirtualList;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.dom.ElementFactory;
import com.vaadin.flow.router.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.stream.Collectors;

@Route(value = "Branches/:branchId",layout = MainLayout.class)
@PageTitle("Bar | Brunch ")
public class SingleBrunchView extends VerticalLayout implements BeforeEnterObserver
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

        HorizontalLayout content = new HorizontalLayout(employeeDtoGrid,newEmployeeButton,notInBranchEmployee);
        content.setFlexGrow(2, employeeDtoGrid);
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
        updateNotInBranchGrid();

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
        updateEmployeeGrid();
    }

    private void updateEmployeeGrid()
    {
        this.employeeDtoList = employeeService.getBranchEmployees(branchId);
        employeeDtoGrid.setItems(this.employeeDtoList);

    }


    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent)
    {
        System.out.println("before");
        branchId = beforeEnterEvent.getRouteParameters().get("branchId").get();
        this.brunchDto = brunchService.getBranch(branchId);
        employeeDtoList = employeeService.getBranchEmployees(brunchDto.getBrunchId());
        initEmployeesGrid();
        setupNotInBranchGrid();
        employeesLayout.add(new Text(brunchDto.getBrunchName()));
    }


}
