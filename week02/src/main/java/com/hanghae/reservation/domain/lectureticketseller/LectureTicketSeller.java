package com.hanghae.reservation.domain.lectureticketseller;

public record LectureTicketSeller (
        Long id,
        Long lectureId,
        Long lectureTicketNumber
) {
    public LectureTicketSeller sellTicket() {
        if (lectureTicketNumber <= 0) {
            throw new RuntimeException("해당 특강은 신청이 마감되었습니다.");
        }
        return new LectureTicketSeller(id, lectureId, lectureTicketNumber - 1);
    }

    public LectureTicketSeller chargeTicket(Long chargingTicketNumber) {
        return new LectureTicketSeller(id, lectureId, lectureTicketNumber + chargingTicketNumber);
    }
}
