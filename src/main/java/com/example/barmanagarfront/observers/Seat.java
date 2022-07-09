package com.example.barmanagarfront.observers;

import java.util.ArrayList;
import java.util.List;

public class Seat
{
    private static int seatCounter = 0;
    List<ISeatStatusObserver> observers;
    private int seatNumber;

    private boolean isSeatTaken;

    public Seat(int num)
    {
        this.seatNumber = num;
//        this.seatNumber = getSeatCounter();
        this.isSeatTaken = false;
        this.observers = new ArrayList<>();
    }

    public void addObserver(ISeatStatusObserver observer)
    {
        observers.add(observer);
    }

    public void removeObserver(ISeatStatusObserver observer)
    {
        observers.remove(observer);
    }

    public static int getSeatCounter()
    {
        return ++seatCounter;
    }

    public int getSeatNumber()
    {
        return seatNumber;
    }

    public void setSeatNumber(int seatNumber)
    {
        this.seatNumber = seatNumber;
    }

    public boolean isSeatTaken()
    {
        return isSeatTaken;
    }

    public void setSeatTaken(boolean seatTaken)
    {
        isSeatTaken = seatTaken;
        onStatusChanges();
    }

    private void onStatusChanges()
    {
        if ( observers != null )
            notifyObservers();
    }

    private void notifyObservers()
    {
        observers.forEach(observer -> observer.onStatusChange(this));
    }

    @Override
    public String toString()
    {
        return "Seat{" +
                "seatNumber=" + seatNumber +
                ", isSeatTaken=" + isSeatTaken +
                '}';
    }
}
