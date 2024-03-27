package com.hanghae.reservation.domain.reservation;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;
import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.domain.lecture.LectureReader;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerManager;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class ReservationService implements ReservationCoreService {
    private final LectureReader lectureReader;
    private final ReservationReader reservationReader;
    private final LectureTicketSellerReader lectureTicketSellerReader;
    private final LectureTicketSellerManager lectureTicketSellerManager;
    private final ReservationAppender reservationAppender;

    @Override
    @Transactional
    public Reservation reserve(Long userId, LectureReservationRequest request) {
        Lecture lecture = lectureReader.read(request.lectureId());

        LectureTicketSeller lectureTicketSeller = lectureTicketSellerReader.readByLectureId(lecture.id());
        lectureTicketSellerManager.sellTicket(lectureTicketSeller, lecture.openTime());

        return reservationAppender.create(userId, lecture.id(), lecture.title(), lecture.openTime());
    }

    @Override
    public Reservation checkReservationStatus(Long userId, String title, LocalDateTime openTime) {
        return reservationReader.readeByUserIdAndLectureTitleAndOpenTime(userId, title, openTime);
    }
}
