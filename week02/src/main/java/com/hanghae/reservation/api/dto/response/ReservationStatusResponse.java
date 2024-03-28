package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.reservation.Reservation;

import java.util.Optional;

public record ReservationStatusResponse(String status) {

    public static ReservationStatusResponse from(Optional<Reservation> reservation) {
        if (reservation.isEmpty()) {
            return new ReservationStatusResponse("특강 명단에 존재하지 않습니다.");
        }
        return new ReservationStatusResponse(
                reservation.get().lectureTitle() + " 특강 예약 완료 되었습니다.");
    }
}
