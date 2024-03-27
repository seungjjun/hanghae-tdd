package com.hanghae.reservation.domain.reservation;

import java.time.LocalDateTime;

public record Reservation(
        Long id,
        Long userId,
        Long lectureId,
        String lectureTitle,
        LocalDateTime openTime
) {
}
