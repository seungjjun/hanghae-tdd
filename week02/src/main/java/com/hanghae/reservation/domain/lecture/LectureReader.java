package com.hanghae.reservation.domain.lecture;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LectureReader {
    private final LectureRepository lectureRepository;

    public LectureReader(LectureRepository lectureRepository) {
        this.lectureRepository = lectureRepository;
    }

    public Lecture read(Long lectureId) {
        return lectureRepository.readById(lectureId);
    }

    public List<Lecture> readAll() {
        return lectureRepository.readAll();
    }
}
