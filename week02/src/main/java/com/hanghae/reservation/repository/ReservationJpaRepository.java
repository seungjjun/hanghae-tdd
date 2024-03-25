package com.hanghae.reservation.repository;

import com.hanghae.reservation.storage.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationJpaRepository extends JpaRepository<ReservationEntity, Long> {

    Optional<ReservationEntity> findByUserId(Long userId);

    List<ReservationEntity> findALlByLectureId(Long lectureId);

    boolean existsByUserIdAndLectureId(Long userId, Long lectureId);
}
