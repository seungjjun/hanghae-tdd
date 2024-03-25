package com.hanghae.reservation.controller;

import com.hanghae.reservation.service.ReservationCoreService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReservationController.class)
@ActiveProfiles("test")
@Import(TestStubConfig.class)
class ReservationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private ReservationCoreService reservationCoreService;


    @BeforeEach
    void setUp() {
        reservationCoreService = new ReservationServiceStub();
    }

    @Test
    @DisplayName("특강 신청 성공 테스트")
    void success_post_reserve() throws Exception {
        // Given
        Long userId = 100L;

        // When && Then
        mockMvc.perform(post("/reservation/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "lectureId": 1
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(100L));
    }

    @Test
    @DisplayName("이미 예약이 완료된 특강을 예약 신청 할 때, 실패 테스트")
    void when_already_succeed_reserve_then_fail_post_reserve() throws Exception {
        // Given
        Long userId = 1L;

        // When && Then
        mockMvc.perform(post("/reservation/" + userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {
                            "lectureId": 1
                        }
                        """))
                .andExpect(status().isInternalServerError());
    }

    @Test
    @DisplayName("특강 신청 완료 여부 조회 성공 테스트")
    void success_get_completed_reserve_status() throws Exception {
        // Given
        Long userId = 2L;

        // When && Then
        mockMvc.perform(get("/reservation/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(2L));
    }

    @Test
    @DisplayName("특강 신청 조회 실패 테스트")
    void fail_get_reserve_status() throws Exception {
        // Given
        Long userId = 1L;

        // When && Then
        mockMvc.perform(get("/reservation/" + userId))
                .andExpect(status().isInternalServerError());
    }
}
