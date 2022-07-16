package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BrunchMapper.BrunchDto;
import com.example.barmanagarfront.services.BrunchService;
import com.example.barmanagarfront.views.dialogs.NewBranchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import java.util.List;

@Route(value = "Brunches",layout = MainLayout.class)
@PageTitle("Bar | Brunches ")
public class BranchesView extends VerticalLayout
{

    private final BrunchService brunchService;
    private Grid<BrunchDto> brunchDtoGrid;
    private List<BrunchDto> brunchList;
    private Button newBranchButton,getAllEmployees;
    public BranchesView(BrunchService brunchService)
    {
        this.brunchService = brunchService;
        brunchList = brunchService.getBrunchesDtos();
        brunchDtoGrid = new Grid<>(BrunchDto.class,false);

        setupTitleSection();


        initGrid();

        add(setupTitleSection(),brunchDtoGrid);
    }

    private VerticalLayout setupTitleSection()
    {
        final H3 title;
        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();
        headerTitle.setAlignItems(Alignment.CENTER);

        headerTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(headerTitle);

        title = new H3("Branches");
        title.setHeight("50px");

        HorizontalLayout newBranchLayout = new HorizontalLayout();
        setupNewBranchButtons();
        newBranchLayout.setAlignItems(Alignment.CENTER);
        newBranchLayout.setJustifyContentMode(JustifyContentMode.BETWEEN);
        newBranchLayout.add(newBranchButton,getAllEmployees);

        headerTitle.add(title,newBranchLayout);
        return headerContainer;
    }

    private void setupNewBranchButtons()
    {
        newBranchButton = new Button("new branch");
        newBranchButton.setIcon(VaadinIcon.HOME_O.create());
        newBranchButton.addClickListener(buttonClickEvent -> createNewBranch());
        newBranchButton.setIconAfterText(true);

        getAllEmployees = new Button(VaadinIcon.USERS.create());
        getAllEmployees.setIconAfterText(true);
        getAllEmployees.setText("all employees");
        getAllEmployees.addClickListener(buttonClickEvent -> openEmployeesView());
    }

    private void openEmployeesView()
    {
        getAllEmployees.getUI().ifPresent(ui -> ui.navigate(EmployeesView.class));
    }

    private void createNewBranch()
    {
        NewBranchDialog newBranchDialog = new NewBranchDialog(brunchService);
        newBranchDialog.addListener(ClosCustomerDialogEvent.class,closeEvent ->{
        newBranchDialog.close();
        updateGrid();
        });
        newBranchDialog.open();
    }

    private void initGrid()
    {
        brunchDtoGrid.setAllRowsVisible(true);
        brunchDtoGrid.addColumn(brunchDto -> brunchDto.getBranchName()).setHeader("Brunch");
        brunchDtoGrid.addColumn(brunchDto -> brunchDto.getEmployeesIds().size()).setHeader("Employees");
        brunchDtoGrid.addComponentColumn(brunchDto -> createButtons(brunchDto));

        updateGrid();
    }

    private void updateGrid()
    {
        brunchDtoGrid.setItems(brunchService.getBrunchesDtos());
    }

    private Component createButtons(BrunchDto brunchDto)
    {
        Button toBrunchButton = new Button("Enter", VaadinIcon.POINTER.create());

        toBrunchButton.addClickListener(buttonClickEvent -> {
            System.out.println(brunchDto.getBrunchId());
            toBrunchButton.getUI().ifPresent(ui -> ui.navigate(SingleBrunchView.class,
                    new RouteParameters("branchId",brunchDto.getBrunchId())));
        });


        HorizontalLayout horizontalLayout = new HorizontalLayout(toBrunchButton);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }
}




