package com.hanghae.reservation.controller;

import com.hanghae.reservation.domain.Reservation;
import com.hanghae.reservation.dto.request.LectureReservationRequest;
import com.hanghae.reservation.dto.response.ReservationStatusResponse;
import com.hanghae.reservation.service.ReservationCoreService;
import com.hanghae.reservation.util.ReservationTimeChecker;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationCoreService reservationCoreService;

    public ReservationController(ReservationCoreService reservationCoreService) {
        this.reservationCoreService = reservationCoreService;
    }

    @PostMapping("/{userId}")
    public ReservationStatusResponse reserve(@PathVariable Long userId, @RequestBody LectureReservationRequest request) throws Exception {
        ReservationTimeChecker.checkStartTime(LocalDateTime.now());
        Reservation reservation = reservationCoreService.reserve(userId, request);
        return ReservationStatusResponse.from(reservation);
    }

    @GetMapping("/{userId}")
    public ReservationStatusResponse reservationStatus(@PathVariable Long userId) {
        Reservation reservation = reservationCoreService.checkReservationStatus(userId);
        return ReservationStatusResponse.from(reservation);
    }
}
