package com.example.barmanagarfront.views;

import com.example.barmanagarfront.MainLayout;
import com.example.barmanagarfront.enums.eFilterType;
import com.example.barmanagarfront.models.ApiDrink;
import com.example.barmanagarfront.services.SupplierService;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.dataview.GridListDataView;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.component.tabs.TabsVariant;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.*;
import java.util.stream.Collectors;

@Route(value = "manage_inventory",layout = MainLayout.class)
public class InventoryManagementView extends Main implements HasComponents, HasStyle {

    private final Map<Tab, Component> contentMap = new HashMap<>();
    private final SupplierService supplierService;
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
                    List<ApiDrink> drinks = supplierService.getDrinksByCategory(card.getModifiedCategoryName());
                    categoryContainer.removeAll();
                    drinks.forEach(drink-> categoryContainer.add(new DrinkViewCard(drink)));
                }));

                return categoryCards;
            }
            case Ingredient -> {
                List<String> ingredients = supplierService.getIngredients();
                List<FormViewCard> ingredientCards = ingredients.stream().map(ingredient->new FormViewCard(ingredient, eFilterType.Ingredient)).toList();

                ingredientCards.forEach(card-> card.addClickListener(clickEvent -> {
                    List<ApiDrink> drinks = supplierService.getDrinksByIngredient(card.getModifiedCategoryName());
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
                    List<ApiDrink> drinks = supplierService.getDrinksByAlcoholicFilter(card.getModifiedCategoryName());
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

        ingredientLayout.add(this.ingredientContainer);
        ingredientLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        categoryLayout.add(this.categoryContainer);
        categoryLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
        alcoholicFilterLayout.add(this.alcoholicFilterContainer);
        alcoholicFilterLayout.addClassNames("person-form-view", "max-w-screen-lg", "mx-auto", "pb-l", "px-l");
    }


    private void buildContentMap(){

        Tab categoryTab = new Tab("Category");
        Tab ingredientTab = new Tab("Ingredient");
        Tab alcoholicFilterTab = new Tab("Alcoholic Filter");

        this.contentMap.put(categoryTab,this.categoryLayout);
        this.contentMap.put(ingredientTab,this.ingredientLayout);
        this.contentMap.put(alcoholicFilterTab,this.alcoholicFilterLayout);
    }


    private void constructUI(){
        HorizontalLayout contentContainer = new HorizontalLayout();
        contentContainer.addClassNames("items-center", "justify-between");

        Tabs tabs = new Tabs(this.contentMap.keySet().toArray(new Tab[]{}));

        tabs.addThemeVariants(TabsVariant.LUMO_CENTERED);
        tabs.addThemeVariants(TabsVariant.LUMO_EQUAL_WIDTH_TABS);

        tabs.addSelectedChangeListener(selectedChangeEvent -> {
            contentContainer.removeAll();
            contentContainer.add(this.contentMap.get(selectedChangeEvent.getSelectedTab()));
            //initializeOrderedLists();

        });

        //default container
        contentContainer.add(this.contentMap.get(tabs.getSelectedTab()));
        add(tabs, contentContainer);
    }



}
