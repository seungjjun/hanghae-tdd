package com.hanghae.reservation.domain.lecture;

import com.hanghae.reservation.stub.LectureRepositoryStub;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

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
    @DisplayName("강의를 찾는데 성공한다.")
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
        assertThat(lecture.openTime()).isEqualTo(LocalDateTime.of(2024, 4, 20, 13, 0, 0));
    }

    @Test
    @DisplayName("등록된 모든 강의를 불러온다.")
    void success_find_lecture_list() {
        // Given
        String title = "TDD";
        String otherTitle = "DDD";
        LocalDateTime openTime = LocalDateTime.of(2024, 4, 20, 13, 0, 0);

        lectureRepository.create(title, openTime);
        lectureRepository.create(otherTitle, openTime);

        // When
        List<Lecture> lectures = lectureReader.readAll();

        // Then
        assertThat(lectures.size()).isEqualTo(2);
        assertThat(lectures.getFirst().title()).isEqualTo("TDD");
        assertThat(lectures.getLast().title()).isEqualTo("DDD");
    }
}
