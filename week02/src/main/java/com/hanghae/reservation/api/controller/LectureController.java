package com.hanghae.reservation.api.controller;

import com.hanghae.reservation.api.dto.request.LectureTicketChargeRequest;
import com.hanghae.reservation.api.dto.response.ChargedLectureTicketResponse;
import com.hanghae.reservation.api.dto.response.LectureListResponse;
import com.hanghae.reservation.api.dto.request.RegisterLectureRequest;
import com.hanghae.reservation.api.dto.response.RegisteredLectureResponse;
import com.hanghae.reservation.domain.lecture.LectureCoreService;
import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/lectures")
public class LectureController {
    private final LectureCoreService lectureService;

    public LectureController(LectureCoreService lectureService) {
        this.lectureService = lectureService;
    }

    @GetMapping
    public LectureListResponse getLectureList() {
        List<Lecture> lectures = lectureService.getLectures();
        return LectureListResponse.from(lectures);
    }

    @PostMapping
    public RegisteredLectureResponse postLecture(@RequestBody RegisterLectureRequest request) {
        Lecture lecture = lectureService.register(request);
        return RegisteredLectureResponse.from(lecture);
    }

    @PatchMapping("/charge")
    public ChargedLectureTicketResponse charge(@RequestBody LectureTicketChargeRequest request) {
        LectureTicketSeller lectureTicketSeller = lectureService.charge(request);
        return ChargedLectureTicketResponse.from(lectureTicketSeller);
    }
}
