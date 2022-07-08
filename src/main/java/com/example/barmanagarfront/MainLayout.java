package com.example.barmanagarfront;

import com.example.barmanagarfront.observers.IInventoryObserver;
import com.example.barmanagarfront.services.SupplierService;
import com.example.barmanagarfront.views.*;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;


@Route(value = "")
@PageTitle("Main")
public class MainLayout extends AppLayout implements IInventoryObserver
{
    private final SupplierService apiDrinkService;

    public MainLayout(SupplierService apiDrinkService)
    {
        this.apiDrinkService = apiDrinkService;
        DrawerToggle drawerToggle = new DrawerToggle();

        H1 title = new H1(" Bar ");
        title.getStyle()
                .set("font-size", "var(--lumo-font-size-l)")
                .set("margin", "0");
        Tabs tabs = getTabs();
        Button button = new Button("Get");
        button.addClickListener(buttonClickEvent ->
        {
            apiDrinkService.getDrinksCategories();
        });

        addToDrawer(tabs);
        addToNavbar(drawerToggle, title);
        CartOfDrinksManager.getInstance().addObserver(this);

    }

    private Tabs getTabs()
    {
        Tabs tabs = new Tabs();
        tabs.add(createTab(VaadinIcon.HOME, "Home", MainLayout.class));
        tabs.add(createTab(VaadinIcon.LINES_LIST, "Categories", CategorisView.class ));
        tabs.add(createTab(VaadinIcon.CART_O, "Added", CartView.class ));
        tabs.add(createTab(VaadinIcon.GLASS, "Bar Inventory", InventoryView.class ));
        tabs.add(createTab(VaadinIcon.DENTAL_CHAIR, "Bar Seats", SeatView.class ));



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
