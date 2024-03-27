package com.hanghae.reservation.storage.reservation;

import com.hanghae.reservation.domain.reservation.Reservation;
import com.hanghae.reservation.storage.BaseEntity;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reservation")
public class ReservationEntity extends BaseEntity {
    private Long userId;
    private Long lectureId;
    private String lectureTitle;
    private LocalDateTime openTime;

    protected ReservationEntity() {
    }

    public ReservationEntity(Long userId, Long lectureId, String lectureTitle, LocalDateTime openTime) {
        this.userId = userId;
        this.lectureId = lectureId;
        this.lectureTitle = lectureTitle;
        this.openTime = openTime;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public String getLectureTitle() {
        return lectureTitle;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public Reservation toReservation() {
        return new Reservation(getId(), userId, lectureId, lectureTitle, openTime);
    }
}
