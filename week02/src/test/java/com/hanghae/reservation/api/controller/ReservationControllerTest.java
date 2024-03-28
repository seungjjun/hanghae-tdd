package com.hanghae.reservation.api.controller;

import com.hanghae.reservation.domain.reservation.ReservationCoreService;
import com.hanghae.reservation.stub.ReservationServiceStub;
import com.hanghae.reservation.stub.TestStubConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReservationController.class)
@ActiveProfiles("test")
@Import(TestStubConfig.class)
class ReservationControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private ReservationCoreService reservationService;

    @BeforeEach
    void setUp() {
        reservationService = new ReservationServiceStub();
    }

    @Test
    @DisplayName("특강 신청 성공 테스트")
    void success_reserve_lecture() throws Exception {
        // Given
        Long userId = 1L;

        // When && Then
        mockMvc.perform(post("/reservation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                "lectureId": 1,
                                "lectureTitle": "TDD"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lectureTitle").value("TDD"));
    }

    @Test
    @DisplayName("특강 신청 여부 조회 성공 테스트")
    void success_check_reservation_status() throws Exception {
        // Given
        Long userId = 1L;

        // When && Then
        mockMvc.perform(get("/reservation/" + userId)
                        .param("title", "Clean Architecture")
                        .param("openTime", "2024-04-20T13:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Clean Architecture 특강 예약 완료 되었습니다."));
    }

    @Test
    @DisplayName("특강 명단에 존재 하지 않는 경우")
    void when_not_exists_lecture_list_then_receive_empty_message() throws Exception {
        // Given
        Long userId = 1L;

        // When && Then
        mockMvc.perform(get("/reservation/" + userId)
                        .param("title", "DDD")
                        .param("openTime", "2024-04-20T13:00:00"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("특강 명단에 존재하지 않습니다."));
    }
}
