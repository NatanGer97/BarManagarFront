package com.example.barmanagarfront.models;

import com.example.barmanagarfront.enums.eOrderStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class Order
{
    private ArrayList<BarDrink> orderedDrinks;
    private double bill;
//    private eOrderStatus orderStatus;
    private LocalDate orderDate;
    private Customer customer;
    private int seatNumber;
    private String orderId;

    public Order()
    {
        this.orderedDrinks = new ArrayList<>();
//        this.orderStatus = eOrderStatus.Open;
        this.orderDate = LocalDate.now();
    }

    public Order(Customer customer)
    {
        this();
        this.customer = customer;
    }
}
