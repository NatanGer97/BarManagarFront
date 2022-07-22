package com.example.barmanagarfront.views;

import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.Singeltones.SeatsManager;
import com.example.barmanagarfront.enums.eSeatStatus;
import com.example.barmanagarfront.events.ClosCustomerDialogEvent;
import com.example.barmanagarfront.factories.ImageFactory;
import com.example.barmanagarfront.models.BranchMapper.BranchDto;
import com.example.barmanagarfront.models.OrderResponseObject.OrderDto;
import com.example.barmanagarfront.observers.ISeatStatusObserver;
import com.example.barmanagarfront.observers.Seat;
import com.example.barmanagarfront.services.BranchService;
import com.example.barmanagarfront.services.OrderService;
import com.example.barmanagarfront.views.dialogs.OrderBillDialog;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.*;

import java.util.ArrayList;
import java.util.List;

@Route(value = "Seats",layout = BasicLayout.class)
@PageTitle("Bar | Seats ")
public class SeatView extends VerticalLayout implements ISeatStatusObserver
{

    private final OrderService orderService;
    private Grid<Seat> seatGrid;
    private List<Seat> seats;
    private List<OrderDto> orderDtos;
    private final BranchService brunchService;
    private BranchDto selecedBrunchDto;

    public SeatView(OrderService orderService, BranchService brunchService)
    {
        this.brunchService = brunchService;

        System.out.println("inside seat view");
        this.orderService = orderService;
        seats = new ArrayList<>();
        try
        {
            orderDtos = orderService.getOpenOrders();

        }
        catch (Exception exception)
        {
            orderDtos = new ArrayList<>();
        }
        System.out.println("open: " + orderDtos);


        for ( int i = 0; i < 5; i++ )
        {
            Seat seat = new Seat(i + 1);
            seat.addObserver(this);
            seats.add(seat);
        }
        setSizeFull();
        initSeatGrid();
        add(initTitle(),getContent());
//        add(initTitle(),getContent());
    }

    private VerticalLayout initTitle()
    {
        VerticalLayout headerContainer = new VerticalLayout();

        VerticalLayout headerTitle = new VerticalLayout();
        headerTitle.setAlignItems(FlexComponent.Alignment.CENTER);
        headerTitle.addClassNames("mb-0", "mt-xs", "text-xl");
        Image seatsLogoImage = ImageFactory.getInstance().getImage("seatsLogo.png");
//        H3 title = new H1(seatsLogoImage);
        headerTitle.add(seatsLogoImage);
//        headerTitle.add(new H3(new Text("Seats")));
        headerContainer.add(headerTitle);
        return headerContainer;
    }

    private void initSeatGrid()
    {
        this.seatGrid = new Grid<>(Seat.class,false);
        seatGrid.setAllRowsVisible(true);
        seatGrid.addColumn(seat -> seat.getSeatNumber()).setHeader("Seat")
                .setTextAlign(ColumnTextAlign.CENTER);
        seatGrid.addComponentColumn(seat -> createStatusLabel(seat)).setHeader("status")
                .setTextAlign(ColumnTextAlign.CENTER);;
        seatGrid.addComponentColumn(seat -> createButtons(seat)).setHeader("Actions")
                .setTextAlign(ColumnTextAlign.CENTER);
        seatGrid.asSingleSelect().addValueChangeListener(event -> {
            System.out.println(event.getValue());
        });

        updateSeatGrid();
    }



    private Span createStatusLabel(Seat seat)
    {
        Icon icon;
        Span statusSpan;

        if (!seat.isSeatTaken()) {
            icon = createIcon(VaadinIcon.UNLOCK, eSeatStatus.Free);
//            icon.getElement().getThemeList().add("badge success");
            statusSpan = new Span(icon,new Span(eSeatStatus.Free.toString()));
            statusSpan.getElement().getThemeList().add("badge success");



        } else {
            icon = createIcon(VaadinIcon.LOCK, eSeatStatus.Taken);
//            icon.getElement().getThemeList().add("badge error");
            statusSpan = new Span(icon,new Span(eSeatStatus.Taken.toString()));
            statusSpan.getElement().getThemeList().add("badge error");
        }

        return statusSpan;
    }

    private Icon createIcon(VaadinIcon vaadinIcon,eSeatStatus eStatus ) {
        Icon icon = vaadinIcon.create();
        icon.getStyle().set("padding", "var(--lumo-space-xs");
        // Accessible label
        icon.getElement().setAttribute("aria-label", eStatus.toString());
        // Tooltip
        icon.getElement().setAttribute("title", eStatus.toString());
        return icon;
    }

    private Component createButtons(Seat seat)
    {
        Button openButton = new Button("Place",VaadinIcon.GLASS.create());
        openButton.setEnabled(!seat.isSeatTaken());
        Button payButton = new Button("Pay",VaadinIcon.DOLLAR.create());
        payButton.setEnabled(seat.isSeatTaken());

        openButton.addClickListener(buttonClickEvent -> {
            openButton.getUI().ifPresent(ui -> ui.navigate(
                    OrderBuilderForm.class,
                    new RouteParameters(new RouteParam("seat", String.valueOf(seat.getSeatNumber())))

            ));
        });

        payButton.addClickListener(buttonClickEvent ->
        {
            OrderDto orderDto = orderService.getOrderBySeatNumber(seat.getSeatNumber());

            OrderBillDialog orderBillDialog = new OrderBillDialog(orderDto);

            orderBillDialog.addListener(ClosCustomerDialogEvent.class,closCustomerDialogEvent ->
            {
               orderBillDialog.close();
               updateSeatGrid();
            });
            orderBillDialog.open();
        });


        HorizontalLayout horizontalLayout = new HorizontalLayout(openButton, payButton);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }


    private void updateSeatGrid()
    {
        this.seatGrid.setItems(SeatsManager.getInstance().getSeatMap().values());
        System.out.println("Seats:" + SeatsManager.getInstance().getSeatMap().values());
//        this.seatGrid.setItems(this.seats);
    }

    private Component getContent()
    {

        HorizontalLayout content = new HorizontalLayout(seatGrid);
        content.setFlexGrow(2, seatGrid);
        content.setFlexGrow(1, content);
        content.setSizeFull();
        return content;
    }

    @Override
    public void onStatusChange(Seat seat)
    {
        System.out.println(seat.toString());
        updateSeatGrid();
    }
}

