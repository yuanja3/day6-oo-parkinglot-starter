package com.afs.parkinglot;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;

public class StandardParkingBoyTest {

    private ParkingLot parkingLot;
    private StandardParkingBoy parkingBoy;
    private Car car;

    @BeforeEach
    void setUp() {
        parkingLot = new ParkingLot(2);
        parkingBoy = new StandardParkingBoy(parkingLot);
        car = new Car();
    }

    @Test
    void should_return_ticket_when_park_car_given_parking_boy_and_car() {
        ParkingTicket ticket = parkingBoy.park(car);
        assertNotNull(ticket);
    }

    @Test
    void should_return_car_when_fetch_car_given_parking_boy_with_parked_car_and_ticket() {
        ParkingTicket ticket = parkingBoy.park(car);
        Car fetchedCar = parkingBoy.fetch(ticket);
        assertEquals(car, fetchedCar);
    }

    @Test
    void should_throw_exception_when_fetch_car_given_unrecognized_ticket() {
        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingBoy.fetch(wrongTicket);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_fetch_car_given_used_ticket() {
        ParkingTicket ticket = parkingBoy.park(car);
        parkingBoy.fetch(ticket); // 第一次取车

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingBoy.fetch(ticket); // 第二次取车
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_park_car_given_full_parking_lot() {
        parkingBoy.park(new Car());
        parkingBoy.park(new Car());

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingBoy.park(new Car());
        });

        assertEquals("No available position.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_park_null_car() {
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingBoy.park(null);
        });

        assertEquals("Cannot park null car", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_fetch_with_null_ticket() {
        ParkingException exception = assertThrows(ParkingException.class, () -> {
            parkingBoy.fetch(null);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_return_correct_car_when_fetch_twice_given_two_parked_cars() {
        Car car1 = new Car();
        Car car2 = new Car();
        ParkingTicket ticket1 = parkingBoy.park(car1);
        ParkingTicket ticket2 = parkingBoy.park(car2);

        assertEquals(car1, parkingBoy.fetch(ticket1));
        assertEquals(car2, parkingBoy.fetch(ticket2));
    }

    @Test
    void should_throw_exception_when_no_parking_lot_managed() {
        StandardParkingBoy emptyParkingBoy = new StandardParkingBoy();

        ParkingException parkException = assertThrows(ParkingException.class, () -> {
            emptyParkingBoy.park(new Car());
        });
        assertEquals("No parking lot managed.", parkException.getMessage());

        ParkingException fetchException = assertThrows(ParkingException.class, () -> {
            emptyParkingBoy.fetch(new ParkingTicket());
        });
        assertEquals("No parking lot managed.", fetchException.getMessage());
    }

    @Test
    void should_work_with_multiple_parking_lots() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        Car car1 = new Car();
        Car car2 = new Car();

        ParkingTicket ticket1 = boy.park(car1);
        ParkingTicket ticket2 = boy.park(car2);

        assertEquals(car1, boy.fetch(ticket1));
        assertEquals(car2, boy.fetch(ticket2));
    }
}