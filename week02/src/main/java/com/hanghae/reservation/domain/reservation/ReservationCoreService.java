package com.hanghae.reservation.domain.reservation;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationCoreService {
    Reservation reserve(Long userId, LectureReservationRequest lectureReservationRequest);

    Optional<Reservation> checkReservationStatus(Long userId, String title, LocalDateTime openTime);
}
