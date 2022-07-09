package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;


@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class CustomerResponseObject
{
    public Embedded _embedded;

    @Data
    public static class Embedded{
        public ArrayList<Customer> customerDtoList;
    }





  /*  public Embedded _embedded;

    @Data
    public static class Embedded{
        public ArrayList<Customer> customerList;
    }
*/



}
