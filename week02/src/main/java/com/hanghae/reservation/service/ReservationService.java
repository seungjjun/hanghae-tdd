package com.hanghae.reservation.service;

import com.hanghae.reservation.domain.Reservation;
import com.hanghae.reservation.dto.request.LectureReservationRequest;
import com.hanghae.reservation.repository.ReservationRepository;
import com.hanghae.reservation.storage.ReservationEntity;
import com.hanghae.reservation.util.LockHandler;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ReservationService implements ReservationCoreService {
    private static final int LIMIT_RESERVATION_NUMBER = 30;

    private final ReservationRepository reservationRepository;
    private final LockHandler lockHandler;

    public ReservationService(ReservationRepository reservationRepository, LockHandler lockHandler) {
        this.reservationRepository = reservationRepository;
        this.lockHandler = lockHandler;
    }

    @Override
    public Reservation reserve(Long userId, LectureReservationRequest request) throws Exception {
        return lockHandler.withLock(userId, () -> {
            if (reservationRepository.existsByUserIdAndLectureId(userId, request.lectureId())) {
                throw new RuntimeException("이미 예약이 완료된 특강 입니다.");
            }

            List<ReservationEntity> reservationEntityList = reservationRepository.findAllByLectureId(request.lectureId());
            if (reservationEntityList.size() >= LIMIT_RESERVATION_NUMBER) {
                throw new RuntimeException("해당 특강은 신청이 마감되었습니다.");
            }

            ReservationEntity reservationEntity = new ReservationEntity(userId, request.lectureId());
            return reservationRepository.reserve(reservationEntity).toReservation();
        });
    }

    @Override
    public Reservation checkReservationStatus(Long userId) {
        ReservationEntity reservationEntity = reservationRepository.findByUserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("특강 명단에 존재하지 않는 사용자 입니다."));
        return reservationEntity.toReservation();
    }
}
