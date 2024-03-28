package com.hanghae.reservation.domain.lecture;

import com.hanghae.reservation.api.dto.request.LectureTicketChargeRequest;
import com.hanghae.reservation.api.dto.request.RegisterLectureRequest;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerAppender;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerManager;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LectureServiceTest {

    private LectureService lectureService;
    private LectureReader lectureReader;
    private LectureTicketSellerReader lectureTicketSellerReader;
    private LectureTicketSellerAppender lectureTicketSellerAppender;
    private LectureAppender lectureAppender;
    private LectureTicketSellerManager lectureTicketSellerManager;

    @BeforeEach
    void setUp() {
        lectureReader = mock(LectureReader.class);
        lectureTicketSellerReader = mock(LectureTicketSellerReader.class);
        lectureTicketSellerAppender = mock(LectureTicketSellerAppender.class);
        lectureAppender = mock(LectureAppender.class);
        lectureTicketSellerManager = mock(LectureTicketSellerManager.class);

        lectureService = new LectureService(lectureReader, lectureTicketSellerReader, lectureTicketSellerAppender, lectureAppender, lectureTicketSellerManager);
    }

    @Test
    @DisplayName("강의 등록 성공 테스트")
    void success_register_lecture() {
        // Given
        Long lectureId = 1L;
        String title = "TDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 4, 20, 13, 0, 0);
        RegisterLectureRequest request = new RegisterLectureRequest(title, openTime);

        Lecture lecture = new Lecture(lectureId, title, openTime);
        when(lectureAppender.create(any(), any())).thenReturn(lecture);

        // When
        Lecture registeredLecture = lectureService.register(request);

        // Then
        verify(lectureTicketSellerAppender).create(any());
        assertThat(registeredLecture.title()).isEqualTo("TDD");
    }

    @Test
    @DisplayName("티켓 충전 성공 테스트")
    void success_charge_ticket() {
        // Given
        Long lectureId = 1L;
        Long chargingTicketNumber = 30L;

        LectureTicketChargeRequest request = new LectureTicketChargeRequest(lectureId, chargingTicketNumber);
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, lectureId, 0L);
        when(lectureTicketSellerReader.readByLectureId(any())).thenReturn(lectureTicketSeller);

        LectureTicketSeller chargedLectureTicketSeller = new LectureTicketSeller(1L, lectureId, chargingTicketNumber);
        when(lectureTicketSellerManager.chargeTicket(lectureTicketSeller, chargingTicketNumber)).thenReturn(chargedLectureTicketSeller);

        // When
        LectureTicketSeller charged = lectureService.charge(request);

        // Then
        assertThat(charged.lectureTicketNumber()).isEqualTo(30L);
    }
}
