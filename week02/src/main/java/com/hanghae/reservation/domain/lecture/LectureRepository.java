package com.hanghae.reservation.domain.lecture;

import java.time.LocalDateTime;
import java.util.List;

public interface LectureRepository {
    Lecture readById(Long id);

    List<Lecture> readAll();

    Lecture create(String title, LocalDateTime openTime);
}
