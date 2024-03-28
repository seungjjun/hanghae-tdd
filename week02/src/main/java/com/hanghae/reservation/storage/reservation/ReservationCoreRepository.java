package com.hanghae.reservation.storage.reservation;

import com.hanghae.reservation.domain.reservation.Reservation;
import com.hanghae.reservation.domain.reservation.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public class ReservationCoreRepository implements ReservationRepository {
    private final ReservationJpaRepository reservationJpaRepository;

    public ReservationCoreRepository(ReservationJpaRepository reservationJpaRepository) {
        this.reservationJpaRepository = reservationJpaRepository;
    }

    @Override
    public boolean existsByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime) {
        return reservationJpaRepository.existsByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime);
    }

    @Override
    public Reservation create(Long userId, Long lectureId, String lectureTitle, LocalDateTime openTime) {
        if (reservationJpaRepository.existsByUserIdAndLectureId(userId, lectureId)) {
            throw new RuntimeException("이미 예약이 완료된 특강 입니다.");
        }
        return reservationJpaRepository
                .save(new ReservationEntity(userId, lectureId, lectureTitle, openTime))
                .toReservation();
    }

    @Override
    public Reservation readByUserIdAndLectureTitleAndOpenTime(Long userId, String lectureTitle, LocalDateTime openTime) {
        return reservationJpaRepository.findByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime)
                .orElseThrow(() -> new IllegalArgumentException("특강 명단에 존재하지 않는 사용자 입니다."))
                .toReservation();
    }
}
