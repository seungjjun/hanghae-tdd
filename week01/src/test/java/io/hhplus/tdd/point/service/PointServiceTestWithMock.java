package io.hhplus.tdd.point.service;

import io.hhplus.tdd.LockHandler;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.point.repository.UserPointRepositoryImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.BDDMockito.given;

public class PointServiceTestWithMock {
    private PointService pointService;
    private UserPointRepository userPointRepository;
    private PointHistoryRepository pointHistoryRepository;

    @BeforeEach
    void setUp() {
        userPointRepository = mock(UserPointRepositoryImpl.class);
        pointHistoryRepository = mock(PointHistoryRepository.class);
        LockHandler lockHandler = new LockHandler();

        pointService = new PointService(userPointRepository, pointHistoryRepository, lockHandler);
    }

    @Test
    @DisplayName("특정 유저의 UserPoint 조회")
    void findUserPointByUserId() {
        // Given
        Long userId = 1L;

        given(userPointRepository.findUserPointById(anyLong()))
                .willReturn(new UserPoint(userId, 0, System.currentTimeMillis()));

        // When
        UserPoint userPoint = pointService.findPointByUserId(userId);

        // Then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(1L);
        assertThat(userPoint.point()).isEqualTo(0);
    }

    @Test
    @DisplayName("특정 유저의 point 충전 성공")
    void succeedChargingPoint() throws Exception {
        // Given
        Long userId = 1L;
        Long amount = 5_000L;

        UserPoint foundUserPoint = new UserPoint(userId, 0, System.currentTimeMillis());
        given(userPointRepository.findUserPointById(anyLong()))
                .willReturn(foundUserPoint);

        UserPoint chargedUserPoint = new UserPoint(userId, foundUserPoint.point() + amount, System.currentTimeMillis());
        given(userPointRepository.save(anyLong(), any()))
                .willReturn(chargedUserPoint);

        // When
        UserPoint userPoint = pointService.chargePoint(userId, amount);

        // Then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(1L);
        assertThat(userPoint.point()).isEqualTo(chargedUserPoint.point());
    }
}
