package com.example.barmanagarfront.views;

import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.factories.ImageFactory;
import com.example.barmanagarfront.models.BranchMapper.BranchDto;
import com.example.barmanagarfront.services.BranchService;
import com.example.barmanagarfront.views.dialogs.NewBranchDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParameters;

import java.util.List;

@Route(value = "Branches",layout = BasicLayout.class)
@PageTitle("Bar | Branches ")
public class BranchesView extends VerticalLayout
{

    private final BranchService brunchService;
    private Grid<BranchDto> brunchDtoGrid;
    private List<BranchDto> brunchList;
    private Button newBranchButton,getAllEmployees;
    public BranchesView(BranchService brunchService)
    {
        this.brunchService = brunchService;
        brunchList = brunchService.getBranchesDtos();
        brunchDtoGrid = new Grid<>(BranchDto.class,false);

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
        Image branchesLogoImage = ImageFactory.getInstance().getImage("branchesLogo.png");
        Image branchesTextImage = ImageFactory.getInstance().getImage("branchesTextLogo.png");
        branchesLogoImage.setWidth("80px");
        branchesLogoImage.setHeight("80px");
        headerTitle.add(new H1(branchesTextImage,branchesLogoImage),newBranchLayout);
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
        brunchDtoGrid.addColumn(brunchDto -> brunchDto.getBranchName()).setHeader("Brunch")
                        .setTextAlign(ColumnTextAlign.CENTER);
        brunchDtoGrid.addColumn(brunchDto -> brunchDto.getEmployeesIds().size()).setHeader("Employees")
                        .setTextAlign(ColumnTextAlign.CENTER);
        brunchDtoGrid.addComponentColumn(brunchDto -> createButtons(brunchDto))
                        .setTextAlign(ColumnTextAlign.CENTER);

        updateGrid();
    }

    private void updateGrid()
    {
        brunchDtoGrid.setItems(brunchService.getBranchesDtos());
    }

    private Component createButtons(BranchDto brunchDto)
    {
        Button toBrunchButton = new Button("Enter", VaadinIcon.POINTER.create());
        toBrunchButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        toBrunchButton.setIconAfterText(true);

        toBrunchButton.addClickListener(buttonClickEvent -> {
            System.out.println(brunchDto    );
            toBrunchButton.getUI().ifPresent(ui -> ui.navigate(SingleBrunchView.class,
                    new RouteParameters("branchId",brunchDto.getBranchId())));
        });

        Button removeButton = new Button("Remove");
        removeButton.setIconAfterText(true);
        removeButton.setIcon(VaadinIcon.TRASH.create());
        removeButton.addThemeVariants(ButtonVariant.LUMO_ERROR,ButtonVariant.LUMO_TERTIARY);
        removeButton.addClickListener(event -> {
            brunchService.removeBranch(brunchDto.getBranchId());
            updateGrid();
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(toBrunchButton, removeButton);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);

        return horizontalLayout;
    }
}




