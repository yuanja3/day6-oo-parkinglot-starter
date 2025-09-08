package com.afs.parkinglot;

import java.util.HashMap;
import java.util.Map;

public class ParkingLot {
    private final int capacity;
    private final Map<ParkingTicket, Car> parkedCars;
    private int currentCount;

    public ParkingLot() {
        this(10);
    }

    public ParkingLot(int capacity) {
        this.capacity = capacity;
        this.parkedCars = new HashMap<>();
        this.currentCount = 0;
    }

    public ParkingTicket park(Car car) {
        if (car == null) {
            throw new ParkingException("Cannot park null car");
        }
        if (isFull()) {
            throw new ParkingException("No available position.");
        }

        ParkingTicket ticket = new ParkingTicket();
        parkedCars.put(ticket, car);
        currentCount++;
        return ticket;
    }

    public Car fetch(ParkingTicket ticket) {
        if (ticket == null) {
            throw new ParkingException("Unrecognized parking ticket.");
        }
        if (!parkedCars.containsKey(ticket)) {
            throw new ParkingException("Unrecognized parking ticket.");
        }

        Car car = parkedCars.remove(ticket);
        currentCount--;
        return car;
    }

    public boolean isFull() {
        return currentCount >= capacity;
    }
    public int getCapacity() {
        return capacity;
    }

    public int getAvailableCount() {
        return capacity - currentCount;
    }
}
