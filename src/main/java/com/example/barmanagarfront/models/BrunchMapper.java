package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BrunchMapper
{
    @JsonProperty("_embedded")
    public Embedded _embedded;

    @Data
    @JsonRootName("Embedded")
    public class Embedded{
        public ArrayList<BrunchDto> brunchDtoList;
    }
    @Data
    @NoArgsConstructor
    public static class BrunchDto
    {
        public String brunchName;
        public String brunchId;
        public int numOfOrders;
        public double totalOrdersBill;
        public int numOfEmployees;
        public ArrayList<String> employeesIds;
        public ArrayList<String> orderIds;
    }



}
