package com.example.barmanagarfront.models;

import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class Employee
{
    private String firstName;
    private String lastName;
    private double salaryPerHour;
    private  int idNumber;

    public Employee(String firstName, String lastName, double salaryPerHour, int idNumber)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.salaryPerHour = salaryPerHour;
        this.idNumber = idNumber;
    }
}
