package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.lecture.Lecture;

import java.time.LocalDateTime;

public record LectureResponse(Long lectureId, String title, LocalDateTime openTime) {
    public static LectureResponse from(Lecture lecture) {
        return new LectureResponse(lecture.id(), lecture.title(), lecture.openTime());
    }
}
