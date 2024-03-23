package io.hhplus.tdd.point.service;

import io.hhplus.tdd.LockHandler;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.PointHistoryRepository;
import io.hhplus.tdd.point.repository.UserPointRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService implements PointServiceUseCase {

    private final UserPointRepository userPointRepository;
    private final PointHistoryRepository pointHistoryRepository;
    private final LockHandler lockHandler;

    @Override
    public UserPoint findPointByUserId(Long userId) {
        verifyUserId(userId);
        return userPointRepository.findUserPointById(userId);
    }

    @Override
    public List<PointHistory> findHistoryByUserId(Long userId) {
        verifyUserId(userId);
        return pointHistoryRepository.findAllByUserId(userId).stream()
                .sorted(Comparator.comparingLong(PointHistory::updateMillis).reversed())
                .collect(Collectors.toList());
    }

    @Override
    public UserPoint chargePoint(Long userId, Long amount) throws Exception {
        return lockHandler.withLock(userId, () -> {
            UserPoint foundUserPoint = userPointRepository.findUserPointById(userId);
            final UserPoint chargedUserPoint = foundUserPoint.add(amount);

            return userPointRepository.save(chargedUserPoint.id(), chargedUserPoint.point());
        });
    }

    @Override
    public UserPoint usePoint(Long userId, Long amount) throws Exception {
        verifyUserId(userId);

        return lockHandler.withLock(userId, () -> {
            final UserPoint foundUserPoint = userPointRepository.findUserPointById(userId);
            foundUserPoint.verifyBalancePoint(amount);
            final UserPoint usedUserPoint = foundUserPoint.minus(amount);
            return userPointRepository.save(usedUserPoint.id(), usedUserPoint.point());
        });
    }

    @Override
    public PointHistory recordHistory(UserPoint userPoint, Long amount, TransactionType transactionType) throws Exception {
        return lockHandler.withLock(userPoint.id(), () -> pointHistoryRepository.save(userPoint.id(), amount, transactionType, userPoint.updateMillis()));
    }

    private void verifyUserId(Long userId) {
        if (userId <= 0) {
            throw new IllegalArgumentException("유효하지 않은 사용자 아이디 입니다.");
        }
    }
}
