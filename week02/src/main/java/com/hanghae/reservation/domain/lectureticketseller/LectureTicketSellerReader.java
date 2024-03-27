package com.hanghae.reservation.domain.lectureticketseller;

import org.springframework.stereotype.Component;

@Component
public class LectureTicketSellerReader {
    private final LectureTicketSellerRepository lectureTicketSellerRepository;

    public LectureTicketSellerReader(LectureTicketSellerRepository lectureTicketSellerRepository) {
        this.lectureTicketSellerRepository = lectureTicketSellerRepository;
    }

    public LectureTicketSeller readByLectureId(Long lectureId) {
        return lectureTicketSellerRepository.readByLectureId(lectureId);
    }
}
