package com.hanghae.reservation.domain.lectureticketseller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LectureTicketSellerReaderTest {

    private LectureTicketSellerReader lectureTicketSellerReader;

    private LectureTicketSellerRepository lectureTicketSellerRepository;

    @BeforeEach
    void setUp() {
        lectureTicketSellerRepository = mock(LectureTicketSellerRepository.class);

        lectureTicketSellerReader = new LectureTicketSellerReader(lectureTicketSellerRepository);
    }

    @Test
    @DisplayName("강의 id에 해당하는 ticket seller를 찾는데 성공한다.")
    void success_find_lecture_ticket_seller() {
        // Given
        Long lectureId = 1L;
        Long lectureTicketNumber = 30L;

        LectureTicketSeller lectureTicketSeller = new LectureTicketSeller(1L, lectureId, lectureTicketNumber);

        when(lectureTicketSellerRepository.readByLectureId(lectureId)).thenReturn(lectureTicketSeller);

        // When
        LectureTicketSeller foundLectureTicketSeller = lectureTicketSellerReader.readByLectureId(lectureId);

        // Then
        assertThat(foundLectureTicketSeller.lectureId()).isEqualTo(1L);
        assertThat(foundLectureTicketSeller.lectureTicketNumber()).isEqualTo(30L);
    }

}
