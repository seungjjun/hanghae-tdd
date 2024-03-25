package com.hanghae.reservation.service;

import com.hanghae.reservation.domain.Reservation;
import com.hanghae.reservation.dto.request.LectureReservationRequest;
import com.hanghae.reservation.repository.ReservationRepository;
import com.hanghae.reservation.stub.ReservationRepositoryStub;
import com.hanghae.reservation.util.LockHandler;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationServiceTest {

    private ReservationService reservationService;

    @BeforeEach
    void setUp() {
        ReservationRepository reservationRepository = new ReservationRepositoryStub();
        LockHandler lockHandler = new LockHandler();

        reservationService = new ReservationService(reservationRepository, lockHandler);
    }

    @Test
    @DisplayName("특강 예약 신청에 성공한다.")
    void reserve_success() throws Exception {
        // Given
        Long userId = 1L;
        LectureReservationRequest request = new LectureReservationRequest(1L);

        // When
        Reservation reservation = reservationService.reserve(userId, request);

        // Then
        assertThat(reservation.lectureId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("이미 신청이 성공된 특강은 예약에 실패한다.")
    void when_already_reserve_lecture_then_reserve_fail() throws Exception {
        // Given
        Long userId = 1L;
        LectureReservationRequest request = new LectureReservationRequest(1L);

        reservationService.reserve(userId, request);

        // When && Then
        assertThrows(RuntimeException.class, () -> {
            reservationService.reserve(userId, request);
        });
    }

    @Test
    @DisplayName("신청하려는 특강의 신청자가 30명이 초과되었을 경우 특강 예약에 실패한다.")
    void when_exceed_reserve_then_reserve_fail() throws Exception {
        // Given
        Long userId = 50L;
        LectureReservationRequest request = new LectureReservationRequest(1L);

        for (long i = 0; i < 30; i += 1) {
            reservationService.reserve(i, request);
        }

        // When && Then
        assertThrows(RuntimeException.class, () -> {
            reservationService.reserve(userId, request);
        });
    }

    @Test
    @DisplayName("예약에 성공한 사용자의 예약 정보를 받아온다.")
    void when_succeed_reserve_then_receive_reservation_status() throws Exception {
        // Given
        Long userId = 1L;
        LectureReservationRequest request = new LectureReservationRequest(1L);

        reservationService.reserve(userId, request);

        // When
        Reservation reservation = reservationService.checkReservationStatus(userId);

        // Then
        assertThat(reservation.userId()).isEqualTo(1L);
        assertThat(reservation.lectureId()).isEqualTo(1L);
    }

    @Test
    @DisplayName("예약하지 못한 특강의 예약 정보를 호출할 경우 실패한다.")
    void when_failed_reserve_then_not_receive_reservation_status() {
        // Given
        Long userId = 1L;

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> {
            reservationService.checkReservationStatus(userId);
        });
    }

    @RepeatedTest(100)
    @DisplayName("동시 특강 예약 테스트")
    void when_reserve_concurrency_then_fail_lecture_reserve() throws InterruptedException {
        LectureReservationRequest request = new LectureReservationRequest(1L);

        int numberOfThread = 30;
        ExecutorService executor = Executors.newFixedThreadPool(numberOfThread);
        CountDownLatch latch = new CountDownLatch(numberOfThread);

        for (long i = 1; i <= numberOfThread; i += 1) {
            long userId = i;
            executor.submit(() -> {
                try {
                   reservationService.reserve(userId, request);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        Long otherUserId = 50L;
        assertThrows(RuntimeException.class, () -> {
            reservationService.reserve(otherUserId, request);
        });
    }
}
