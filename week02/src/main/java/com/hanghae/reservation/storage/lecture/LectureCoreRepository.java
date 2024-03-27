package com.hanghae.reservation.storage.lecture;

import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.domain.lecture.LectureRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Repository
public class LectureCoreRepository implements LectureRepository {
    private final LectureJpaRepository lectureJpaRepository;

    public LectureCoreRepository(LectureJpaRepository lectureJpaRepository) {
        this.lectureJpaRepository = lectureJpaRepository;
    }

    @Override
    public Lecture readById(Long id) {
        return lectureJpaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 특강 입니다."))
                .toLecture();
    }

    @Override
    public List<Lecture> readAll() {
        return lectureJpaRepository.findAll().stream()
                .map(LectureEntity::toLecture)
                .sorted(Comparator.comparing(Lecture::openTime).reversed())
                .toList();
    }

    @Override
    public Lecture create(String title, LocalDateTime openTime) {
        if (lectureJpaRepository.existsByTitleAndOpenTime(title, openTime)) {
            throw new RuntimeException("이미 같은 시간에 동일한 특강이 존재합니다.");
        }
        return lectureJpaRepository.save(new LectureEntity(title, openTime)).toLecture();
    }
}
