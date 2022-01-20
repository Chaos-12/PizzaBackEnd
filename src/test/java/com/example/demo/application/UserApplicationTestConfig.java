package com.example.demo.application;

import com.example.demo.domain.userDomain.UserWriteRepository;
import com.example.demo.infraestructure.userInfraestructure.UserRepositoryMock;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

@TestConfiguration
public class UserApplicationTestConfig {
    @Bean
    public UserWriteRepository getUserRepositoryMock() {
        return new UserRepositoryMock();
    }
}