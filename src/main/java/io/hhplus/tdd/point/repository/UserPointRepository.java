package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

@Repository
public class UserPointRepository {
    private final UserPointTable userPointTable;

    public UserPointRepository(UserPointTable userPointTable) {
        this.userPointTable = userPointTable;
    }

    public UserPoint findById(Long userId) {
        return userPointTable.selectById(userId);
    }
}
