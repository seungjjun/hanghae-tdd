package com.hanghae.reservation.api.dto.response;

public record ErrorResponse(
        String code,
        String message
) {
}
