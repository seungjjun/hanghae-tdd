package com.hanghae.reservation.api.dto.request;

import java.time.LocalDateTime;

public record RegisterLectureRequest(String title, LocalDateTime openTime) {
}
