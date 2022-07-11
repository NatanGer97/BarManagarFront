package com.example.barmanagarfront.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class OrderResponseObject
{
    @JsonProperty("_embedded")
    public Embedded _embedded;

    @Data
    @JsonRootName("Embedded")
    public static class Embedded{
        public ArrayList<OrderDto> orderDtoList;
    }

    @Data
    @NoArgsConstructor
    public static class OrderDto{
        public double orderBill;
        public String orderId;
        public int seatNumber;
        public String orderStatus;
        public String orderName;
        public ArrayList<BarDrink> orderedItems;

    }

}
