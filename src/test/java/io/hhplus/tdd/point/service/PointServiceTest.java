package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.PointService;
import io.hhplus.tdd.stub.PointHistoryTableStub;
import io.hhplus.tdd.stub.UserPointTableStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointServiceTest {
    private PointService pointService;
    private final UserPointTable userPointTable = new UserPointTableStub();
    private final PointHistoryTable pointHistoryTable = new PointHistoryTableStub();

    /**
     * UserPointTableStub, PointHistoryTableStub 객체를 사용하여 PointService 테스트 시,
     * UserPointTable, PointHistoryTable 와의 의존성을 제거하여 PointService 기능 테스트에 집중하도록 하였습니다.
     */
    @BeforeEach
    void setUp() {
        pointService = new PointService(userPointTable, pointHistoryTable);
    }

    /**
     * point를 조회하려는 유저의 id를 입력받았을 때, 해당 유저의 point를 조회하는 기능이 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("특정 유저의 UserPoint 조회")
    void findUserPointByUserId() {
        // Given
        Long userId = 1L;

        // When
        UserPoint userPoint = pointService.findPointByUserId(userId);

        // Then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(1L);
        assertThat(userPoint.point()).isEqualTo(0);
    }

    /**
     * point 이용 내역을 조회하려는 유저의 id를 입력받았을 때, 해당 유저의 이용 내역을 조회하는 기능이 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("특정 유저의 Point History 조회")
    void findHistoryByUserId() {
        // Given
        Long userId = 1L;

        // When
        List<PointHistory> pointHistories = pointService.findHistoryByUserId(userId);

        // Then
        assertThat(pointHistories).isNotNull();
        assertThat(pointHistories.get(0).id()).isEqualTo(1L);
        assertThat(pointHistories.get(0).userId()).isEqualTo(1L);
        assertThat(pointHistories.get(0).amount()).isEqualTo(5000L);
        assertThat(pointHistories.get(0).type()).isEqualTo(TransactionType.CHARGE);
    }

    /**
     * 충전하려는 포인트 값이 양수일 때, 충전 기능이 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("특정 유저의 point 충전 성공")
    void succeedChargingPoint() {
        // Given
        Long userId = 1L;
        Long amount = 5_000L;

        // When
        UserPoint userPoint = pointService.chargePoint(userId, amount);

        // Then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.id()).isEqualTo(1L);
        assertThat(userPoint.point()).isEqualTo(5_000L);
    }

    /**
     * 5000 포인트 충전 이후 2000 포인트를 충전하면 총 7000 포인트가 정상적으로 충전되는지 테스트 합니다.
     */
    @Test
    @DisplayName("point를 두번 충전시 포인트 값이 누적 된다.")
    void sequencedChargingPoint() {
        // Given
        Long userId = 1L;
        Long firstChargingAmount = 5_000L;
        Long secondChargingAmount = 2_000L;

        // When
        UserPoint userPoint1 = pointService.chargePoint(userId, firstChargingAmount);
        UserPoint userPoint2 = pointService.chargePoint(userId, secondChargingAmount);

        // Then
        assertThat(userPoint1.point()).isEqualTo(5_000L);
        assertThat(userPoint2.point()).isEqualTo(7_000L);
    }

    /**
     * 포인트 사용 기능이 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("특정 유저의 point 사용 성공")
    void succeedUsingPoint() {
        // Given
        Long userId = 2L;
        Long chargedPoint = 3_000L;
        Long usingPoint = 1_000L;

        pointService.chargePoint(userId, chargedPoint);

        // When
        UserPoint userPoint = pointService.usePoint(userId, usingPoint);

        // Then
        assertThat(userPoint).isNotNull();
        assertThat(userPoint.point()).isEqualTo(2_000L);
    }

    /**
     * 사용하려는 포인트 보다 잔여 포인트가 적을 경우 포인트 사용에 실패하고 예외 처리가 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("특정 유저의 point 사용 실패")
    void failedUsingPointWhenPointNotEnough() {
        // Given
        Long userId = 2L;
        Long amount = 5_000L;

        // When && Then
        assertThrows(IllegalArgumentException.class, () -> {
            pointService.usePoint(userId, amount);
        });
        assertThat(pointService.findPointByUserId(userId).point()).isEqualTo(0);
    }

    /**
     * 포인트 사용 및 충전 기록이 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("point 기록 테스트")
    void succeedRecordingPointHistory() {
        // Given
        UserPoint userPoint = UserPoint.empty(1L);
        Long amount = 5_000L;
        TransactionType transactionType = TransactionType.CHARGE;

        // When
        PointHistory pointHistory = pointService.recordHistory(userPoint, amount, transactionType);

        // Then
        assertThat(pointHistory.amount()).isEqualTo(5_000L);
        assertThat(pointHistory.type()).isEqualTo(TransactionType.CHARGE);
    }
}
