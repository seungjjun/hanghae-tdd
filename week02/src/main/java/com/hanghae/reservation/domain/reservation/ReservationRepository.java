package com.hanghae.reservation.domain.reservation;

import java.time.LocalDateTime;

public interface ReservationRepository {
    Reservation readeByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime);
    Reservation create(Long userId, Long lectureId, String lectureTitle, LocalDateTime openTime);
}
