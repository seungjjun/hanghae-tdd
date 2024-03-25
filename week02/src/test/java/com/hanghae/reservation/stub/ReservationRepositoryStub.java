package com.hanghae.reservation.stub;

import com.hanghae.reservation.storage.ReservationEntity;
import com.hanghae.reservation.repository.ReservationRepository;

import java.util.*;

public class ReservationRepositoryStub implements ReservationRepository {
    private final Map<Long, ReservationEntity> reservationMap = new HashMap<>();
    private final Map<Long, Long> lectureMap = new HashMap<>();
    private Long reservationId = 1L;

    @Override
    public ReservationEntity reserve(ReservationEntity reservationEntity) {
        reservationMap.put(reservationId++, reservationEntity);
        lectureMap.put(reservationId, reservationEntity.getLectureId());
        return reservationEntity;
    }

    @Override
    public Optional<ReservationEntity> findByUserId(Long userId) {
        for (Map.Entry<Long, ReservationEntity> entry : reservationMap.entrySet()) {
            if (entry.getValue().getUserId().equals(userId)) {
                return Optional.of(entry.getValue());
            }
        }

        return Optional.empty();
    }

    @Override
    public List<ReservationEntity> findAllByLectureId(Long lectureId) {
        List<ReservationEntity> reservationList = new ArrayList<>();

        for (Map.Entry<Long, ReservationEntity> entry : reservationMap.entrySet()) {
            if (entry.getValue().getLectureId().equals(lectureId)) {
                reservationList.add(entry.getValue());
            }
        }

        return reservationList;
    }

    @Override
    public boolean existsByUserIdAndLectureId(Long userId, Long lectureId) {
        for (Map.Entry<Long, ReservationEntity> entry : reservationMap.entrySet()) {
            ReservationEntity reservationEntity = entry.getValue();
            if (reservationEntity.getUserId().equals(userId) && reservationEntity.getLectureId().equals(lectureId)) {
                return true;
            }
        }

        return false;
    }
}
