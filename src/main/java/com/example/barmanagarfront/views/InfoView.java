package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.enums.eFilterType;
import com.example.barmanagarfront.models.QueryResult;
import com.example.barmanagarfront.services.InventoryService;
import com.example.barmanagarfront.services.OrderService;
import com.storedobject.chart.*;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.Locale;
import java.util.Random;

@Route(value = "info",layout = MainLayout.class)
public class InfoView extends Main implements  HasComponents , HasStyle {
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private HorizontalLayout buttonsLayout;
    private HorizontalLayout mainContainer;

    public InfoView(InventoryService inventoryService, OrderService orderService) {
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        initializeButtonsLayout();
        initialize();
    }


    private void initializeButtonsLayout(){
        buttonsLayout = new HorizontalLayout();
        Button ingredientStatusButton = new Button("Inventory Status by Ingredient");
        Button categoryStatusButton = new Button("Inventory Status by Category");
        Button profitButton = new Button("Annual Profit");
        Button drinkPopularityButton = new Button("Most popular drinks");

        ingredientStatusButton.addClickListener(buttonClickEvent -> {
            mainContainer.removeAll();
            mainContainer.add(getCountChart("Ingredient"));
        });

        categoryStatusButton.addClickListener(buttonClickEvent -> {
            mainContainer.removeAll();
            mainContainer.add(getCountChart("Category"));
        });

        profitButton.addClickListener(buttonClickEvent -> {
            mainContainer.removeAll();
            mainContainer.add(getProfitChart());
        });

        drinkPopularityButton.addClickListener(buttonClickEvent -> {
            mainContainer.removeAll();
            mainContainer.add(getTenMostPopularDrinksChart());
        });

        buttonsLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.add(ingredientStatusButton, categoryStatusButton, profitButton, drinkPopularityButton);
    }

    private SOChart getProfitChart() {
        SOChart soChart = new SOChart();
        soChart.setSize("900px", "600px");

        DateData monthLabels = new DateData();
        for (int i = 0; i < 12; i++) {
            monthLabels.add(LocalDate.of(2021, i + 1, 1));
        }
        Data currentYearData = new Data();
        //TODO : Let the user pick a year
        orderService.getProfitsByYear(2022).forEach(item -> {
            currentYearData.add(item.getResult());
        });

        Data lastYearData = new Data();
        orderService.getProfitsByYear(2021).forEach(item->{
            lastYearData.add(item.getResult());
        });

        BarChart currentYearBarChart = new BarChart(monthLabels, currentYearData);
        currentYearBarChart.setName("2022");
        currentYearBarChart.setStackName("BC");
        BarChart lastYearBarChart = new BarChart(monthLabels, lastYearData);
        lastYearBarChart.setName("2021");
        lastYearBarChart.setStackName("BC");


        XAxis xAxis = new XAxis(DataType.DATE);
        xAxis.setMinAsMinData();
        xAxis.getLabel(true).setFormatter("{MMM}");

        RectangularCoordinate coordinate =
                new RectangularCoordinate(xAxis, new YAxis(DataType.NUMBER));

        coordinate.add(currentYearBarChart,lastYearBarChart);
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());

        soChart.add(new Title("Annual profit compared to last year"),coordinate, toolbox);
        return soChart;
    }

    private SOChart getTenMostPopularDrinksChart(){
        CategoryData labels = new CategoryData();
        Data data = new Data();
        orderService.getMostPopularDrinks().forEach(item -> {
            labels.add(inventoryService.getDrinkById(item.get_id()).name);
            data.add(item.getResult());
        });

        SOChart soChart = new SOChart();
        soChart.setSize("900px", "600px");

        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        nc.setName("NightingaleChart");
        Position p = new Position();
        p.setTop(Size.percentage(55));
        nc.setPosition(p);

        BarChart bc = new BarChart(labels, data);
        bc.setName("BarChart");

        RectangularCoordinate coordinate =
                new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        p = new Position();
        p.setBottom(Size.percentage(50));
        coordinate.setPosition(p); // Position it leaving 55% space at the bottom
        bc.plotOn(coordinate); // Bar chart needs to be plotted on a coordinate system

        // Just to demonstrate it, we are creating a "Download" and a "Zoom" toolbox button
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());
        soChart.add(nc, bc, toolbox);
        return soChart;
    }

    private SOChart getCountChart(String chartDataType){
        CategoryData labels = new CategoryData();
        Data data = new Data();
        if (chartDataType.equals(eFilterType.Category.name())) {
            inventoryService.getInventoryCountByCategory().forEach(item -> {
                labels.add(item.get_id());
                data.add(item.getResult());
            });
        }
        if (chartDataType.equals(eFilterType.Ingredient.name())){
            List<QueryResult> inventoryCount = inventoryService.getInventoryCountByIngredient();
            int sum = inventoryCount.stream().map(QueryResult::getResult).reduce(0,Integer::sum);
            inventoryCount.forEach(item -> {
                if (item.getResult() > ( sum / inventoryCount.size()) * 2){
                    labels.add(item.get_id());
                    data.add(item.getResult());
                }
            });
        }

        SOChart soChart = new SOChart();
        soChart.setSize("900px", "600px");

        NightingaleRoseChart nc = new NightingaleRoseChart(labels, data);
        nc.setName("NightingaleChart");
        Position p = new Position();
        p.setTop(Size.percentage(55));
        nc.setPosition(p);

        BarChart bc = new BarChart(labels, data);
        bc.setName("BarChart");

        RectangularCoordinate coordinate =
                new RectangularCoordinate(new XAxis(DataType.CATEGORY), new YAxis(DataType.NUMBER));
        p = new Position();
        p.setBottom(Size.percentage(50));
        coordinate.setPosition(p); // Position it leaving 55% space at the bottom
        bc.plotOn(coordinate); // Bar chart needs to be plotted on a coordinate system

        // Just to demonstrate it, we are creating a "Download" and a "Zoom" toolbox button
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());
        soChart.add(nc, bc, toolbox);
        return soChart;
    }
    private void initialize(){
        mainContainer = new HorizontalLayout();
        mainContainer.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        add(buttonsLayout, mainContainer);
    }


}
