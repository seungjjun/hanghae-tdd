package com.hanghae.reservation.domain;

public record Reservation(
        Long id,
        Long userId,
        Long lectureId
) {
}
