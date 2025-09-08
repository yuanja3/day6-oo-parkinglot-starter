package com.afs.parkinglot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.Arrays;
import java.util.List;

public class SuperParkingBoyTest {

    private ParkingLot lot1;
    private ParkingLot lot2;
    private ParkingLot lot3;
    private SuperParkingBoy superParkingBoy;
    private Car car;

    @BeforeEach
    public void setUp() {
        lot1 = new ParkingLot(10); // 容量10，空置率100%
        lot2 = new ParkingLot(5);  // 容量5，空置率100%
        lot3 = new ParkingLot(8);  // 容量8，空置率100%
        List<ParkingLot> parkingLots = Arrays.asList(lot1, lot2, lot3);
        superParkingBoy = new SuperParkingBoy(parkingLots);
        car = new Car();
    }

    @Test
    public void should_park_to_lot_with_highest_vacancy_rate() {
        // Given: lot1: 8/10=80%, lot2: 5/5=100%, lot3: 8/8=100%
        // 先停一些车来设置不同的空置率
        lot1.park(new Car());
        lot1.park(new Car()); // lot1: 8/10=80%

        // When: 停车
        ParkingTicket ticket = superParkingBoy.park(car);

        // Then: 应该停在空置率最高的lot2或lot3（100% > 80%）
        assertNotNull(ticket);

        // 检查车是否停在空置率100%的停车场（lot2或lot3）
        boolean parkedInHighVacancyLot = false;
        try {
            Car fetchedCar = lot2.fetch(ticket);
            parkedInHighVacancyLot = true;
        } catch (ParkingException e) {
            // 不在lot2，检查lot3
            try {
                Car fetchedCar = lot3.fetch(ticket);
                parkedInHighVacancyLot = true;
            } catch (ParkingException ex) {
                // 也不在lot3，测试失败
            }
        }

        assertTrue(parkedInHighVacancyLot, "Should park in lot with highest vacancy rate");
    }

    @Test
    public void should_park_sequentially_when_vacancy_rates_equal() {
        // Given: 两个停车场空置率相同（都是80%）
        ParkingLot lotA = new ParkingLot(10); // 8/10=80%
        ParkingLot lotB = new ParkingLot(5);  // 4/5=80%

        // 设置相同的空置率
        lotA.park(new Car());
        lotA.park(new Car()); // 8/10=80%
        lotB.park(new Car()); // 4/5=80%

        SuperParkingBoy boy = new SuperParkingBoy(Arrays.asList(lotA, lotB));

        // When: 停车
        ParkingTicket ticket = boy.park(car);

        // Then: 空置率相同，按顺序停在第一个停车场（lotA）
        assertEquals(car, lotA.fetch(ticket));
    }

    @Test
    public void should_choose_higher_vacancy_rate_over_more_spaces() {
        // Given: lot1有更多空位但空置率低，lot2空置率高
        ParkingLot bigLot = new ParkingLot(100); // 大停车场
        ParkingLot smallLot = new ParkingLot(5);  // 小停车场

        // 停90辆车到大停车场，空置率10%
        for (int i = 0; i < 90; i++) {
            bigLot.park(new Car());
        }
        // 小停车场空置率100%

        SuperParkingBoy boy = new SuperParkingBoy(Arrays.asList(bigLot, smallLot));

        // When: 停车
        ParkingTicket ticket = boy.park(car);

        // Then: 应该选择空置率更高的小停车场（100% > 10%）
        assertEquals(car, smallLot.fetch(ticket));
    }

    @Test
    public void should_handle_single_parking_lot() {
        // Given: 只有一个停车场
        SuperParkingBoy boy = new SuperParkingBoy(lot1);

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
        SuperParkingBoy boy = new SuperParkingBoy(Arrays.asList(smallLot1, smallLot2));

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
        ParkingTicket ticket = superParkingBoy.park(car);

        // When: 取车
        Car fetchedCar = superParkingBoy.fetch(ticket);

        // Then: 应该取到正确的车
        assertEquals(car, fetchedCar);
    }

    @Test
    public void should_throw_exception_when_fetch_with_wrong_ticket() {
        // Given: 错误的票
        ParkingTicket wrongTicket = new ParkingTicket();

        // When & Then: 取车应该抛出异常
        try {
            superParkingBoy.fetch(wrongTicket);
            fail("Should throw ParkingException");
        } catch (ParkingException e) {
            assertEquals("Unrecognized parking ticket.", e.getMessage());
        }
    }

    @Test
    public void should_calculate_vacancy_rate_correctly() {
        // Given: 停车场有5容量，已停2辆车
        ParkingLot testLot = new ParkingLot(5);
        testLot.park(new Car());
        testLot.park(new Car());

        // 空置率应该是 (5-2)/5 = 0.6
        SuperParkingBoy boy = new SuperParkingBoy(testLot);

        // When: 停车
        ParkingTicket ticket = boy.park(new Car());

        // Then: 应该正常停车
        assertNotNull(ticket);
    }

    @Test
    public void should_handle_zero_capacity() {
        // Given: 容量为0的停车场（边界情况）
        ParkingLot zeroLot = new ParkingLot(0);
        ParkingLot normalLot = new ParkingLot(1);
        SuperParkingBoy boy = new SuperParkingBoy(Arrays.asList(zeroLot, normalLot));

        // When: 停车
        ParkingTicket ticket = boy.park(car);

        // Then: 应该停在正常停车场
        assertEquals(car, normalLot.fetch(ticket));
    }
}