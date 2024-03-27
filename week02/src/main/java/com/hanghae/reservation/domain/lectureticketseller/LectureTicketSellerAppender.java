package com.hanghae.reservation.domain.lectureticketseller;

import org.springframework.stereotype.Component;

@Component
public class LectureTicketSellerAppender {
    private final LectureTicketSellerRepository lectureTicketSellerRepository;

    public LectureTicketSellerAppender(LectureTicketSellerRepository lectureTicketSellerRepository) {
        this.lectureTicketSellerRepository = lectureTicketSellerRepository;
    }

    public LectureTicketSeller create(Long lectureId) {
        return lectureTicketSellerRepository.create(lectureId);
    }
}
