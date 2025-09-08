package com.afs.parkinglot;
import java.util.ArrayList;
import java.util.List;

public class StandardParkingBoy {
    private final List<ParkingLot> parkingLots;

    public StandardParkingBoy() {
        this.parkingLots = new ArrayList<>();
    }

    public StandardParkingBoy(ParkingLot parkingLot) {
        this();
        this.parkingLots.add(parkingLot);
    }

    public StandardParkingBoy(List<ParkingLot> parkingLots) {
        this.parkingLots = new ArrayList<>(parkingLots);
    }

    public void manageParkingLot(ParkingLot parkingLot) {
        parkingLots.add(parkingLot);
    }

    public List<ParkingLot> getManagedParkingLots() {
        return new ArrayList<>(parkingLots);
    }

    public ParkingTicket park(Car car) {
        if (parkingLots.isEmpty()) {
            throw new ParkingException("No parking lot managed.");
        }

        for (ParkingLot parkingLot : parkingLots) {
            if (!parkingLot.isFull()) {
                return parkingLot.park(car);
            }
        }

        throw new ParkingException("No available position.");
    }

    public Car fetch(ParkingTicket ticket) {
        if (parkingLots.isEmpty()) {
            throw new ParkingException("No parking lot managed.");
        }

        if (ticket == null) {
            throw new ParkingException("Unrecognized parking ticket.");
        }


        for (ParkingLot parkingLot : parkingLots) {
            try {
                Car car = parkingLot.fetch(ticket);
                if (car != null) {
                    return car;
                }
            } catch (ParkingException e) {
                continue;
            }
        }

        throw new ParkingException("Unrecognized parking ticket.");
    }
}