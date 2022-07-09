package com.example.barmanagarfront.models;

import lombok.Data;

@Data
public class CustomerAsDto
{
    public String fullName;
    public int orderAmount;
    public int idNumber;

    public String showCustomer()
    {
            return String.format("%s (orders: %s)",getFullName(),getOrderAmount());
    }

}