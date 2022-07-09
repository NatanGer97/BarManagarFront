package com.example.barmanagarfront.models;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public  class Customer {
    private String firstName;
    private String lastName;
    private int idNumber;

    public Customer(int idNumber,String firstName, String lastName)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.idNumber = idNumber;
    }
}
