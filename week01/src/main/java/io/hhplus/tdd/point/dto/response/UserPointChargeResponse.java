package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.PointHistory;
import io.hhplus.tdd.point.TransactionType;
import io.hhplus.tdd.point.UserPoint;

public record UserPointChargeResponse(
        Long userId,
        Long currentPoint,
        TransactionType transactionType,
        Long updateMillis) {
    public static UserPointChargeResponse of(UserPoint userPoint, PointHistory pointHistory) {
        return new UserPointChargeResponse(
                userPoint.id(),
                userPoint.point(),
                pointHistory.type(),
                pointHistory.updateMillis()
        );
    }
}
