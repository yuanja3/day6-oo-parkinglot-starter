package com.afs.parkinglot;
import java.util.ArrayList;
import java.util.List;

public class SmartParkingBoy extends StandardParkingBoy {

    public SmartParkingBoy() {
        super();
    }

    public SmartParkingBoy(ParkingLot parkingLot) {
        super(parkingLot);
    }

    public SmartParkingBoy(List<ParkingLot> parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        List<ParkingLot> managedLots = getManagedParkingLots();
        if (managedLots.isEmpty()) {
            throw new ParkingException("No parking lot managed.");
        }

        ParkingLot bestLot = selectBestParkingLot(managedLots);
        if (bestLot != null) {
            return bestLot.park(car);
        }

        throw new ParkingException("No available position.");
    }

    private ParkingLot selectBestParkingLot(List<ParkingLot> parkingLots) {
        ParkingLot bestLot = null;
        int maxAvailableSpaces = -1;

        for (ParkingLot parkingLot : parkingLots) {
            if (!parkingLot.isFull()) {
                int availableSpaces = parkingLot.getAvailableCount();
                if (availableSpaces > maxAvailableSpaces) {
                    maxAvailableSpaces = availableSpaces;
                    bestLot = parkingLot;
                }
                // 如果空位数相同，保持当前选择（相当于按顺序选择第一个最优的）
            }
        }

        return bestLot;
    }
}