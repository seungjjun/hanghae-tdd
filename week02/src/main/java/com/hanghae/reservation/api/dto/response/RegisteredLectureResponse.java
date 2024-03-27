package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.lecture.Lecture;

import java.time.LocalDateTime;

public record RegisteredLectureResponse(Long lectureId, String title, LocalDateTime openTime) {
    public static RegisteredLectureResponse from(Lecture lecture) {
        return new RegisteredLectureResponse(lecture.id(), lecture.title(), lecture.openTime());
    }
}
