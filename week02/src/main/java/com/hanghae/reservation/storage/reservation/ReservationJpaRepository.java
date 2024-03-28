package com.hanghae.reservation.storage.reservation;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    boolean existsByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime);
    boolean existsByUserIdAndLectureId(Long userId, Long lectureId);
    Optional<ReservationEntity> findByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime);
}
