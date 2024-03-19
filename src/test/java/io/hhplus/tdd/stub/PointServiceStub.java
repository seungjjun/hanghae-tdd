package io.hhplus.tdd.stub;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.service.PointService;

import java.util.List;

public class PointServiceStub extends PointService {

    public PointServiceStub(UserPointTableStub userPointTableStub, PointHistoryTableStub pointHistoryTableStub) {
        super(userPointTableStub, pointHistoryTableStub);
    }

    @Override
    public UserPoint findPointByUserId(Long userId) {
        return new UserPoint(userId, 5_000L, System.currentTimeMillis());
    }

    @Override
    public List<PointHistory> findHistoryByUserId(Long userId) {
        return List.of(
                new PointHistory(1, userId, 5_000L, TransactionType.CHARGE, System.currentTimeMillis()),
                new PointHistory(2, userId, 1_000L, TransactionType.USE, System.currentTimeMillis())
        );
    }

    @Override
    public UserPoint chargePoint(Long userId, Long amount) {
        return new UserPoint(userId, amount, System.currentTimeMillis());
    }

    @Override
    public UserPoint usePoint(Long userId, Long amount) {
        if (amount > 5000) {
            throw new IllegalArgumentException("잔여 포인트가 부족합니다.");
        }

        return new UserPoint(userId, amount, System.currentTimeMillis());
    }

    @Override
    public PointHistory recordHistory(UserPoint userPoint, Long amount, TransactionType transactionType) {
        return new PointHistory(1, userPoint.id(), amount, transactionType, System.currentTimeMillis());
    }
}
