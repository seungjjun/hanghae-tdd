package com.hanghae.reservation.stub;

import com.hanghae.reservation.domain.Reservation;
import com.hanghae.reservation.dto.request.LectureReservationRequest;
import com.hanghae.reservation.service.ReservationCoreService;

public class ReservationServiceStub implements ReservationCoreService {
    @Override
    public Reservation reserve(Long userId, LectureReservationRequest request) {
        if (userId == 1L) {
            throw new RuntimeException("이미 예약이 완료된 특강 입니다.");
        }

        return new Reservation(1L, userId, request.lectureId());
    }

    @Override
    public Reservation checkReservationStatus(Long userId) {
        if (userId == 1L) {
            throw new IllegalArgumentException("특강 명단에 존재하지 않는 사용자 입니다.");
        }
        return new Reservation(1L, userId, 1L);
    }
}
