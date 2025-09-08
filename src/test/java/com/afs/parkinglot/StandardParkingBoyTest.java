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

    @Test
    void should_park_to_first_lot_when_both_have_available_position() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        Car car = new Car();
        ParkingTicket ticket = boy.park(car);

        // 应该停在第一个停车场
        assertTrue(lot1.isFull());
        assertFalse(lot2.isFull());
        assertEquals(car, lot1.fetch(ticket));
    }

    @Test
    void should_park_to_second_lot_when_first_is_full() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        // 停满第一个停车场
        Car car1 = new Car();
        ParkingTicket ticket1 = boy.park(car1);
        assertTrue(lot1.isFull());

        // 第二个车应该停在第二个停车场
        Car car2 = new Car();
        ParkingTicket ticket2 = boy.park(car2);
        assertTrue(lot2.isFull());

        assertEquals(car1, boy.fetch(ticket1));
        assertEquals(car2, boy.fetch(ticket2));
    }

    @Test
    void should_return_right_car_when_fetch_from_multiple_lots() {
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

    @Test
    void should_throw_exception_when_fetch_with_unrecognized_ticket_from_multiple_lots() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        ParkingTicket wrongTicket = new ParkingTicket();

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            boy.fetch(wrongTicket);
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_fetch_with_used_ticket_from_multiple_lots() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        ParkingTicket ticket = boy.park(new Car());
        boy.fetch(ticket); // 第一次取车

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            boy.fetch(ticket); // 第二次取车
        });

        assertEquals("Unrecognized parking ticket.", exception.getMessage());
    }

    @Test
    void should_throw_exception_when_all_lots_are_full() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        // 停满两个停车场
        boy.park(new Car());
        boy.park(new Car());

        ParkingException exception = assertThrows(ParkingException.class, () -> {
            boy.park(new Car());
        });

        assertEquals("No available position.", exception.getMessage());
    }

    @Test
    void should_work_with_three_parking_lots() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        ParkingLot lot3 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2, lot3));

        // 停三辆车
        Car car1 = new Car();
        Car car2 = new Car();
        Car car3 = new Car();

        ParkingTicket ticket1 = boy.park(car1);
        ParkingTicket ticket2 = boy.park(car2);
        ParkingTicket ticket3 = boy.park(car3);

        assertTrue(lot1.isFull());
        assertTrue(lot2.isFull());
        assertTrue(lot3.isFull());

        assertEquals(car1, boy.fetch(ticket1));
        assertEquals(car2, boy.fetch(ticket2));
        assertEquals(car3, boy.fetch(ticket3));
    }

    @Test
    void should_park_sequentially_after_fetching_car() {
        ParkingLot lot1 = new ParkingLot(1);
        ParkingLot lot2 = new ParkingLot(1);
        StandardParkingBoy boy = new StandardParkingBoy(Arrays.asList(lot1, lot2));

        // 停满两个停车场
        Car car1 = new Car();
        Car car2 = new Car();
        ParkingTicket ticket1 = boy.park(car1);
        ParkingTicket ticket2 = boy.park(car2);

        // 从第一个停车场取车
        boy.fetch(ticket1);

        // 新车应该停在第一个停车场（按顺序）
        Car car3 = new Car();
        ParkingTicket ticket3 = boy.park(car3);

        assertEquals(car3, lot1.fetch(ticket3));
    }
}