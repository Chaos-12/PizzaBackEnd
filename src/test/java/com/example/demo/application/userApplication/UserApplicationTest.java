package com.example.demo.application.userApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.UUID;

import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.User;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepositoryMock;
import com.example.demo.infraestructure.userInfraestructure.UserRepositoryMock;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.authTokens.TokenProvider;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
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
            Mockito.when(tokenProviderMock.generateAccessToken(anyString()))
                    .thenAnswer(invocation -> String.format("AccessToken,UUID:%s", invocation.getArgument(0),
                            String.class));
            Mockito.when(tokenProviderMock.generateRefreshToken())
                    .thenReturn("RefreshToken");
            return new UserApplicationImp(
                    userRepoMock, logInfoRepoMock, refreshTokenRepoMock,
                    tokenProviderMock, new ModelMapper());
        }
    }

    @Autowired
    public UserApplication userApp;

    public CreateUserDTO getValidUserDTO() {
        CreateUserDTO userDTO = new CreateUserDTO();
        userDTO.setName("MyName");
        userDTO.setSurname("MySurname");
        userDTO.setEmail("email@app.com");
        userDTO.setPassword("MySecret");
        return userDTO;
    }

    public AuthRequest getValidAuthRequest() {
        AuthRequest request = new AuthRequest();
        request.setEmail("cust0@app.com");
        request.setPassword("custPass0");
        return request;
    }

    @BeforeEach
    public void resetRepositories() {
        ContextConfiguration.userRepoMock.reset();
        ContextConfiguration.logInfoRepoMock.reset();
        ContextConfiguration.refreshTokenRepoMock.reset();
    }

    @Test
    public void injectionTest() {
        assertNotNull(userApp);
    }

    @Test
    public void newUserFieldsTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
        User dbUser = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail());
        assertEquals(userDTO.getName(), dbUser.getName());
        assertEquals(userDTO.getSurname(), dbUser.getSurname());
        assertEquals(userDTO.getEmail(), dbUser.getEmail());
    }

    @Test
    public void invalidEmailTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        userDTO.setEmail("email.app.com");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void blankNameTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        userDTO.setName("");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void blankSurnameTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        userDTO.setSurname("");
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void blankEmailTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        userDTO.setEmail(null);
        assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
    }

    @Test
    public void newUserResponseTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        AuthResponse response = userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
        assertEquals(response.getType(), "Bearer");
        assertEquals(response.getExpiration(), 60 * 60 * 1000);
        assertTrue(response.getAccessToken().contains("AccessToken"));
        assertTrue(response.getRefreshToken().contains("RefreshToken"));
    }

    @Test
    public void sameUUIDTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        String accessToken = userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block().getAccessToken();
        UUID dbUUID = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail()).getId();
        assertTrue(accessToken.contains(dbUUID.toString()));
        assertEquals(dbUUID, ContextConfiguration.logInfoRepoMock.entityMap.get(dbUUID.toString()).getId());
    }

    @Test
    public void timeLogInfo1HourTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
        UUID dbUUID = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail()).getId();
        ContextConfiguration.logInfoRepoMock.presentTime = 0.99;
        assertNotNull(ContextConfiguration.logInfoRepoMock.getFromID(dbUUID.toString()).block());
        ContextConfiguration.logInfoRepoMock.presentTime = 1.01;
        assertNull(ContextConfiguration.logInfoRepoMock.getFromID(dbUUID.toString()).block());
    }

    @Test
    public void timeRefresh2HoursTest() {
        CreateUserDTO userDTO = getValidUserDTO();
        String refreshToken = userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block().getRefreshToken();
        ContextConfiguration.refreshTokenRepoMock.presentTime = 1.99;
        assertNotNull(ContextConfiguration.refreshTokenRepoMock.getFromID(refreshToken).block());
        ContextConfiguration.refreshTokenRepoMock.presentTime = 2.01;
        assertNull(ContextConfiguration.refreshTokenRepoMock.getFromID(refreshToken).block());
    }

    @Nested
    public class LoginTest {
        @Test
        public void test() {
            assertNotNull(userApp);
        }
    }
}