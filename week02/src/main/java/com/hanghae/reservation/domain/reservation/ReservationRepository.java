package com.hanghae.reservation.domain.reservation;

import java.time.LocalDateTime;

public interface ReservationRepository {
    boolean existsByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime);
    Reservation create(Long userId, Long lectureId, String lectureTitle, LocalDateTime openTime);

    Reservation readByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime);
}
