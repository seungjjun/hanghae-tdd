package com.hanghae.reservation.storage.lectureticketseller;

import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSeller;
import com.hanghae.reservation.domain.lectureticketseller.LectureTicketSellerRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public class LectureTicketSellerCoreRepository implements LectureTicketSellerRepository {
    private final LectureTicketSellerJpaRepository lectureTicketSellerJpaRepository;

    public LectureTicketSellerCoreRepository(LectureTicketSellerJpaRepository lectureTicketSellerJpaRepository) {
        this.lectureTicketSellerJpaRepository = lectureTicketSellerJpaRepository;
    }

    @Override
    @Transactional
    public LectureTicketSeller readByLectureId(Long lectureId) {
        return lectureTicketSellerJpaRepository.findByLectureId(lectureId)
                .orElseGet(() -> new LectureTicketSellerEntity(lectureId, 30L))
                .toLectureTicketSeller();
    }

    @Override
    public LectureTicketSeller updateTicketSeller(LectureTicketSeller lectureTicketSeller) {
        LectureTicketSellerEntity lectureTicketSellerEntity = lectureTicketSellerJpaRepository.findByLectureId(lectureTicketSeller.lectureId())
                .orElseGet(() -> new LectureTicketSellerEntity(lectureTicketSeller.lectureId(), lectureTicketSeller.lectureTicketNumber()));
        lectureTicketSellerEntity.updateTicketNumber(lectureTicketSeller);
        return lectureTicketSellerJpaRepository.save(lectureTicketSellerEntity).toLectureTicketSeller();
    }

    @Override
    @Transactional
    public LectureTicketSeller create(Long lectureId) {
        if (lectureTicketSellerJpaRepository.existsByLectureId(lectureId)) {
            throw new RuntimeException("해당 강의의 티켓 판매자는 이미 등록되어 있습니다.");
        }
        return lectureTicketSellerJpaRepository.save(new LectureTicketSellerEntity(lectureId, 0L)).toLectureTicketSeller();
    }
}
