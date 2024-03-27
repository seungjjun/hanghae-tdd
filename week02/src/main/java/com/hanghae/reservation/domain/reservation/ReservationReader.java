package com.hanghae.reservation.domain.reservation;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationReader {
    private final ReservationRepository reservationRepository;

    public ReservationReader(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reservation readeByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime) {
        return reservationRepository.readeByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime);
    }
}
