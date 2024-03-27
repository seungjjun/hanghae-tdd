package com.hanghae.reservation.domain.lecture;

import com.hanghae.reservation.api.dto.request.LectureTicketChargeRequest;
import com.hanghae.reservation.api.dto.request.RegisterLectureRequest;
import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;

import java.util.List;

public interface LectureCoreService {
    List<Lecture> getLectures();

    Lecture register(RegisterLectureRequest request);

    LectureTicketSeller charge(LectureTicketChargeRequest request);
}
