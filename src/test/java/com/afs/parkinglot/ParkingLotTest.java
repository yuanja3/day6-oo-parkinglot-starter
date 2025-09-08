package com.afs.parkinglot;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ParkingLotTest {

    @Test
    void should_return_ticket_when_park_car_given_parking_lot_and_car() {
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car();
        ParkingTicket ticket = parkingLot.park(car);
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetch_car_given_parking_lot_with_parked_car_and_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car();
        ParkingTicket ticket = parkingLot.park(car);
        Car fetchedCar = parkingLot.fetch(ticket);
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_throw_exception_when_fetch_car_given_unrecognized_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.fetch(wrongTicket);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_fetch_car_given_used_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car();
        ParkingTicket ticket = parkingLot.park(car);

        // 第一次取车成功
        parkingLot.fetch(ticket);

        // 第二次取车应该抛出异常
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.fetch(ticket);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_park_car_given_full_parking_lot() {
        ParkingLot parkingLot = new ParkingLot(1);
        Car car1 = new Car();
        Car car2 = new Car();

        parkingLot.park(car1);

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.park(car2);
        });

        assertEquals("No available position.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_park_null_car() {
        ParkingLot parkingLot = new ParkingLot();

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.park(null);
        });

        assertEquals("Cannot park null car", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_fetch_with_null_ticket() {
        ParkingLot parkingLot = new ParkingLot();

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.fetch(null);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_correct_car_when_fetch_twice_given_two_parked_cars() {
        ParkingLot parkingLot = new ParkingLot();
        Car car1 = new Car();
        Car car2 = new Car();
        ParkingTicket ticket1 = parkingLot.park(car1);
        ParkingTicket ticket2 = parkingLot.park(car2);

        assertEquals(car1, parkingLot.fetch(ticket1));
        assertEquals(car2, parkingLot.fetch(ticket2));
    }

    @Test
    void should_return_nothing_when_fetch_car_given_no_ticket() {
        ParkingLot parkingLot = new ParkingLot();
        Car car = new Car();
        parkingLot.park(car);

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingLot.fetch(null);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }
}
