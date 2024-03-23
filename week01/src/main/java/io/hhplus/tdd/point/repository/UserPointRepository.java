package io.hhplus.tdd.point.repository;

import io.hhplus.tdd.point.UserPoint;

public interface UserPointRepository {
    UserPoint findUserPointById(Long userId);

    UserPoint save(Long userId, Long amount);
}
