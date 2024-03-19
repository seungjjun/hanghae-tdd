package io.hhplus.tdd.stub;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class UserPointTableStub extends UserPointTable {
    private Map<Long, UserPoint> table = new ConcurrentHashMap<>();

    @Override
    public UserPoint selectById(Long id) {
        return table.getOrDefault(id, UserPoint.empty(id));
    }

    @Override
    public UserPoint insertOrUpdate(long id, long amount) {
        UserPoint userPoint = new UserPoint(id, amount, System.currentTimeMillis());
        table.put(id, userPoint);
        return userPoint;
    }
}
