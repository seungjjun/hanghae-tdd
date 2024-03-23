package io.hhplus.tdd.point.dto.response;

import io.hhplus.tdd.point.PointHistory;

import java.util.List;

public record PointHistoryResponse(List<PointHistory> pointHistories) {
    public static PointHistoryResponse from(List<PointHistory> pointHistories) {
        return new PointHistoryResponse(pointHistories);
    }
}
