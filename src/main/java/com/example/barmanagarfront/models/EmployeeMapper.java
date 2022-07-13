package com.example.barmanagarfront.models;

import java.util.ArrayList;

public class EmployeeMapper
{
    public Embedded _embedded;

    public static class Embedded{
        public ArrayList<EmployeeDto> employeeDtoList;
    }

    public static class EmployeeDto
    {
        public String fullName;
        public double salaryPerHour;

    }


}
