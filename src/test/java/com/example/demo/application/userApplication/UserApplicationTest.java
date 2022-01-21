package com.example.demo.application.userApplication;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;

import java.util.UUID;

import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.core.exceptions.UnauthorizedException;
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
import org.mindrot.jbcrypt.BCrypt;
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

    @Nested
    public class CreateUserTest {
        public CreateUserDTO getValidUserDTO() {
            CreateUserDTO userDTO = new CreateUserDTO();
            userDTO.setName("MyName");
            userDTO.setSurname("MySurname");
            userDTO.setEmail("email@app.com");
            userDTO.setPassword("MySecret");
            return userDTO;
        }

        @Test
        public void fieldsAreSaved() {
            CreateUserDTO userDTO = getValidUserDTO();
            userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
            User dbUser = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail());
            assertEquals(userDTO.getName(), dbUser.getName());
            assertEquals(userDTO.getSurname(), dbUser.getSurname());
            assertEquals(userDTO.getEmail(), dbUser.getEmail());
        }

        @Test
        public void invalidEmail() {
            CreateUserDTO userDTO = getValidUserDTO();
            userDTO.setEmail("email.app.com");
            assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
        }

        @Test
        public void blankEmail() {
            CreateUserDTO userDTO = getValidUserDTO();
            userDTO.setEmail(null);
            assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
        }

        @Test
        public void blankName() {
            CreateUserDTO userDTO = getValidUserDTO();
            userDTO.setName("");
            assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
        }

        @Test
        public void blankSurname() {
            CreateUserDTO userDTO = getValidUserDTO();
            userDTO.setSurname("");
            assertThrows(BadRequestException.class, () -> userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block());
        }

        @Test
        public void responseFormat() {
            CreateUserDTO userDTO = getValidUserDTO();
            AuthResponse response = userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
            assertEquals(response.getType(), "Bearer");
            assertEquals(response.getExpiration(), 60 * 60 * 1000);
            assertTrue(response.getAccessToken().contains("AccessToken"));
            assertTrue(response.getRefreshToken().contains("RefreshToken"));
        }

        @Test
        public void accessTokenHasRightUUID() {
            CreateUserDTO userDTO = getValidUserDTO();
            String accessToken = userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block().getAccessToken();
            UUID dbUUID = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail()).getId();
            assertTrue(accessToken.contains(dbUUID.toString()));
        }

        @Test
        public void timeLogInfo1Hour() {
            CreateUserDTO userDTO = getValidUserDTO();
            userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block();
            UUID dbUUID = ContextConfiguration.userRepoMock.emailMap.get(userDTO.getEmail()).getId();
            ContextConfiguration.logInfoRepoMock.presentTime = 0.99;
            assertNotNull(ContextConfiguration.logInfoRepoMock.getFromID(dbUUID.toString()).block());
            ContextConfiguration.logInfoRepoMock.presentTime = 1.01;
            assertNull(ContextConfiguration.logInfoRepoMock.getFromID(dbUUID.toString()).block());
        }

        @Test
        public void timeRefresh2Hours() {
            CreateUserDTO userDTO = getValidUserDTO();
            String refreshToken = userApp.registerNewUser(userDTO, Role.ROLE_CUSTOMER).block().getRefreshToken();
            ContextConfiguration.refreshTokenRepoMock.presentTime = 1.99;
            assertNotNull(ContextConfiguration.refreshTokenRepoMock.getFromID(refreshToken).block());
            ContextConfiguration.refreshTokenRepoMock.presentTime = 2.01;
            assertNull(ContextConfiguration.refreshTokenRepoMock.getFromID(refreshToken).block());
        }
    }

    @Nested
    public class UpdateUserTest {

        public User getValidCustomer() {
            return ContextConfiguration.userRepoMock.idMap.get(UUID.fromString("20000000-0000-0000-0000-00000000000"));
        }

        public String getValidPassword() {
            return "custPass0";
        }

        @Test
        public void wrongId() {
            assertThrows(NotFoundException.class,
                    () -> userApp.updateUser(UUID.randomUUID().toString(), new UpdateUserDTO()).block());
        }

        @Test
        public void wrongPassword() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword("notMyPassword");
            assertThrows(UnauthorizedException.class,
                    () -> userApp.updateUser(oldUser.getId().toString(), userDto).block());
        }

        @Test
        public void duplicatedEmail() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword(getValidPassword());
            userDto.setEmail("admin@app.com");
            assertThrows(BadRequestException.class,
                    () -> userApp.updateUser(oldUser.getId().toString(), userDto).block());
        }

        @Test
        public void updateName() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setName("MyNewName");
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(userDto.getName(), newUser.getName());
        }

        @Test
        public void notUpdateName() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(oldUser.getName(), newUser.getName());
        }

        @Test
        public void updateSurname() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setSurname("MyNewSurname");
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(userDto.getSurname(), newUser.getSurname());
        }

        @Test
        public void notUpdateSurname() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(oldUser.getSurname(), newUser.getSurname());
        }

        @Test
        public void updateEmail() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setEmail("newEmail@app.com");
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(userDto.getEmail(), newUser.getEmail());
        }

        @Test
        public void notUpdateEmail() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(oldUser.getEmail(), newUser.getEmail());
        }

        @Test
        public void updatePassword() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setNewPassword("MyNewPassword");
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertTrue(newUser.validate(userDto.getNewPassword()));
        }

        @Test
        public void notUpdatePassword() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(oldUser.getPassword(), newUser.getPassword());
        }

        @Test
        public void updateRole() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setRole(Role.ROLE_EMPLOYEE);
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(userDto.getRole(), newUser.getRole());
        }

        @Test
        public void notUpdateRole() {
            User oldUser = getValidCustomer();
            UpdateUserDTO userDto = new UpdateUserDTO();
            userDto.setPassword(getValidPassword());
            userApp.updateUser(oldUser.getId().toString(), userDto).block();
            User newUser = ContextConfiguration.userRepoMock.idMap.get(oldUser.getId());
            assertEquals(oldUser.getRole(), newUser.getRole());
        }
    }

    @Nested
    public class LoginTest {

        public AuthRequest getValidAuthRequest() {
            AuthRequest request = new AuthRequest();
            request.setEmail("cust0@app.com");
            request.setPassword("custPass0");
            return request;
        }

        @Test
        public void loginSuccess() {
            AuthRequest request = getValidAuthRequest();
            assertNotNull(userApp.login(request).block());
            UUID userId = ContextConfiguration.userRepoMock.emailMap.get(request.getEmail()).getId();
            assertNotNull(ContextConfiguration.logInfoRepoMock.getFromID(userId.toString()).block());
        }

        @Test
        public void logoutSuccess() {
            AuthRequest request = getValidAuthRequest();
            assertNotNull(userApp.login(request).block());
            UUID userId = ContextConfiguration.userRepoMock.emailMap.get(request.getEmail()).getId();
            assertNotNull(ContextConfiguration.logInfoRepoMock.getFromID(userId.toString()).block());
            userApp.logout(userId).block();
            assertNull(ContextConfiguration.logInfoRepoMock.getFromID(userId.toString()).block());
        }

        @Test
        public void emailNotInRepository() {
            AuthRequest request = getValidAuthRequest();
            request.setEmail("wrong@app.com");
            assertThrows(NotFoundException.class, () -> userApp.login(request).block());
        }

        @Test
        public void noEmail() {
            AuthRequest request = getValidAuthRequest();
            request.setEmail(null);
            assertThrows(NotFoundException.class, () -> userApp.login(request).block());
        }

        @Test
        public void wrongPassword() {
            AuthRequest request = getValidAuthRequest();
            request.setPassword("NotMyPassword");
            assertThrows(BadRequestException.class, () -> userApp.login(request).block());
            User dbUser = ContextConfiguration.userRepoMock.emailMap.get(request.getEmail());
            assertEquals(dbUser.getTries(), User.maxRetries - 1);
        }

        @Test
        public void noPassword() {
            AuthRequest request = getValidAuthRequest();
            request.setPassword(null);
            assertThrows(BadRequestException.class, () -> userApp.login(request).block());
            User dbUser = ContextConfiguration.userRepoMock.emailMap.get(request.getEmail());
            assertEquals(dbUser.getTries(), User.maxRetries - 1);
        }

        @Test
        public void responseFormat() {
            AuthRequest request = getValidAuthRequest();
            AuthResponse response = userApp.login(request).block();
            assertEquals(response.getType(), "Bearer");
            assertEquals(response.getExpiration(), 60 * 60 * 1000);
            assertTrue(response.getAccessToken().contains("AccessToken"));
            assertTrue(response.getRefreshToken().contains("RefreshToken"));
        }

        @Test
        public void accessTokenHasRightUUID() {
            AuthRequest request = getValidAuthRequest();
            UUID userID = ContextConfiguration.userRepoMock.emailMap.get(request.getEmail()).getId();
            String accessToken = userApp.login(request).block().getAccessToken();
            assertTrue(accessToken.contains(userID.toString()));
        }

        @Test
        public void timeLogInfo1Hour() {
            AuthRequest request = getValidAuthRequest();
            userApp.login(request).block();
            UUID dbUUID = ContextConfiguration.userRepoMock.emailMap.get(request.getEmail()).getId();
            ContextConfiguration.logInfoRepoMock.presentTime = 0.99;
            assertNotNull(ContextConfiguration.logInfoRepoMock.getFromID(dbUUID.toString()).block());
            ContextConfiguration.logInfoRepoMock.presentTime = 1.01;
            assertNull(ContextConfiguration.logInfoRepoMock.getFromID(dbUUID.toString()).block());
        }

        @Test
        public void timeRefresh2Hours() {
            AuthRequest request = getValidAuthRequest();
            String refreshToken = userApp.login(request).block().getRefreshToken();
            ContextConfiguration.refreshTokenRepoMock.presentTime = 1.99;
            assertNotNull(ContextConfiguration.refreshTokenRepoMock.getFromID(refreshToken).block());
            ContextConfiguration.refreshTokenRepoMock.presentTime = 2.01;
            assertNull(ContextConfiguration.refreshTokenRepoMock.getFromID(refreshToken).block());
        }
    }

}