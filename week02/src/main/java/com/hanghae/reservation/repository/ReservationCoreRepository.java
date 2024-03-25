package com.hanghae.reservation.repository;

import com.hanghae.reservation.storage.ReservationEntity;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public class ReservationCoreRepository implements ReservationRepository {
    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationCoreRepository(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    @Transactional
    public ReservationEntity reserve(ReservationEntity reservationEntity) {
        return reservationJpaRepository.save(reservationEntity);
    }

    @Override
    public Optional<ReservationEntity> findByUserId(Long userId) {
        return reservationJpaRepository.findByUserId(userId);
    }

    @Override
    public List<ReservationEntity> findAllByLectureId(Long lectureId) {
        return reservationJpaRepository.findALlByLectureId(lectureId);
    }

    @Override
    public boolean existsByUserIdAndLectureId(Long userId, Long lectureId) {
        return reservationJpaRepository.existsByUserIdAndLectureId(userId, lectureId);
    }
}
