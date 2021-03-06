package com.example.barmanagarfront.views;

import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.factories.ImageFactory;
import com.example.barmanagarfront.models.OrderResponseObject;
import com.example.barmanagarfront.services.OrderService;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@Route(value = "Orders",layout = BasicLayout.class)
@PageTitle("Bar | Orders ")
public class OrdersView extends VerticalLayout
{
    private final OrderService orderService;
    private Grid<OrderResponseObject.OrderDto> orderDtoGrid;
    private List<OrderResponseObject.OrderDto> orderDtos;


    public OrdersView(OrderService orderService)
    {
        this.orderService = orderService;
        orderDtoGrid = new Grid<>(OrderResponseObject.OrderDto.class,false);
        this.orderDtos = orderService.getDtoOrders();
        initGrid();

        add(createHeader(),orderDtoGrid);

    }

    private void initGrid()
    {
        orderDtoGrid.setAllRowsVisible(true);
        orderDtoGrid.addColumn(orderDto -> orderDtos.indexOf(orderDto) + 1).setHeader("Index")
                        .setTextAlign(ColumnTextAlign.CENTER);
        orderDtoGrid.addColumn(orderDto -> orderDto.getOrderName()).setHeader("Order")
                .setTextAlign(ColumnTextAlign.CENTER);
        orderDtoGrid.addComponentColumn(orderDto -> createStatusLabel(orderDto.getOrderStatus()))
                .setHeader("Status").setTextAlign(ColumnTextAlign.CENTER);
        orderDtoGrid.addColumn(orderDto -> orderDto.getOrderDate()).setHeader("Date")
                        .setTextAlign(ColumnTextAlign.CENTER);
        orderDtoGrid.addColumn(orderDto -> String.format("%.2f",orderDto.getOrderBill()))
                .setHeader("Bill").setSortable(true).setTextAlign(ColumnTextAlign.CENTER).setFooter(calcSum());




        updateSeatGrid();
    }

    private VerticalLayout createHeader()
    {
        VerticalLayout layout = new VerticalLayout();
        layout.setAlignItems(Alignment.CENTER);
        layout.setJustifyContentMode(JustifyContentMode.CENTER);
        Image image = ImageFactory.getInstance().getImage("ordersLogo.png");
        layout.add(image);

        return layout;
    }

    private String calcSum()
    {
        double sum = 0.0;
        for ( OrderResponseObject.OrderDto orderDto : orderDtos )
        {
            sum += orderDto.getOrderBill();
        }
        return String.format("Total: %.2f",sum);
    }

    private void updateSeatGrid()
    {
        orderDtoGrid.setItems(orderService.getDtoOrders());
    }

    private Span createStatusLabel(String status)
    {
        Icon icon;
        Span statusSpan = null;
        if ( status.equals("Open") )
        {
            icon = createIcon(VaadinIcon.UNLOCK);
            statusSpan = new Span(icon);
            statusSpan.getElement().getThemeList().add("badge success");
        }
        else
        {
            icon = createIcon(VaadinIcon.LOCK);
            statusSpan = new Span(icon);
            statusSpan.getElement().getThemeList().add("badge error");
        }

        return statusSpan;
    }

    private Icon createIcon(VaadinIcon vaadinIcon ) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        return icon;
    }
}
