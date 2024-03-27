package com.hanghae.reservation.storage.lecture;

import com.hanghae.reservation.domain.lecture.Lecture;
import com.hanghae.reservation.storage.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.time.LocalDateTime;

@Entity
@Table(name = "lecture")
public class LectureEntity extends BaseEntity {
    private String title;

    private LocalDateTime openTime;

    protected LectureEntity() {
    }

    public LectureEntity(String title, LocalDateTime openTime) {
        this.title = title;
        this.openTime = openTime;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public Lecture toLecture() {
        return new Lecture(getId(), title, openTime);
    }
}
