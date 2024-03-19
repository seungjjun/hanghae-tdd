package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

public record UserPointUsingResponse(
        Long userId,
        Long currentPoint,
        TransactionType transactionType,
        Long updateMillis
) {
    public static UserPointUsingResponse of(UserPoint userPoint, PointHistory pointHistory) {
        return new UserPointUsingResponse(
                userPoint.id(),
                userPoint.point(),
                pointHistory.type(),
                pointHistory.updateMillis()
        );
    }
}
