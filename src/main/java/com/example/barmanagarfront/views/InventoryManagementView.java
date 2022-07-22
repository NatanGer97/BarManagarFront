package com.example.barmanagarfront.views;

import com.example.barmanagarfront.BasicLayout;
import com.example.barmanagarfront.enums.eFilterType;
import com.example.barmanagarfront.models.ApiDrink;
import com.example.barmanagarfront.services.SupplierService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.router.Route;

import java.util.*;

@Route(value = "manage_inventory",layout = BasicLayout.class)
public class InventoryManagementView extends Main implements HasComponents, HasStyle {

    private final Map<Tab, Component> contentMap = new HashMap<>();
    private final SupplierService supplierService;
    private Tab categoryTab;
    private Tab ingredientTab;
    private Tab alcoholicFilterTab;
    private HorizontalLayout categoryHeaderContainer;
    private HorizontalLayout ingredientHeaderContainer;
    private HorizontalLayout alcoholicFilterHeaderContainer;
    private H3 categoryTitle;
    private H3 ingredientTitle;
    private H3 alcoholicFilterTitle;
    private HorizontalLayout categoryLayout;
    private HorizontalLayout ingredientLayout;
    private HorizontalLayout alcoholicFilterLayout;
    private OrderedList categoryContainer;
    private OrderedList ingredientContainer;
    private OrderedList alcoholicFilterContainer;

    public InventoryManagementView(SupplierService supplierService){
        this.supplierService = supplierService;
        initializeOrderedLists();
        initializeLayouts();
        buildContentMap();
        constructUI();
    }

    private void initializeOrderedLists() {
        categoryContainer = new OrderedList();
        categoryContainer.addClassNames("grid","gap-m", "list-none", "m-0", "p-0");
        categoryContainer.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        ingredientContainer = new OrderedList();
        ingredientContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        ingredientContainer.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

        alcoholicFilterContainer = new OrderedList();
        alcoholicFilterContainer.addClassNames("gap-m", "grid", "list-none", "m-0", "p-0");
        alcoholicFilterContainer.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");

       populateListContainers();

    }

    private void populateListContainers(){
        categoryContainer.removeAll();
        ingredientContainer.removeAll();
        alcoholicFilterContainer.removeAll();
        Objects.requireNonNull(getCards(eFilterType.Category)).forEach(category -> categoryContainer.add(category));
        Objects.requireNonNull(getCards(eFilterType.Ingredient)).forEach(ingredient -> ingredientContainer.add(ingredient));
        Objects.requireNonNull(getCards(eFilterType.AlcoholicFilter)).forEach(filter -> alcoholicFilterContainer.add(filter));
    }

    private List<FormViewCard> getCards(eFilterType type){
        switch (type){
            case Category -> {
                List<String> categories = supplierService.getDrinksCategories();
                List<FormViewCard> categoryCards = categories.stream().map(category->new FormViewCard(category, eFilterType.Category)).toList();

                categoryCards.forEach(card-> card.addClickListener(clickEvent -> {
                    categoryTitle.setText(card.getModifiedName());
                    List<ApiDrink> drinks = supplierService.getDrinksByCategory(card.getModifiedName());
                    categoryContainer.removeAll();
                    drinks.forEach(drink-> categoryContainer.add(new DrinkViewCard(drink)));
                }));

                return categoryCards;
            }
            case Ingredient -> {
                List<String> ingredients = supplierService.getIngredients();
                List<FormViewCard> ingredientCards = ingredients.stream()
                        .map(ingredient->new FormViewCard(ingredient, eFilterType.Ingredient)).toList();

                ingredientCards.forEach(card-> card.addClickListener(clickEvent -> {
                    ingredientTitle.setText(card.getModifiedName());
                    List<ApiDrink> drinks = supplierService.getDrinksByIngredient(card.getModifiedName());
                    ingredientContainer.removeAll();
                    drinks.forEach(drink-> ingredientContainer.add(new DrinkViewCard(drink)));
                }));

                return ingredientCards;
            }
            case AlcoholicFilter -> {
                List<FormViewCard> alcoholicFilterCards = new ArrayList<>();
                alcoholicFilterCards.add(new FormViewCard("Alcoholic", eFilterType.AlcoholicFilter));
                alcoholicFilterCards.add(new FormViewCard("Non alcoholic", eFilterType.AlcoholicFilter));
                alcoholicFilterCards.add(new FormViewCard("Optional alcohol", eFilterType.AlcoholicFilter));

                alcoholicFilterCards.forEach(card -> card.addClickListener(clickEvent -> {
                    alcoholicFilterTitle.setText(card.getModifiedName());
                    List<ApiDrink> drinks = supplierService.getDrinksByAlcoholicFilter(card.getModifiedName());
                    alcoholicFilterContainer.removeAll();
                    drinks.forEach(drink-> alcoholicFilterContainer.add(new DrinkViewCard(drink)));
                }));

                return alcoholicFilterCards;
            }
        }
        return null;
    }

    private void initializeLayouts() {
        this.categoryLayout = new HorizontalLayout();
        this.ingredientLayout = new HorizontalLayout();
        this.alcoholicFilterLayout = new HorizontalLayout();

        ingredientLayout.add(ingredientContainer);
        ingredientLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        categoryLayout.add(categoryContainer);
        categoryLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        alcoholicFilterLayout.add(alcoholicFilterContainer);
        alcoholicFilterLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
    }

