package io.hhplus.tdd.point;

import io.hhplus.tdd.point.dto.response.PointHistoryResponse;
import io.hhplus.tdd.point.dto.response.UserPointChargeResponse;
import io.hhplus.tdd.point.dto.response.UserPointResponse;
import io.hhplus.tdd.point.dto.response.UserPointUsingResponse;
import io.hhplus.tdd.point.dto.request.ChargeRequest;
import io.hhplus.tdd.point.service.PointService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/point")
@RestController
@RequiredArgsConstructor
public class PointController {
    private final PointService pointService;

    /**
     * TODO - 특정 유저의 포인트를 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}")
    public UserPointResponse point(@PathVariable Long id) {
        UserPoint userPoint = pointService.findPointByUserId(id);
        return UserPointResponse.from(userPoint);
    }

    /**
     * TODO - 특정 유저의 포인트 충전/이용 내역을 조회하는 기능을 작성해주세요.
     */
    @GetMapping("{id}/histories")
    public PointHistoryResponse history(@PathVariable Long id) {
        List<PointHistory> pointHistories = pointService.findHistoryByUserId(id);
        return PointHistoryResponse.from(pointHistories);
    }

    /**
     * TODO - 특정 유저의 포인트를 충전하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/charge")
    public UserPointChargeResponse charge(@PathVariable Long id, @RequestBody ChargeRequest request) {
        request.validateAmount();
        UserPoint userPoint = pointService.chargePoint(id, request.amount());
        PointHistory pointHistory = pointService.recordHistory(userPoint, request.amount(), TransactionType.CHARGE);
        return UserPointChargeResponse.of(userPoint, pointHistory);
    }

    /**
     * TODO - 특정 유저의 포인트를 사용하는 기능을 작성해주세요.
     */
    @PatchMapping("{id}/use")
    public UserPointUsingResponse use(@PathVariable Long id, @RequestBody Long amount) {
        UserPoint userPoint = pointService.usePoint(id, amount);
        PointHistory pointHistory = pointService.recordHistory(userPoint, amount, TransactionType.USE);
        return UserPointUsingResponse.of(userPoint, pointHistory);
    }
}
