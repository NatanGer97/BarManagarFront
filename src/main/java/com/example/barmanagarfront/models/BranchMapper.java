package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class BranchMapper
{
    @JsonProperty("_embedded")
    public Embedded _embedded;

    @Data
    @JsonRootName("Embedded")
    public class Embedded{
        public ArrayList<BranchDto> branchDtoList;
    }
    @Data
    @NoArgsConstructor
    public static class BranchDto
    {
        public String branchName;
        public String branchId;
        public int numOfOrders;
        public double totalOrdersBill;
        public int numOfEmployees;
        public ArrayList<String> employeesIds;
//        public ArrayList<String> orderIds;
    }



}
