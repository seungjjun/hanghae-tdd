package com.hanghae.reservation.storage.lecture;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface LectureJpaRepository extends JpaRepository<LectureEntity, Long> {
    boolean existsByTitleAndOpenTime(String title, LocalDateTime openTime);
}
