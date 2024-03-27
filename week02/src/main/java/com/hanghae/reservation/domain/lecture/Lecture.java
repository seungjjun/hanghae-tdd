package com.hanghae.reservation.domain.lecture;

import java.time.LocalDateTime;

public record Lecture(
        Long id,
        String title,
        LocalDateTime openTime
) {
}
