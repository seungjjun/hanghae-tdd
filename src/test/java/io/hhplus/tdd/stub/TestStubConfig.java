package io.hhplus.tdd.stub;

import io.hhplus.tdd.point.service.PointService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("test")
public class TestStubConfig {

    @Bean
    public UserPointTableStub userPointTableStub() {
        return new UserPointTableStub();
    }

    @Bean
    public PointHistoryTableStub pointHistoryTableStub() {
        return new PointHistoryTableStub();
    }

    @Bean
    public PointService pointServiceStub() {
        return new PointServiceStub(userPointTableStub(), pointHistoryTableStub());
    }
}
