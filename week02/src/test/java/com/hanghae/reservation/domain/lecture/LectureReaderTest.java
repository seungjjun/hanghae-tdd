package com.hanghae.reservation.domain.lecture;

import com.hanghae.reservation.stub.LectureRepositoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

class LectureReaderTest {

    private LectureReader lectureReader;
    private LectureRepository lectureRepository;

    @BeforeEach
    void setUp() {
        lectureRepository = new LectureRepositoryStub();

        lectureReader = new LectureReader(lectureRepository);
    }

    @Test
    void success_find_lecture() {
        // Given
        Long lectureId = 1L;
        String title = "TDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 4, 20, 13, 0, 0);

        lectureRepository.create(title, openTime);

        // When
        Lecture lecture = lectureReader.read(lectureId);

        // Then
        assertThat(lecture).isNotNull();
        assertThat(lecture.title()).isEqualTo("TDD");
    }
}
