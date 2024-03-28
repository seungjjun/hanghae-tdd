package com.hanghae.reservation.domain.reservation;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;
import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.domain.lecture.LectureReader;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerManager;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationServiceTest {
    private ReservationService reservationService;
    private LectureReader lectureReader;
    private ReservationReader reservationReader;
    private LectureTicketSellerReader lectureTicketSellerReader;
    private LectureTicketSellerManager lectureTicketSellerManager;
    private ReservationAppender reservationAppender;

    @BeforeEach
    void setUp() {
        lectureReader = mock(LectureReader.class);
        reservationReader = mock(ReservationReader.class);
        lectureTicketSellerReader = mock(LectureTicketSellerReader.class);
        lectureTicketSellerManager = mock(LectureTicketSellerManager.class);
        reservationAppender = mock(ReservationAppender.class);

        reservationService = new ReservationService(
                lectureReader,
                reservationReader,
                lectureTicketSellerReader,
                lectureTicketSellerManager,
                reservationAppender
        );
    }

    @Test
    @DisplayName("특강 신청 성공 테스트")
    void success_reserve() {
        // Given
        Long userId = 1L;
        Long lectureId = 1L;
        String lectureTitle = "TDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 4, 20, 13, 0, 0);
        LectureReservationRequest request = new LectureReservationRequest(lectureId, lectureTitle);

        Lecture lecture = new Lecture(lectureId, lectureTitle, openTime);
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, lectureId, 30L);
        Reservation reservation = new Reservation(1L, userId, lectureId, lectureTitle, openTime);

        when(lectureReader.read(lectureId)).thenReturn(lecture);
        when(lectureTicketSellerReader.readByLectureId(lectureId)).thenReturn(lectureTicketSeller);
        when(reservationAppender.create(userId, lectureId, lectureTitle, openTime)).thenReturn(reservation);

        // When
        Reservation reserve = reservationService.reserve(userId, request);

        // Then
        assertThat(reserve).isNotNull();
        assertThat(reserve.lectureTitle()).isEqualTo("TDD");
        assertThat(reserve.openTime()).isEqualTo(LocalDateTime.of(2024, 4, 20, 13, 0, 0));
    }

    @Test
    @DisplayName("특강 신청 여부를 조회 성공 테스트")
    void success_find_reservation_status() {
        // Given
        Long userId = 1L;
        Long lectureId = 1L;
        String lectureTitle = "TDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 4, 20, 13, 0, 0);

        Reservation reservation = new Reservation(1L, userId, lectureId, lectureTitle, openTime);

        when(reservationReader.checkByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime)).thenReturn(true);
        when(reservationReader.readByUserIdAndLectureTitleAndOpenTime(userId, lectureTitle, openTime)).thenReturn(reservation);

        // When
        Optional<Reservation> checkReservationStatus = reservationService.checkReservationStatus(userId, lectureTitle, openTime);


        // Then
        assertThat(checkReservationStatus.get().lectureTitle()).isEqualTo("TDD");
        assertThat(checkReservationStatus.get().openTime()).isEqualTo(LocalDateTime.of(2024, 4, 20, 13, 0, 0));
    }
}