    private void initializeCategoryHeaderContainer(){
        //initialize category tab header container
        categoryHeaderContainer = new HorizontalLayout();
        VerticalLayout categoryHeaderTitle = new VerticalLayout();
        categoryHeaderTitle.addClassNames("items-center", "justify-between");
        categoryTitle = new H3(eFilterType.Category.name());
        Button categoryClearButton = new Button("Clear");
        categoryClearButton.addClickListener(buttonClickEvent -> {
            categoryContainer.removeAll();
            categoryTitle.setText(eFilterType.Category.name());
            Objects.requireNonNull(getCards(eFilterType.Category)).forEach(category -> categoryContainer.add(category));
        });
        categoryHeaderTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        categoryHeaderTitle.add(categoryTitle, categoryClearButton);
        categoryHeaderContainer.add(categoryHeaderTitle);
    }

    private void initializeIngredientHeaderContainer(){
        //initialize ingredient tab header container
        ingredientHeaderContainer = new HorizontalLayout();
        VerticalLayout ingredientHeaderTitle = new VerticalLayout();
        ingredientHeaderTitle.addClassNames("items-center", "justify-between");
        ingredientTitle = new H3(eFilterType.Ingredient.name());
        Button ingredientClearButton = new Button("Clear");
        ingredientClearButton.addClickListener(buttonClickEvent -> {
            ingredientContainer.removeAll();
            ingredientTitle.setText(eFilterType.Ingredient.name());
            Objects.requireNonNull(getCards(eFilterType.Ingredient)).forEach(ingredient -> ingredientContainer.add(ingredient));
        });
        ingredientHeaderTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        ingredientHeaderTitle.add(ingredientTitle, ingredientClearButton);
        ingredientHeaderContainer.add(ingredientHeaderTitle);
    }

    private void initializeAlcoholicFilterHeaderContainer(){
        //initialize alcoholic filter tab header container
        alcoholicFilterHeaderContainer = new HorizontalLayout();
        VerticalLayout alcoholicFilterHeaderTitle = new VerticalLayout();
        alcoholicFilterHeaderTitle.addClassNames("items-center", "justify-between");
        alcoholicFilterTitle = new H3(eFilterType.AlcoholicFilter.name());
        Button alcoholicFilterClearButton = new Button("Clear");
        alcoholicFilterClearButton.addClickListener(buttonClickEvent -> {
            alcoholicFilterContainer.removeAll();
            alcoholicFilterTitle.setText(eFilterType.AlcoholicFilter.name());
            Objects.requireNonNull(getCards(eFilterType.AlcoholicFilter)).forEach(alcoholicFilter -> alcoholicFilterContainer.add(alcoholicFilter));
        });
        alcoholicFilterHeaderTitle.addClassNames("mb-0", "mt-xl", "text-3xl");
        alcoholicFilterHeaderTitle.add(alcoholicFilterTitle, alcoholicFilterClearButton);
        alcoholicFilterHeaderContainer.add(alcoholicFilterHeaderTitle);
    }

    private void buildContentMap(){
        categoryTab = new Tab("Category");
        ingredientTab = new Tab("Ingredient");
        alcoholicFilterTab = new Tab("AlcoholicFilter");

        this.contentMap.put(categoryTab,this.categoryLayout);
        this.contentMap.put(ingredientTab,this.ingredientLayout);
        this.contentMap.put(alcoholicFilterTab,this.alcoholicFilterLayout);
    }

    private void replaceHeaderContainer(String previous, String selected){
        eFilterType selectedType = eFilterType.valueOf(selected);
        eFilterType previousType = eFilterType.valueOf(previous);
        switch (previousType){
            case Category -> {
                if (selectedType == eFilterType.Ingredient){
                    replace(categoryHeaderContainer, ingredientHeaderContainer);
                } else {
                    replace(categoryHeaderContainer, alcoholicFilterHeaderContainer);
                }
            }
            case Ingredient -> {
                if (selectedType == eFilterType.Category){
                    replace(ingredientHeaderContainer, categoryHeaderContainer);
                } else {
                    replace(ingredientHeaderContainer, alcoholicFilterHeaderContainer);
                }
            }
            case AlcoholicFilter -> {
                if (selectedType == eFilterType.Category) {
                    replace(alcoholicFilterHeaderContainer, categoryHeaderContainer);
                } else {
                    replace(alcoholicFilterHeaderContainer, ingredientHeaderContainer);
                }

            }
        }
    }

    private void constructUI(){
        HorizontalLayout contentContainer = new HorizontalLayout();
        contentContainer.addClassNames("items-center", "justify-between");

        Tabs tabs = new Tabs();

        tabs.add(categoryTab);
        tabs.add(ingredientTab);
        tabs.add(alcoholicFilterTab);
        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            contentContainer.removeAll();
            contentContainer.add(contentMap.get(selectedChangeEvent.getSelectedTab()));
            replaceHeaderContainer(selectedChangeEvent.getPreviousTab().getLabel(),
                    selectedChangeEvent.getSelectedTab().getLabel());
        });

        //default container
        initializeCategoryHeaderContainer();
        initializeIngredientHeaderContainer();
        initializeAlcoholicFilterHeaderContainer();
        contentContainer.add(categoryLayout);

        add(tabs, categoryHeaderContainer, contentContainer);
    }



}
