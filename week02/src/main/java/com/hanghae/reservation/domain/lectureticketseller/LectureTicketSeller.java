package com.hanghae.reservation.domain.lectureticketseller;

public record LectureTicketSeller (
        Long id,
        Long lectureId,
        Long lectureTicketNumber
) {
    public LectureTicketSeller sellTicket() {
        return new LectureTicketSeller(id, lectureId, lectureTicketNumber - 1);
    }

    public boolean checkRemainderTicketNumber() {
        return lectureTicketNumber <= 0;
    }

    public LectureTicketSeller chargeTicket(Long chargingTicketNumber) {
        return new LectureTicketSeller(id, lectureId, lectureTicketNumber + chargingTicketNumber);
    }
}
