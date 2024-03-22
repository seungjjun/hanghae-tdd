package io.hhplus.tdd.point;

import io.hhplus.tdd.point.service.PointService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
public class PointControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PointService pointService;

    private final Long userId = 1L;

    /**
     * @SpringBootTest 을 이용해 PointController 통합 테스트를 진행합니다.
     * 2개의 쓰레드를 생성하여 동일 유저에 대하여 2000 point 충전 후 1000 point 사용 순차 테스트를 진행합니다.
     * 처음 사용자의 point를 1000 충전 하는 api를 호출하여 충전 합니다.
     * 이후 각각의 쓰레드에서 2000 point 충전 api 호출, 1000 point 사용 api 호출 합니다.
     * 동시성 처리가 되지 않았을 경우 3000 point 또는 0 point 가 남는 상황이 발생합니다.
     * 순차적으로 처리가 되었을 경우 최종적으로 2000 point 가 남기 때문에 3000 Point를 사용하려는 api 에서는 에러가 발생해야 합니다.
     * 1000 point가 정상적으로 조회되는지 테스트 합니다.
     */
    @Test
    @DisplayName("통합 동시성 테스트")
    void integrationConcurrencyRequestTest() throws Exception {
        final int numThreads = 2;
        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numThreads);

        pointService.chargePoint(userId, 1_000L);

        executor.submit(() -> {
            try {
                mockMvc.perform(patch("/point/" + userId + "/charge")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("""
                                        {
                                            "amount": 2000
                                        }
                                        """))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                mockMvc.perform(patch("/point/" + userId + "/use")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content("1000"))
                        .andExpect(status().isOk());
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executor.shutdown();

        mockMvc.perform(patch("/point/" + userId + "/use")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("3000"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("에러가 발생했습니다."));

        mockMvc.perform(get("/point/" + userId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.point").value(2_000));
    }
}
