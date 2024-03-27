package com.hanghae.reservation.domain.reservation;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationAppender {
    private final ReservationRepository reservationRepository;

    public ReservationAppender(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation create(Long userId, Long lectureId, String lectureTitle, LocalDateTime openTime) {
        return reservationRepository.create(userId, lectureId, lectureTitle, openTime);
    }
}
