package com.hanghae.reservation.storage.lectureticketseller;

import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.storage.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Version;

@Entity
@Table(name = "lecture_ticket_seller")
public class LectureTicketSellerEntity extends BaseEntity {
    private Long lectureId;
    private Long lectureTicketNumber;

    protected LectureTicketSellerEntity() {
    }

    public LectureTicketSellerEntity(Long lectureId, Long lectureTicketNumber) {
        this.lectureId = lectureId;
        this.lectureTicketNumber = lectureTicketNumber;
    }

    public Long getLectureId() {
        return lectureId;
    }

    public Long getLectureTicketNumber() {
        return lectureTicketNumber;
    }

    public LectureTicketSeller toLectureTicketSeller() {
        return new LectureTicketSeller(getId(), lectureId, lectureTicketNumber);
    }

    public void updateTicketNumber(LectureTicketSeller lectureTicketSeller) {
        this.lectureTicketNumber = lectureTicketSeller.lectureTicketNumber();
    }
}
