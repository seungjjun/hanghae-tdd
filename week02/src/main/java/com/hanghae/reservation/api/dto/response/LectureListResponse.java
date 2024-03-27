package com.hanghae.reservation.api.dto.response;

import com.hanghae.reservation.domain.lecture.Lecture;

import java.util.List;

public record LectureListResponse(List<LectureResponse> lectures) {
    public static LectureListResponse from(List<Lecture> lectures) {
        return new LectureListResponse(lectures.stream()
                .map(LectureResponse::from)
                .toList());
    }
}
