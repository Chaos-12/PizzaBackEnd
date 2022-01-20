package com.example.demo.application.userApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.UUID;

import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.User;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepositoryMock;
import com.example.demo.infraestructure.userInfraestructure.UserRepositoryMock;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.authTokens.TokenProvider;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

@SpringBootTest
@ContextConfiguration(loader = AnnotationConfigContextLoader.class)
public class UserApplicationTest {

    @Configuration
    public static class ContextConfiguration {
        public static UserRepositoryMock userRepoMock = new UserRepositoryMock();
        public static RedisRepositoryMock<UserLogInfo, String> logInfoRepoMock = new RedisRepositoryMock<UserLogInfo, String>();
        public static RedisRepositoryMock<UUID, String> refreshTokenRepoMock = new RedisRepositoryMock<UUID, String>();

        @Bean
        public UserApplication getUserApplication() {
            TokenProvider tokenProviderMock = Mockito.mock(TokenProvider.class);
            Mockito.when(tokenProviderMock.generateRefreshToken()).thenReturn("RefreshToken");
            Mockito.when(tokenProviderMock.generateAccessToken(anyString())).thenReturn("AccessToken");
            return new UserApplicationImp(
                    userRepoMock, logInfoRepoMock, refreshTokenRepoMock,
                    tokenProviderMock, new ModelMapper());
        }
    }

    @Autowired
    public UserApplication userApp;

    @Test
    public void autowiredWorks() {
        assertNotNull(userApp);
    }


    @Test
    public void newUserSavedWithRightFieldsTest() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("MyName");
        userDTO.setSurname("MySurname");
        userDTO.setEmail("email@app.com");
        userDTO.setPassword("MySecret");
        userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
        User dbUser = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail());
        assertEquals(userDTO.getName(), dbUser.getName());
        assertEquals(userDTO.getSurname(), dbUser.getSurname());
        assertEquals(userDTO.getEmail(), dbUser.getEmail());
    }

    @Test
    public void invalidEmail() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("MyName");
        userDTO.setSurname("MySurname");
        userDTO.setEmail("email.app.com");
        userDTO.setPassword("MySecret");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void blankName() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("");
        userDTO.setSurname("MySurname");
        userDTO.setEmail("email@app.com");
        userDTO.setPassword("MySecret");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void blankSurname() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("MyName");
        userDTO.setSurname("");
        userDTO.setEmail("email@app.com");
        userDTO.setPassword("MySecret");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void blankEmail() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("MyName");
        userDTO.setSurname("MySurname");
        userDTO.setEmail(null);
        userDTO.setPassword("MySecret");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }
}
