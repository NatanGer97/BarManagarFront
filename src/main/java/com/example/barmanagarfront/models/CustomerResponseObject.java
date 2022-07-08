package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerResponseObject
{
    @JsonProperty("_embedded")
    public CustomerList customersList;

    @Data
    @JsonRootName("Embedded")
    public static class CustomerList
    {
        public ArrayList<Customer> customers;
    }

}
