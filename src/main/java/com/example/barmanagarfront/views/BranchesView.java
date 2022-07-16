package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.models.BrunchMapper.BrunchDto;
import com.example.barmanagarfront.observers.Seat;
import com.example.barmanagarfront.services.BrunchService;
import com.example.barmanagarfront.views.OrderBuilderForm;
import com.example.barmanagarfront.views.dialogs.NewBranchDialog;
import com.example.barmanagarfront.views.dialogs.OrderBillDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
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
    private Button newBranchButton;
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
        final H1 title;
        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();
        headerTitle.setAlignItems(Alignment.CENTER);

        headerTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        headerContainer.add(headerTitle);

        title = new H1(new Text("Branches"));
        VerticalLayout newBranchLayout = new VerticalLayout();
        setupNewBranchButton();
        newBranchLayout.setAlignItems(Alignment.CENTER);
        newBranchLayout.add(newBranchButton);

        headerTitle.add(title,newBranchLayout);
        return headerContainer;
    }

    private void setupNewBranchButton()
    {
        newBranchButton = new Button("new branch");
        newBranchButton.setIcon(VaadinIcon.HOME_O.create());
        newBranchButton.addClickListener(buttonClickEvent -> createNewBranch());

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




