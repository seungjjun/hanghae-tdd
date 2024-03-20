package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;

import java.util.List;

public interface PointHistoryRepository {
    List<PointHistory> findAllByUserId(Long userId);

    PointHistory save(Long userId, Long amount, TransactionType transactionType, long updateMillis);
}
