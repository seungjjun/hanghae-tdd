package io.hhplus.tdd.stub;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;

import java.util.ArrayList;
import java.util.List;

public class PointHistoryTableStub extends PointHistoryTable {
    private final List<PointHistory> table = new ArrayList<>();
    private Long cursor = 1L;

    @Override
    public PointHistory insert(long id, long amount, TransactionType transactionType, long updateMillis) {
        PointHistory pointHistory = new PointHistory(cursor++, id, amount, transactionType, updateMillis);
        table.add(pointHistory);
        return pointHistory;
    }

    @Override
    public List<PointHistory> selectAllByUserId(long userId) {
        if (userId == 1L) {
            return List.of(
                    new PointHistory(cursor++, userId, 5_000L, TransactionType.CHARGE, System.currentTimeMillis()),
                    new PointHistory(cursor++, userId, 1_000L, TransactionType.USE, System.currentTimeMillis())
            );
        }

        return table;
    }
}
