package io.hhplus.tdd.point;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UserPointTest {
    private UserPoint userPoint;

    @BeforeEach
    void setUp() {
        userPoint = new UserPoint(1L, 1_000L, System.currentTimeMillis());
    }

    /**
     * 입력받은 포인트가 정상적으로 누적되는지 테스트 합니다.
     */
    @Test
    @DisplayName("포인트를 더하는 테스트")
    void addPoint() {
        // Given
        Long chargePoint = 5_000L;

        // When
        UserPoint addedUserPoint = userPoint.add(chargePoint);

        // Then
        assertThat(addedUserPoint.point()).isEqualTo(6_000L);
    }

    /**
     * 잔여 포인트에서 사용하려는 포인트를 정상적으로 차감하는지 테스트 합니다.
     */
    @Test
    @DisplayName("포인트를 차감하는 테스트")
    void minusPoint() {
        // Given
        Long usingPoint = 1_000L;

        // When
        UserPoint usedUserPoint = userPoint.minus(usingPoint);

        // Then
        assertThat(usedUserPoint.point()).isEqualTo(0);
    }

    /**
     * 사용하려는 포인트가 잔여 포인트보다 클 경우 정상적으로 예외처리 하는지 테스트 합니다.
     */
    @Test
    @DisplayName("잔여 포인트 보다 사용하려는 포인트가 클 경우 예외 테스트")
    void verifyBalancePoint() {
        // Given
        Long usingPoint = 3_000L;

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> {
            userPoint.verifyBalancePoint(usingPoint);
        });
    }
}
