package com.hanghae.reservation.api.controller;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;
import com.hanghae.reservation.api.dto.response.ReservationResponse;
import com.hanghae.reservation.api.dto.response.ReservationStatusResponse;
import com.hanghae.reservation.domain.reservation.ReservationCoreService;
import com.hanghae.reservation.domain.reservation.Reservation;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Optional;

@RestController
@RequestMapping("/reservation")
public class ReservationController {

    private final ReservationCoreService reservationCoreService;

    public ReservationController(ReservationCoreService reservationCoreService) {
        this.reservationCoreService = reservationCoreService;
    }

    @PostMapping("/{userId}")
    public ReservationResponse reserve(@PathVariable Long userId,
                                       @RequestBody LectureReservationRequest request) {
        Reservation reservation = reservationCoreService.reserve(userId, request);
        return ReservationResponse.from(reservation);
    }

    @GetMapping("/{userId}")
    public ReservationStatusResponse reservationStatus(@PathVariable Long userId,
                                                       @RequestParam(value = "title") String title,
                                                       @RequestParam(value = "openTime") @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss") LocalDateTime openTime) {
        Optional<Reservation> reservation = reservationCoreService.checkReservationStatus(userId, title, openTime);
        return ReservationStatusResponse.from(reservation);
    }
}
