package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.reservation.Reservation;

import java.time.LocalDateTime;

public record ReservationStatusResponse(
        Long reservationId,
        Long userId,
        Long lectureId,
        String lectureTitle,
        LocalDateTime openTime) {
    public static ReservationStatusResponse from(Reservation reservation) {
        return new ReservationStatusResponse(
                reservation.id(),
                reservation.userId(),
                reservation.lectureId(),
                reservation.lectureTitle(),
                reservation.openTime()
        );
    }
}
