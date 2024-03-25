package com.hanghae.reservation.dto.response;

public record ErrorResponse(
        String code,
        String message
) {
}
