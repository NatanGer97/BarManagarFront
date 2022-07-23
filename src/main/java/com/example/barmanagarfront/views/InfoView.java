package com.example.barmanagarfront.views;

import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.enums.eFilterType;
import com.example.barmanagarfront.models.QueryResult;
import com.example.barmanagarfront.services.InventoryService;
import com.example.barmanagarfront.services.OrderService;
import com.storedobject.chart.*;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.html.Main;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.Route;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Route(value = "info",layout = BasicLayout.class)
public class InfoView extends Main implements  HasComponents , HasStyle {
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private HorizontalLayout buttonsLayout;
    private HorizontalLayout mainContainer;

    private ComboBox<Integer> startYearComboBox;
    private ComboBox<Integer> endYearComboBox;


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
            mainContainer.add(createYearPickerToolBar());
//            mainContainer.add(getProfitChart());
        });

        drinkPopularityButton.addClickListener(buttonClickEvent -> {
            mainContainer.removeAll();
            mainContainer.add(getTenMostPopularDrinksChart());
        });

        buttonsLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        buttonsLayout.setAlignItems(FlexComponent.Alignment.CENTER);
        buttonsLayout.add(ingredientStatusButton, categoryStatusButton, profitButton, drinkPopularityButton);
    }

    private List<Integer> creatYearRange(int start, int end)
    {
        List<Integer> optionalYears = IntStream.range(start, end)
                .boxed().collect(Collectors.toList());

        return optionalYears;
    }

    private HorizontalLayout createYearPickerToolBar()
    {
        LocalDate currentDate = LocalDate.now();
        List<Integer> optionalYears = creatYearRange(currentDate.getYear() - 100, currentDate.getYear());

        startYearComboBox = new ComboBox<>("", optionalYears);
        startYearComboBox.setWidth(6, Unit.EM);

        endYearComboBox = new ComboBox<>("");
        endYearComboBox.setWidth(6, Unit.EM);
        endYearComboBox.setEnabled(false);

        startYearComboBox.addValueChangeListener(event -> {
            endYearComboBox.setEnabled(true);
            endYearComboBox.setItems(creatYearRange(event.getValue() + 1, currentDate.getYear() +1));
        });

        endYearComboBox.addValueChangeListener(event -> {
            SOChart profitChart = getProfitChart(startYearComboBox.getValue(), endYearComboBox.getValue());
            mainContainer.add(profitChart);
            startYearComboBox.setEnabled(false);
            endYearComboBox.setEnabled(false);
        });

        Button resetButton = new Button(VaadinIcon.REFRESH.create());
        resetButton.addClickListener(buttonClickEvent -> resetYearsSelectors());
        resetButton.setText("clear");
        resetButton.setIconAfterText(true);


        HorizontalLayout yearPickerLayout = new HorizontalLayout(startYearComboBox, endYearComboBox,resetButton);

        return yearPickerLayout;
    }
    private void resetYearsSelectors()
    {
        mainContainer.removeAll();
        mainContainer.add(createYearPickerToolBar());

    }

    private SOChart getProfitChart(int startYear, int endYear) {
        SOChart soChart = new SOChart();
        soChart.setSize("900px", "600px");

        DateData monthLabels = new DateData();
        for (int i = 0; i < 12; i++) {
            monthLabels.add(LocalDate.of(2021, i + 1, 1));
        }
        Data currentYearData = new Data();
        //TODO : Let the user pick a year
        orderService.getProfitsByYear(endYear).forEach(item -> {
            currentYearData.add(item.getResult());
        });

        Data lastYearData = new Data();
        orderService.getProfitsByYear(startYear).forEach(item->{
            lastYearData.add(item.getResult());
        });

        XAxis xAxis = new XAxis(monthLabels);
        xAxis.setMinAsMinData();
        xAxis.getLabel(true).setFormatter("{MMM}");
        YAxis currentYearYAxis = new YAxis(currentYearData);
        YAxis lastYearYAxis = new YAxis(lastYearData);
        BarChart currentYearBarChart = new BarChart(monthLabels, currentYearData);
        currentYearBarChart.setName(String.valueOf(endYear));
        BarChart lastYearBarChart = new BarChart(monthLabels, lastYearData);
        lastYearBarChart.setName(String.valueOf(startYear));
        lastYearBarChart.setBarGap(0);


        RectangularCoordinate coordinate =
                new RectangularCoordinate(xAxis, new YAxis(DataType.NUMBER));
        currentYearBarChart.plotOn(coordinate, xAxis, currentYearYAxis);
        lastYearBarChart.plotOn(coordinate, xAxis, lastYearYAxis);
        coordinate.add(currentYearBarChart,lastYearBarChart);
        Toolbox toolbox = new Toolbox();
        toolbox.addButton(new Toolbox.Download());

        soChart.add(new Title(String.format("Annual profit of  %s compared to %s",startYear,endYear)),
                coordinate, toolbox);
//        soChart.add(new Title("Annual profit compared to last year"),coordinate, toolbox);

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
