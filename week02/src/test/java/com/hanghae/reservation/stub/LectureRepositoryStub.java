package com.hanghae.reservation.stub;

import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.domain.lecture.LectureRepository;

import java.time.LocalDateTime;
import java.util.*;

public class LectureRepositoryStub implements LectureRepository {
    private final Map<Long, Lecture> lectureMap = new HashMap<>();
    private Long id = 1L;

    @Override
    public Lecture readById(Long id) {
        return lectureMap.get(id);
    }

    @Override
    public List<Lecture> readAll() {
        return new ArrayList<>(lectureMap.values());
    }

    @Override
    public Lecture create(String title, LocalDateTime openTime) {
        Lecture lecture = new Lecture(id, title, openTime);
        lectureMap.put(id++, lecture);
        return lecture;
    }
}
