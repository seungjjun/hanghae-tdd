package com.hanghae.reservation.domain.lectureticketseller;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class LectureTicketSellerManager {
    private final LectureTicketSellerRepository lectureTicketSellerRepository;

    public LectureTicketSellerManager(LectureTicketSellerRepository lectureTicketSellerRepository) {
        this.lectureTicketSellerRepository = lectureTicketSellerRepository;
    }

    public LectureTicketSeller sellTicket(LectureTicketSeller lectureTicketSeller, LocalDateTime openTime) {
        if (LocalDateTime.now().isBefore(openTime)) {
            throw new RuntimeException("아직 특강 신청을 하실 수 없습니다.");
        }

        if (lectureTicketSeller.checkRemainderTicketNumber()) {
            throw new RuntimeException("해당 특강은 신청이 마감되었습니다.");
        }
        return lectureTicketSellerRepository.updateTicketSeller(lectureTicketSeller.sellTicket());
    }

    public LectureTicketSeller chargeTicket(LectureTicketSeller lectureTicketSeller, Long chargingTicketNumber) {
        if (chargingTicketNumber <= 0) {
            throw new RuntimeException("음수는 입력할 수 없습니다.");
        }
        return lectureTicketSellerRepository.updateTicketSeller(lectureTicketSeller.chargeTicket(chargingTicketNumber));
    }
}
