package com.hanghae.reservation.storage;

import com.hanghae.reservation.domain.Reservation;
import jakarta.persistence.*;

@Entity
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity {
    private Long userId;
    private Long lectureId;

    protected ReservationEntity() {
    }

    public ReservationEntity(Long userId, Long lectureId) {
        this.userId = userId;
        this.lectureId = lectureId;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public Reservation toReservation() {
        return new Reservation(getId(), userId, lectureId);
    }
}
