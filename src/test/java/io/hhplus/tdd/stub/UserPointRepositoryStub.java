package io.hhplus.tdd.stub;

import io.hhplus.tdd.point.UserPoint;
import io.hhplus.tdd.point.repository.UserPointRepository;

import java.util.HashMap;
import java.util.Map;


public class UserPointRepositoryStub implements UserPointRepository {
    private final Map<Long, UserPoint> userPointMap = new HashMap<>();

    @Override
    public UserPoint findUserPointById(Long userId) {
        return userPointMap.getOrDefault(userId, UserPoint.empty(userId));
    }

    @Override
    public UserPoint save(Long userId, Long amount) {
        UserPoint userPoint = new UserPoint(userId, amount, 10000);
        userPointMap.put(userId, userPoint);
        return userPoint;
    }
}
