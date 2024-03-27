package com.hanghae.reservation.domain.lectureticketseller;

public interface LectureTicketSellerRepository {
    LectureTicketSeller readByLectureId(Long lectureId);
    LectureTicketSeller updateTicketSeller(LectureTicketSeller lectureTicketSeller);
    LectureTicketSeller create(Long lectureId);
}
