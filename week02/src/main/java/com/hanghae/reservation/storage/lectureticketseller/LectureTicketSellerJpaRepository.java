package com.hanghae.reservation.storage.lectureticketseller;

import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.Optional;

public interface LectureTicketSellerJpaRepository extends JpaRepository<LectureTicketSellerEntity, Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<LectureTicketSellerEntity> findByLectureId(Long lectureId);

    boolean existsByLectureId(Long lectureId);
}
