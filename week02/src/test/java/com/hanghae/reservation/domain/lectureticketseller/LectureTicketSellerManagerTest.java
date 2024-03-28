package com.hanghae.reservation.domain.lectureticketseller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LectureTicketSellerManagerTest {
    private LectureTicketSellerManager lectureTicketSellerManager;

    private LectureTicketSellerRepository lectureTicketSellerRepository;

    @BeforeEach
    void setUp() {
        lectureTicketSellerRepository = mock(LectureTicketSellerRepository.class);

        lectureTicketSellerManager = new LectureTicketSellerManager(lectureTicketSellerRepository);
    }

    @Test
    @DisplayName("특강 예약 티켓 판매 성공 테스트")
    void success_sell_ticket() {
        // Given
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, 30L);
        LocalDateTime openTime = LocalDateTime.of(2024, 3, 1, 13, 0, 0);

        // When
        lectureTicketSellerManager.sellTicket(lectureTicketSeller, openTime);

        // Then
        verify(lectureTicketSellerRepository).updateTicketSeller(any());
    }

    @Test
    @DisplayName("특강 오픈 시간이 지나지 않았을 경우 실패 테스트")
    void when_not_open_time_then_fail_sell_ticket() {
        // Given
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, 30L);
        LocalDateTime openTime = LocalDateTime.of(2024, 12, 1, 13, 0, 0);

        // When && Then
        assertThrows(RuntimeException.class, () -> {
            lectureTicketSellerManager.sellTicket(lectureTicketSeller, openTime);
        });
    }

    @Test
    @DisplayName("티켓이 부족할 경우 실패 테스트")
    void when_lack_ticket_then_fail_sell_ticket() {
        // Given
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, 0L);
        LocalDateTime openTime = LocalDateTime.of(2024, 3, 1, 13, 0, 0);

        // When && Then
        assertThrows(RuntimeException.class, () -> {
            lectureTicketSellerManager.sellTicket(lectureTicketSeller, openTime);
        });
    }

    @Test
    @DisplayName("티켓 충전 성공 테스트")
    void success_charge_ticket() {
        // Given
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, 30L);
        Long chargingTicketNumber = 30L;


        // When
        lectureTicketSellerManager.chargeTicket(lectureTicketSeller, chargingTicketNumber);

        // Then
        verify(lectureTicketSellerRepository).updateTicketSeller(any());
    }

    @Test
    @DisplayName("티켓 충전 실패 테스트")
    void when_charging_ticket_number_is_negative_then_fail_charge_ticket() {
        // Given
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, 30L);
        Long chargingTicketNumber = -1L;

        // When && Then
        assertThrows(RuntimeException.class, () -> {
            lectureTicketSellerManager.chargeTicket(lectureTicketSeller, chargingTicketNumber);
        });
    }
}
