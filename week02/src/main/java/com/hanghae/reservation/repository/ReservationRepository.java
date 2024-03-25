package com.hanghae.reservation.repository;

import com.hanghae.reservation.storage.ReservationEntity;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository {
    ReservationEntity reserve(ReservationEntity reservationEntity);

    Optional<ReservationEntity> findByUserId(Long userId);

    List<ReservationEntity> findAllByLectureId(Long lectureId);

    boolean existsByUserIdAndLectureId(Long userId, Long lectureId);
}
