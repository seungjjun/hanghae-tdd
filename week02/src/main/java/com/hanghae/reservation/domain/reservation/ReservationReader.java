package com.hanghae.reservation.domain.reservation;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ReservationReader {
    private final ReservationRepository reservationRepository;

    public ReservationReader(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public boolean checkByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime) {
        return reservationRepository.existsByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime);
    }

    public Reservation readByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime) {
        return reservationRepository.readByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime);
    }
}
