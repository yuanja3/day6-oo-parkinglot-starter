package com.afs.parkinglot;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class SmartParkingBoyTest {

    private ParkingLot lot1;
    private ParkingLot lot2;
    private ParkingLot lot3;
    private SmartParkingBoy smartParkingBoy;
    private Car car;

    @BeforeEach
    public void setUp() {
        lot1 = new ParkingLot(5); // 容量5
        lot2 = new ParkingLot(3); // 容量3
        lot3 = new ParkingLot(4); // 容量4
        List<ParkingLot> parkingLots = Arrays.asList(lot1, lot2, lot3);
        smartParkingBoy = new SmartParkingBoy(parkingLots);
        car = new Car();
    }

    @Test
    public void should_park_to_lot_with_most_available_spaces() {
        // Given: 三个停车场，空位数分别为5,3,4
        // When: 停车
        ParkingTicket ticket = smartParkingBoy.park(car);

        // Then: 应该停在空位最多的lot1
        assertNotNull(ticket);
        assertEquals(4, lot1.getAvailableCount()); // 5-1=4
        assertEquals(3, lot2.getAvailableCount());
        assertEquals(4, lot3.getAvailableCount());
        assertEquals(car, lot1.fetch(ticket));
    }

    @Test
    public void should_park_sequentially_when_available_spaces_equal() {
        // Given: 三个停车场空位数相同
        ParkingLot smallLot1 = new ParkingLot(2); // 2空位
        ParkingLot smallLot2 = new ParkingLot(2); // 2空位
        ParkingLot smallLot3 = new ParkingLot(2); // 2空位
        SmartParkingBoy boy = new SmartParkingBoy(Arrays.asList(smallLot1, smallLot2, smallLot3));

        // When: 停车
        ParkingTicket ticket = boy.park(car);

        // Then: 空位数相同，按顺序停在第一个停车场
        assertEquals(car, smallLot1.fetch(ticket));
    }



    @Test
    public void should_handle_single_parking_lot() {
        // Given: 只有一个停车场
        SmartParkingBoy boy = new SmartParkingBoy(lot1);

        // When: 停车
        ParkingTicket ticket = boy.park(car);

        // Then: 正常停在唯一的停车场
        assertNotNull(ticket);
        assertEquals(car, lot1.fetch(ticket));
    }

    @Test
    public void should_throw_exception_when_all_lots_full() {
        // Given: 所有停车场都满
        ParkingLot smallLot1 = new ParkingLot(1);
        ParkingLot smallLot2 = new ParkingLot(1);
        SmartParkingBoy boy = new SmartParkingBoy(Arrays.asList(smallLot1, smallLot2));

        // 停满所有停车场
        boy.park(new Car());
        boy.park(new Car());

        // When & Then: 再次停车应该抛出异常
        try {
            boy.park(new Car());
            fail("Should throw ParkingException");
        } catch (ParkingException e) {
            assertEquals("No available position.", e.getMessage());
        }
    }

    @Test
    public void should_fetch_car_correctly() {
        // Given: 停一辆车
        ParkingTicket ticket = smartParkingBoy.park(car);

        // When: 取车
        Car fetchedCar = smartParkingBoy.fetch(ticket);

        // Then: 应该取到正确的车
        assertEquals(car, fetchedCar);
    }

    @Test
    public void should_throw_exception_when_fetch_with_wrong_ticket() {
        // Given: 错误的票
        ParkingTicket wrongTicket = new ParkingTicket();

        // When & Then: 取车应该抛出异常
        try {
            smartParkingBoy.fetch(wrongTicket);
            fail("Should throw ParkingException");
        } catch (ParkingException e) {
            assertEquals("Unrecognized parking ticket.", e.getMessage());
        }
    }

    @Test
    public void should_throw_exception_when_fetch_with_null_ticket() {
        // When & Then: 用null票取车应该抛出异常
        try {
            smartParkingBoy.fetch(null);
            fail("Should throw ParkingException");
        } catch (ParkingException e) {
            assertEquals("Unrecognized parking ticket.", e.getMessage());
        }
    }

    @Test
    public void should_throw_exception_when_fetch_with_used_ticket() {
        // Given: 停一辆车并取车
        ParkingTicket ticket = smartParkingBoy.park(car);
        smartParkingBoy.fetch(ticket); // 第一次取车

        // When & Then: 用已使用的票再次取车应该抛出异常
        try {
            smartParkingBoy.fetch(ticket);
            fail("Should throw ParkingException");
        } catch (ParkingException e) {
            assertEquals("Unrecognized parking ticket.", e.getMessage());
        }
    }

    @Test
    public void should_handle_partially_filled_lots() {
        // Given: lot1已停2辆车，空位3；lot2空位3；lot3空位4
        lot1.park(new Car());
        lot1.park(new Car()); // lot1现在有3空位

        // When: 停车
        ParkingTicket ticket = smartParkingBoy.park(car);

        // Then: 应该停在空位最多的lot3（4空位）
        assertEquals(car, lot3.fetch(ticket));
    }
}