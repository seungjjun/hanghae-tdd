package com.hanghae.reservation.domain.lectureticketseller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.assertj.core.api.Assertions.assertThat;

class LectureTicketSellerTest {
    @Test
    @DisplayName("티켓 충전 성공 테스트")
    void success_charge_ticket() {
        // Given
        Long chargingTicketNumber = 5L;
        Long currentTicketNumber = 25L;
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, currentTicketNumber);


        // When
        LectureTicketSeller chargedTicket = lectureTicketSeller.chargeTicket(chargingTicketNumber);

        // Then
        assertThat(chargedTicket.lectureTicketNumber()).isEqualTo(25L + 5L);
    }

    @Test
    @DisplayName("티켓 판매 성공 테스트")
    void success_sell_ticket() {
        // Given
        Long currentTicketNumber = 5L;
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, currentTicketNumber);

        // When
        LectureTicketSeller soldTicket = lectureTicketSeller.sellTicket();

        // Then
        assertThat(soldTicket.lectureTicketNumber()).isEqualTo(5L - 1L);
    }

    @Test
    @DisplayName("티켓 수 부족으로 인한 예약 실패 테스트")
    void when_lack_ticket_number_then_fail_sell_ticket() {
        // Given
        Long currentTicketNumber = 0L;
        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, 1L, currentTicketNumber);

        // When && Then
        assertThrows(RuntimeException.class, () -> {
            lectureTicketSeller.sellTicket();
        });
    }

}
