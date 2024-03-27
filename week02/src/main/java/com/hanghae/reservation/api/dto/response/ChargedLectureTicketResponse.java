package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;

public record ChargedLectureTicketResponse(Long lectureId, Long chargedTicketNumber) {
    public static ChargedLectureTicketResponse from(LectureTicketSeller lectureTicketSeller) {
        return new ChargedLectureTicketResponse(lectureTicketSeller.lectureId(), lectureTicketSeller.lectureTicketNumber());
    }
}
