package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.reservation.Reservation;

import java.time.LocalDateTime;

public record ReservationResponse(
        Long reservationId,
        Long userId,
        Long lectureId,
        String lectureTitle,
        LocalDateTime openTime) {
    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.id(),
                reservation.userId(),
                reservation.lectureId(),
                reservation.lectureTitle(),
                reservation.openTime()
        );
    }
}
