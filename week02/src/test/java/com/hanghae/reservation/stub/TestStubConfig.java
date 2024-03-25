package com.hanghae.reservation.stub;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Profile("test")
@Configuration
public class TestStubConfig {
    @Bean
    public ReservationServiceStub reservationServiceStub() {
        return new ReservationServiceStub();
    }
}
