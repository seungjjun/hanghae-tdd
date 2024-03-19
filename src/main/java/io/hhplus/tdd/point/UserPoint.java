package io.hhplus.tdd.point;

public record UserPoint(
        long id,
        long point,
        long updateMillis
) {

    public static UserPoint empty(long id) {
        return new UserPoint(id, 0, System.currentTimeMillis());
    }

    public UserPoint add(Long amount) {
        long addedPoint = point + amount;
        return new UserPoint(id, addedPoint, updateMillis);
    }

    public UserPoint minus(Long amount) {
        long deductedPoint = point - amount;
        return new UserPoint(id, deductedPoint, updateMillis);
    }

    public void verifyBalancePoint(Long amount) {
        if (this.point < amount) {
            throw new IllegalArgumentException("잔여 포인트가 부족합니다.");
        }
    }
}
