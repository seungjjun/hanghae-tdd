package com.hanghae.reservation.domain.lecture;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LectureAppender {
    private final LectureRepository lectureRepository;

    public LectureAppender(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Lecture create(String title, LocalDateTime openTime) {
        return lectureRepository.create(title, openTime);
    }
}
