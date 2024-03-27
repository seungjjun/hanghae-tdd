package com.hanghae.reservation.domain.reservation;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;

import java.time.LocalDateTime;

public interface ReservationCoreService {
    Reservation reserve(Long userId, LectureReservationRequest lectureReservationRequest);

    Reservation checkReservationStatus(Long userId, String title, LocalDateTime openTime);
}
