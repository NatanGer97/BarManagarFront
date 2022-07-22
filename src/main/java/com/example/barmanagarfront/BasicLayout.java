package com.example.barmanagarfront;

import com.example.barmanagarfront.Singeltones.CartOfDrinksManager;
import com.example.barmanagarfront.factories.ImageFactory;
import com.example.barmanagarfront.observers.IInventoryObserver;
import com.example.barmanagarfront.services.SupplierService;
import com.example.barmanagarfront.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;

//
//@Route(value = "")
//@PageTitle("Main")
public class BasicLayout extends AppLayout implements IInventoryObserver
{
    private final SupplierService apiDrinkService;
    private Image titleImage;

    public BasicLayout(SupplierService apiDrinkService)
    {
        this.apiDrinkService = apiDrinkService;
        StreamResource imageResourceForAnimation = new StreamResource("welcomeAnimation",
                () -> getClass().getResourceAsStream("/images/bar_logo.gif"));
        titleImage = new Image(imageResourceForAnimation, "animation");

        createHeader();
        createDrawer();

        CartOfDrinksManager.getInstance().addObserver(this);




    }

    private void createDrawer()
    {
        Tabs tabs = getTabs();
        addToDrawer(tabs);

    }

    private void createHeader()
    {
        H1 title = new H1("");

        title.addClassNames("text-l","m-m");


        Button homeButton = new Button(VaadinIcon.HOME.create());
        homeButton.addClickListener(buttonClickEvent -> {
            homeButton.getUI().ifPresent(ui -> ui.navigate(HomeLayout.class));
        });

        HorizontalLayout header = new HorizontalLayout(new DrawerToggle(),title ,homeButton);
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setJustifyContentMode(FlexComponent.JustifyContentMode.BETWEEN);
        header.setWidth("100%");
        header.addClassNames("py-0","px-m");
        header.getStyle().set("background-image","linear-gradient(135deg, #f5f7fa 0%, #c3cfe2 100%)");
        addToNavbar(header);
    }

    private Tabs getTabs()
    {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.HOME, "Home", HomeLayout.class));
        tabs.add(createTab(VaadinIcon.DENTAL_CHAIR, "Bar Seats", SeatView.class ));
        tabs.add(createTab(VaadinIcon.GLASS, "Bar Inventory", InventoryView.class ));
        tabs.add(createTab(VaadinIcon.WRENCH,"Manage Inventory", InventoryManagementView.class));
        tabs.add(createTab(VaadinIcon.CART_O, "Added", CartView.class ));
        tabs.add(createTab(VaadinIcon.CHART, "Information", InfoView.class));
        tabs.add(createTab(VaadinIcon.SEARCH, "Order History", OrdersView.class ));
        tabs.add(createTab(VaadinIcon.LOCATION_ARROW, "Branches View", BranchesView.class ));

        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String tabName, Class target)
    {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("margin-inline-start", "var(--lumo-space-xs)")
                .set("padding", "var(--lumo-space-xs)");
        RouterLink link = new RouterLink();
        link.add(icon, new Span(tabName));
        link.setRoute(target);
        link.setTabIndex(-1);
        return new Tab(link);

    }

    @Override
    public void OnSizeChanged(int currentSize)
    {
        System.out.println("currentSize: " + currentSize);
        System.out.println(CartOfDrinksManager.getInstance().getDrinksInventory());
        Notification notification = new Notification(String.valueOf(currentSize));
        notification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        notification.setDuration(2000);
        notification.open();
    }
}
