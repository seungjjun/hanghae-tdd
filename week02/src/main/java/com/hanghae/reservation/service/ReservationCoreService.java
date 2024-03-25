package com.hanghae.reservation.service;

import com.hanghae.reservation.domain.Reservation;
import com.hanghae.reservation.dto.request.LectureReservationRequest;

public interface ReservationCoreService {
    Reservation reserve(Long userId, LectureReservationRequest lectureReservationRequest) throws Exception;

    Reservation checkReservationStatus(Long userId);
}
