package com.afs.parkinglot;
import java.util.List;

public class SuperParkingBoy extends StandardParkingBoy {

    public SuperParkingBoy() {
        super();
    }

    public SuperParkingBoy(ParkingLot parkingLot) {
        super(parkingLot);
    }

    public SuperParkingBoy(List<ParkingLot> parkingLots) {
        super(parkingLots);
    }

    @Override
    public ParkingTicket park(Car car) {
        List<ParkingLot> managedLots = getManagedParkingLots();
        if (managedLots.isEmpty()) {
            throw new ParkingException("No parking lot managed.");
        }

        ParkingLot bestLot = selectBestParkingLotByVacancyRate(managedLots);
        if (bestLot != null) {
            return bestLot.park(car);
        }

        throw new ParkingException("No available position.");
    }

    private ParkingLot selectBestParkingLotByVacancyRate(List<ParkingLot> parkingLots) {
        ParkingLot bestLot = null;
        double maxVacancyRate = -1.0;

        for (ParkingLot parkingLot : parkingLots) {
            if (!parkingLot.isFull()) {
                double vacancyRate = calculateVacancyRate(parkingLot);

                if (vacancyRate > maxVacancyRate) {
                    maxVacancyRate = vacancyRate;
                    bestLot = parkingLot;
                }
                // 如果空置率相同，保持当前选择（相当于按顺序选择第一个最优的）
            }
        }

        return bestLot;
    }

    private double calculateVacancyRate(ParkingLot parkingLot) {
        int capacity = parkingLot.getCapacity();
        if (capacity == 0) {
            return 0.0; // 避免除零错误
        }
        int available = parkingLot.getAvailableCount();
        return (double) available / capacity;
    }
}