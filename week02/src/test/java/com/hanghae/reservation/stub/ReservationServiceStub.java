package com.hanghae.reservation.stub;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;
import com.hanghae.reservation.domain.reservation.Reservation;
import com.hanghae.reservation.domain.reservation.ReservationCoreService;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Optional;

public class ReservationServiceStub implements ReservationCoreService {
    @Override
    public Reservation reserve(Long userId, LectureReservationRequest request) {
        return new Reservation(1L, userId, request.lectureId(), request.lectureTitle(), LocalDateTime.now());
    }

    @Override
    public Optional<Reservation> checkReservationStatus(Long userId, String title, LocalDateTime openTime) {
        if (title.equals("DDD")) {
            return Optional.empty();
        }
        return Optional.of(new Reservation(1L, userId, 1L, title, openTime));
    }
}
