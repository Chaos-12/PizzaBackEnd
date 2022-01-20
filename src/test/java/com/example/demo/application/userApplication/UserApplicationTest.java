package com.example.demo.application.userApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.example.demo.application.UserApplicationTestConfig;
import com.example.demo.domain.userDomain.UserWriteRepository;
import com.example.demo.infraestructure.userInfraestructure.UserRepositoryMock;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@SpringBootTest
@ContextConfiguration(loader=AnnotationConfigContextLoader.class)
public class UserApplicationTest {

    @Configuration
    public static class ContextConfiguration {
        
        @Bean
        public UserWriteRepository getUserRepo(){
            return new UserRepositoryMock();
        }
    }

    @Autowired
    public UserWriteRepository userWriteRepository;

    @Test
    public void autowiredTest() {
        assertNotNull(userWriteRepository);
    }

    @Test
    public void usingMock() {
        assertEquals(userWriteRepository.getClass().toString(), new UserRepositoryMock().getClass().toString());
    }
}
