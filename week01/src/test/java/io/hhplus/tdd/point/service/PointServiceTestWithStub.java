package io.hhplus.tdd.point.service;

import io.hhplus.tdd.LockHandler;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import io.hhplus.tdd.stub.PointHistoryRepositoryStub;
import io.hhplus.tdd.stub.UserPointRepositoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PointServiceTestWithStub {
    private PointService pointService;

    /**
     * UserPointRepositoryStub, PointHistoryRepositoryStub 객체를 사용하여 PointService 테스트 시,
     * UserPointTable, PointHistoryTable 와의 의존성을 제거하여 PointService 기능 테스트에 집중하도록 하였습니다.
     */
    @BeforeEach
    void setUp() {
        UserPointRepository userPointRepository = new UserPointRepositoryStub();
        PointHistoryRepository pointHistoryRepository = new PointHistoryRepositoryStub();
        LockHandler lockHandler = new LockHandler();

        pointService = new PointService(userPointRepository, pointHistoryRepository, lockHandler);
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
        assertThat(pointHistories.get(0).id()).isEqualTo(2);
        assertThat(pointHistories.get(0).userId()).isEqualTo(1L);
        assertThat(pointHistories.get(0).amount()).isEqualTo(1000L);
        assertThat(pointHistories.get(0).type()).isEqualTo(TransactionType.USE);
    }

    @Test
    @DisplayName("point history를 업데이트 순으로 조회한다.")
    void findSortedHistories() {
        // Given
        Long userId = 1L;

        // When
        List<PointHistory> pointHistories = pointService.findHistoryByUserId(userId);

        // Then
        assertThat(pointHistories.get(0).amount()).isEqualTo(1_000L);
        assertThat(pointHistories.get(1).amount()).isEqualTo(5_000L);
    }

    /**
     * 충전하려는 포인트 값이 양수일 때, 충전 기능이 정상 작동하는지 테스트 합니다.
     */
    @Test
    @DisplayName("특정 유저의 point 충전 성공")
    void succeedChargingPoint() throws Exception {
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
    void sequencedChargingPoint() throws Exception {
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
    void succeedUsingPoint() throws Exception {
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
    void succeedRecordingPointHistory() throws Exception {
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

    @RepeatedTest(100)
    @DisplayName("동시에 100 포인트씩 사용했을 때, 각 thread 별로 100포인트 씩 사용하는지 확인")
    void use100PointByConcurrency() throws Exception {
        Long userId = 2L;
        Long amount = 100L;
        int numThreads = 100;
        Long totalPoint = amount * numThreads; // 10,000 Point

        UserPoint chargedUserPoint = pointService.chargePoint(userId, totalPoint); // 각 thread 마다 100 point 씩 사용 하기 위해 총 10000 point 충전

        assertThat(chargedUserPoint.point()).isEqualTo(totalPoint);

        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numThreads);

        for (int i = 0; i < numThreads; i += 1) {
            executor.submit(() -> {
                try {
                    UserPoint userPoint = pointService.usePoint(userId, amount);
                    pointService.recordHistory(userPoint, amount, TransactionType.CHARGE);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        assertThat(pointService.findHistoryByUserId(userId).size()).isEqualTo(100);
        assertThat(pointService.findPointByUserId(userId).point()).isEqualTo(0);
    }

    /**
     * 처음 1000 충전 : initial point -> 1000 Point
     * 2개의 Thread 에서 동시에 각각 500 Point 충전, 1000 Point 사용
     * 예상 동작
     * Thread 1 : 1000 + 500 = 1500 point
     * Thread 2 : 1500 - 1000 = 500 point
     * 만일 동시성이 처리 되지 않으면 1500 point 또는 0 point 가 남는 상황이 발생한다.
     */
    @RepeatedTest(100)
    @DisplayName("순차 충전/사용 요청 테스트")
    void sequenceChargeAndUsePoint() throws Exception {
        final Long userId = 1L;
        final int numThreads = 2;

        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numThreads);

        pointService.chargePoint(userId, 1000L);

        executor.submit(() -> {
            try {
                pointService.chargePoint(userId, 500L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                pointService.usePoint(userId, 1000L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                latch.countDown();
            }
        });

        latch.await();

        Long expectedPoint = 1000L + 500L - 1000L;

        UserPoint result = pointService.findPointByUserId(userId);
        assertThat(result.point()).isEqualTo(expectedPoint);
    }

    /**
     * 처음 4000 충전 : initial point -> 4000 Point
     * 2개의 각 Thread 에서 동시에 1000 포인트 충전 -> 3000 포인트 사용 순으로 메서드 호출
     * 예상 동작
     * Thread 1 : 4000 + 1000 - 3000 = 2000 point
     * Thread 2 : 2000 + 1000 - 3000 = 0 point
     * 만일 동시성이 처리 되지 않으면 2000 point 가 남는 상황이 발생한다.
     */
    @RepeatedTest(100)
    @DisplayName("동시 요청 테스트")
    void concurrencyChargeAndUsePoint() throws Exception {
        final Long userId = 3L;
        final int numThreads = 2;

        final ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        final CountDownLatch latch = new CountDownLatch(numThreads);

        pointService.chargePoint(userId, 4_000L);

        for (int i = 0; i < numThreads; i += 1) {
            executor.submit(() -> {
                try {
                    pointService.chargePoint(userId, 1_000L);
                    pointService.usePoint(userId, 3_000L);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        Long expectedPoint = 4000L + 1000L - 3000L + 1000L - 3000L;

        UserPoint result = pointService.findPointByUserId(userId);
        assertThat(result.point()).isEqualTo(expectedPoint);
    }
}
