package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class EmployeeMapper
{
    public Embedded _embedded;

    @Data
    public static class Embedded{
        public ArrayList<EmployeeDto> employeeDtoList;
    }

    @Data
    public static class EmployeeDto
    {
        public String fullName;
        public String id;
        public double salaryPerHour;

    }


}
