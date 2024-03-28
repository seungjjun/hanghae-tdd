package com.hanghae.reservation;

import com.hanghae.reservation.api.dto.request.LectureReservationRequest;
import com.hanghae.reservation.api.dto.request.LectureTicketChargeRequest;
import com.hanghae.reservation.api.dto.request.RegisterLectureRequest;
import com.hanghae.reservation.domain.lecture.LectureReader;
import com.hanghae.reservation.domain.lecture.LectureService;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerManager;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerReader;
import com.hanghae.reservation.domain.reservation.Reservation;
import com.hanghae.reservation.domain.reservation.ReservationAppender;
import com.hanghae.reservation.domain.reservation.ReservationReader;
import com.hanghae.reservation.domain.reservation.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class IntegrationReservationTest {
    @Autowired
    private ReservationService reservationService;

    @Autowired
    private LectureService lectureService;

    @Autowired
    private LectureTicketSellerReader lectureTicketSellerReader;

    @Autowired
    private ReservationAppender reservationAppender;

    /**
     * 25명의 사용자가 동시에 특강 예약을 했을 때, 정확히 25장의 특강 티켓이 팔리고 특강 티켓의 재고가 5장이 남은것을 테스트 합니다.
     * 1. 특강을 등록합니다.
     * 2. 등록한 특강의 예약 티켓 30장을 충전합니다.
     * 3. 25명의 사용자가 동시에 같은 특강을 예약 합니다.
     * 특강 티켓 재고가 정확히 5개가 남은 것을 확인 합니다.
     * @throws InterruptedException
     */
    @Test
    @DisplayName("특강 예약 동시성 테스트")
    void concurrency_reserve_test() throws InterruptedException {
        // Given
        int numThreads = 25;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        Long baseUserId = 1L;
        Long lectureId = 1L;
        String title = "TDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 3, 27, 13, 0, 0);

        // 강의 등록
        lectureService.register(new RegisterLectureRequest(title, openTime));
        // 특강 예약 티켓(자리) 충전 (30장)
        LectureTicketSeller seller = lectureService.charge(new LectureTicketChargeRequest(lectureId, 30L));
        assertThat(seller.lectureTicketNumber()).isEqualTo(30L);

        LectureReservationRequest request = new LectureReservationRequest(lectureId, title);

        // When
        for (int i = 0; i < numThreads; i += 1) {
            Long userId = baseUserId + i;
            executor.submit(() -> {
                try {
                    reservationService.reserve(userId, request);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executor.shutdown();

        LectureTicketSeller lectureTicketSeller = lectureTicketSellerReader.readByLectureId(lectureId);

        // Then
        assertThat(lectureTicketSeller.lectureTicketNumber()).isEqualTo(5);
    }

    /**
     * Reservation Entity 생성과 LectureTicketSeller의 티켓 감소가 하나의 트랜잭션에 묶여있기 때문에 둘 중 하나가 실패하면 모두 롤백되어야 한다.
     * 1. 특강을 등록합니다.
     * 2. 등록한 특강의 예약 티켓 3장을 충전합니다.
     * 3. 2번 사용자에 대해 미리 Reservation entity를 생성하는 메서드를 호출하여 미리 등록해놓습니다.
     * 4. 3명의 사용자가 동시에 같은 특강을 예약 합니다.
     * 5. 2번 사용자는 이미 Reservation Entity가 존재하기 때문에 생성에 실패하여, 티켓 재고 감소도 롤백되어야 합니다.
     * 남은 티켓이 0장이 아닌 1장이 남은 것을 확인 합니다.
     * @throws InterruptedException
     */
    @Test
    @DisplayName("특강 예약 티켓 롤백 테스트")
    void test() throws InterruptedException {
        // Given
        int numThreads = 3;
        ExecutorService executor = Executors.newFixedThreadPool(numThreads);
        CountDownLatch latch = new CountDownLatch(numThreads);

        Long lectureId = 2L;
        String title = "DDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 3, 28, 13, 0, 0);

        lectureService.register(new RegisterLectureRequest(title, openTime));
        lectureService.charge(new LectureTicketChargeRequest(lectureId, 3L));

        LectureReservationRequest request = new LectureReservationRequest(lectureId, title);

        reservationAppender.create(2L, lectureId, title, openTime);

        // When
        executor.submit(() -> {
            try {
                reservationService.reserve(1L, request);
            } finally {
                latch.countDown();
            }
        });

        // 이전에 2번 사용자에 대해 먼저 Reservation 을 생성했기 때문에 Reservation 생성이 실패하여 티켓 감소가 롤백되어야 함
        executor.submit(() -> {
            try {
                reservationService.reserve(2L, request);
            } finally {
                latch.countDown();
            }
        });

        executor.submit(() -> {
            try {
                reservationService.reserve(3L, request);
            } finally {
                latch.countDown();
            }
        });

        latch.await();
        executor.shutdown();

        LectureTicketSeller lectureTicketSeller = lectureTicketSellerReader.readByLectureId(lectureId);

        // Then
        assertThat(lectureTicketSeller.lectureTicketNumber()).isEqualTo(1);
    }
}
