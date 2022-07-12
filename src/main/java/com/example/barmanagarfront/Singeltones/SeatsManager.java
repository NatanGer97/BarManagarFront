package com.example.barmanagarfront.Singeltones;

import com.example.barmanagarfront.models.Order;
import com.example.barmanagarfront.models.OrderResponseObject;
import com.example.barmanagarfront.observers.Seat;
import com.example.barmanagarfront.services.OrderService;
import lombok.Data;
import org.springframework.boot.web.client.RestTemplateBuilder;

import java.util.HashMap;
import java.util.Map;

@Data
public final class SeatsManager {
    private static volatile SeatsManager seatsManagerInstance;
    private  Map<Integer, OrderResponseObject.OrderDto> ordersMap;
    private  Map<Integer,Seat> seatMap;
    private  OrderService orderService;


    private SeatsManager()
    {
       RestTemplateBuilder restTemplateBuilder = new RestTemplateBuilder();
       orderService = new OrderService(restTemplateBuilder);
        initMaps();
    }

    private void initMaps()
    {
        ordersMap = new HashMap<>();
        seatMap = new HashMap<>();
        for ( int i = 0; i < 5; i++ )
        {
            ordersMap.put(i+1,null);
            seatMap.put(i+1,new Seat(i+1));
        }

        getOpenOrder();


    }

    private void getOpenOrder()
    {
        try
        {
            for ( OrderResponseObject.OrderDto openOrder : orderService.getOpenOrders() )
            {
                ordersMap.put(openOrder.getSeatNumber(),openOrder);
                Seat seat = new Seat(openOrder.getSeatNumber());
                seat.setSeatTaken(true);
                seatMap.put(openOrder.getSeatNumber(),seat);
            }
        }
        catch (NullPointerException exception)
        {
            exception.getMessage();
        }
    }

    public static SeatsManager getInstance()
    {
        SeatsManager seatsManager = seatsManagerInstance;
        if ( seatsManagerInstance != null ){
            seatsManager.initMaps();
            return seatsManager;
        }

        synchronized(SeatsManager.class)
        {
            if ( seatsManagerInstance == null )
            {
                seatsManagerInstance = new SeatsManager();
            }
            return  seatsManagerInstance;
        }
    }

    public Map<Integer, Seat> getSeatMap()
    {
        getOpenOrder();
        return seatMap;
    }
    public void updateSet(int setNumber)
    {
        Seat seat = seatMap.get(setNumber);
        seat.setSeatTaken(!seat.isSeatTaken());
        seatMap.put(setNumber,seat);
    }

    public OrderResponseObject.OrderDto getOrder(int seatNum)
    {
        return this.ordersMap.get(seatNum);
    }
}
