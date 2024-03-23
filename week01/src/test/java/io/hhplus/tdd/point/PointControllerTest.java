package io.hhplus.tdd.point;

import io.hhplus.tdd.point.service.PointServiceUseCase;
import io.hhplus.tdd.stub.*;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PointController.class)
@ActiveProfiles("test")
@Import(TestStubConfig.class)
class PointControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private PointServiceUseCase pointService;
    private long userId = 1;

    @BeforeEach
    void setUp() {
        pointService = new PointServiceStub();
    }

    /**
     * userId가 1인 사용자의 포인트 정보 응답값을 정상적으로 반환하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point 조회 성공 테스트")
    void getUserPoint() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(get("/point/" + userId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.id").value(1),
                        jsonPath("$.point").value(5_000),
                        jsonPath("$.updateMillis").exists()
                );
    }

    /**
     * userId가 1인 사용자의 포인트 히스토리 정보 응답값을 정상적으로 반환하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point history 조회 성공 테스트")
    void getPointHistory() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(get("/point/" + userId + "/histories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.pointHistories[0].userId").value(1),
                        jsonPath("$.pointHistories[0].amount").value(5_000),
                        jsonPath("$.pointHistories[0].type").value("CHARGE")
                );
    }

    /**
     * body로 입력받은 point를 정상 충전하여 응답값을 정상적으로 반환하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point 충전 성공 테스트")
    void succeedChargePoint() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(patch("/point/" + userId + "/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": 5000
                                }
                                """))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.currentPoint").value(5000),
                        jsonPath("$.transactionType").value("CHARGE")
                );
    }

    /**
     * body로 입력 받은 point가 음수일 경우 정상적으로 예외처리 하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point 충전 실패 테스트")
    void failedChargePoint() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(patch("/point/" + userId + "/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "amount": -5000
                                }
                                """))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.message").value("에러가 발생했습니다.")
                );
    }

    /**
     * body로 입력 받은 point를 정상적으로 사용하고 응답값을 정상적으로 반환하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point 사용 성공 테스트")
    void succeedUsingPoint() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(patch("/point/" + userId + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("5000"))
                .andExpect(status().isOk())
                .andExpectAll(
                        jsonPath("$.currentPoint").exists(),
                        jsonPath("$.transactionType").value("USE")
                );
    }

    /**
     * body로 입력 받은 point가 잔여 포인트 보다 클 경우 정상적으로 예외처리 하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point 사용 실패 테스트")
    void failedUsingPoint() throws Exception {
        // Given

        // When && Then
        mockMvc.perform(patch("/point/" + userId + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("9999999"))
                .andExpect(status().isInternalServerError())
                .andExpect(
                        jsonPath("$.message").value("에러가 발생했습니다.")
                );
    }
}
