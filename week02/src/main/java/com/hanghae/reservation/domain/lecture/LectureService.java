package com.hanghae.reservation.domain.lecture;

import com.hanghae.reservation.api.dto.request.LectureTicketChargeRequest;
import com.hanghae.reservation.api.dto.request.RegisterLectureRequest;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerAppender;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerManager;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LectureService implements LectureCoreService {
    private final LectureReader lectureReader;
    private final LectureTicketSellerReader lectureTicketSellerReader;
    private final LectureTicketSellerAppender lectureTicketSellerAppender;
    private final LectureAppender lectureAppender;
    private final LectureTicketSellerManager lectureTicketSellerManager;

    @Override
    public List<Lecture> getLectures() {
        return lectureReader.readAll();
    }

    @Override
    @Transactional
    public Lecture register(RegisterLectureRequest request) {
        Lecture lecture = lectureAppender.create(request.title(), request.openTime());
        lectureTicketSellerAppender.create(lecture.id());
        return lecture;
    }

    @Override
    @Transactional
    public LectureTicketSeller charge(LectureTicketChargeRequest request) {
        LectureTicketSeller lectureTicketSeller = lectureTicketSellerReader.readByLectureId(request.lectureId());
        return lectureTicketSellerManager.chargeTicket(lectureTicketSeller, request.chargingTicketNumber());
    }
}
