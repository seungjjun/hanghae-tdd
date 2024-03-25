package com.hanghae.reservation.dto.response;

import com.hanghae.reservation.domain.Reservation;

public record ReservationStatusResponse(
        Long reservationId,
        Long userId,
        Long lectureId) {
    public static ReservationStatusResponse from(Reservation reservation) {
        return new ReservationStatusResponse(
                reservation.id(),
                reservation.userId(),
                reservation.lectureId()
        );
    }
}
