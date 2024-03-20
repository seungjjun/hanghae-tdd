package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointRepositoryImpl implements UserPointRepository {
    private final UserPointTable userPointTable;

    public UserPointRepositoryImpl(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    @Override
    public UserPoint findUserPointById(Long userId) {
        return userPointTable.selectById(userId);
    }

    @Override
    public UserPoint save(Long userId, Long amount) {
        return userPointTable.insertOrUpdate(userId, amount);
    }
}
