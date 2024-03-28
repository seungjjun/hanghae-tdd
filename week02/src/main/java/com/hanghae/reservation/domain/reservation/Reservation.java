package com.hanghae.reservation.domain.reservation;

import java.time.LocalDateTime;
import java.util.Optional;

public record Reservation(
        Long id,
        Long userId,
        Long lectureId,
        String lectureTitle,
        LocalDateTime openTime
) {
    public static Optional<Reservation> empty() {
        return Optional.empty();
    }
}
