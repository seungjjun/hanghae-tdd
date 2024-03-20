package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.database.UserPointTable;
import io.hhplus.tdd.point.UserPoint;
import org.springframework.stereotype.Repository;

public interface UserPointRepository {
    UserPoint findUserPointById(Long userId);

    UserPoint save(Long userId, Long amount);
}
