package io.hhplus.tdd.point.service;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    public UserPoint findPointByUserId(Long userId) {
        verifyUserId(userId);

        return userPointTable.selectById(userId);
    }

    public List<PointHistory> findHistoryByUserId(Long userId) {
        verifyUserId(userId);

        return pointHistoryTable.selectAllByUserId(userId);
    }

    public UserPoint chargePoint(Long userId, Long amount) {
        verifyUserId(userId);

        UserPoint foundUserPoint = userPointTable.selectById(userId);
        final UserPoint chargedUserPoint = foundUserPoint.add(amount);
        return userPointTable.insertOrUpdate(chargedUserPoint.id(), chargedUserPoint.point());
    }

    public UserPoint usePoint(Long userId, Long amount) {
        verifyUserId(userId);

        final UserPoint foundUserPoint = userPointTable.selectById(userId);
        foundUserPoint.verifyBalancePoint(amount);

        final UserPoint usedUserPoint = foundUserPoint.minus(amount);
        return userPointTable.insertOrUpdate(usedUserPoint.id(), usedUserPoint.point());
    }

    public PointHistory recordHistory(UserPoint userPoint, Long amount, TransactionType transactionType) {
        return pointHistoryTable.insert(userPoint.id(), amount, transactionType, userPoint.updateMillis());
    }

    private void verifyUserId(Long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 아이디 입니다.");
        }
    }
}
