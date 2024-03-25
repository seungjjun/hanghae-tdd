package com.hanghae.reservation.util;

import java.time.LocalDateTime;

public class ReservationTimeChecker {
    private static final LocalDateTime START_RESERVATION_TIME = LocalDateTime.of(2024, 4, 20, 13, 0, 0);

    public static void checkStartTime(LocalDateTime dateTime) {
        if (dateTime.isBefore(START_RESERVATION_TIME)) {
            throw new RuntimeException("아직 특강 신청을 하실 수 없습니다.");
        }
    }
}
