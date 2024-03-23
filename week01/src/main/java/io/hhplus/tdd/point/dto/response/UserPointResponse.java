package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.UserPoint;

public record UserPointResponse(Long id,
                                Long point,
                                Long updateMillis) {
    public static UserPointResponse from(UserPoint userPoint) {
        return new UserPointResponse(
                userPoint.id(),
                userPoint.point(),
                userPoint.updateMillis()
        );
    }
}
