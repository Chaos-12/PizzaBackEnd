package com.example.demo.application.userApplication;

import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.domain.userDomain.UserWriteRepository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserApplicationTest {
    @Autowired
    public UserWriteRepository userWriteRepository;

    @Test
    public void autowiredTest() {
        assertNotNull(userWriteRepository);
    }
}
