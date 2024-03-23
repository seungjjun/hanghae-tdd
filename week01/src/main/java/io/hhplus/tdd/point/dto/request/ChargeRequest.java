package io.hhplus.tdd.point.dto.request;

public record ChargeRequest(Long amount) {
    public void validateAmount() {
        if (amount <= 0) {
            throw new IllegalArgumentException(amount + " 포인트는 충전할 수 없습니다.");
        }
    }
}
