package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.core.exceptions.UnauthorizedException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.User;
import com.example.demo.domain.userDomain.UserDTO;
import com.example.demo.domain.userDomain.UserWriteRepository;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;
import com.example.demo.security.UserLogInfo;
import com.example.demo.security.tokens.TokenProvider;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationImp extends ApplicationBase<User> implements UserApplication {

    private final UserWriteRepository userWriteRepository;
    private final RedisRepository<UserLogInfo, UUID> logInfoRepository;
    private final RedisRepository<UUID, String> refreshTokenRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserApplicationImp(final Logger logger, final UserWriteRepository userWriteRepository,
            final ModelMapper modelMapper, final RedisRepository<UserLogInfo, UUID> logInfoRepository,
            final RedisRepository<UUID, String> refreshTokenRepository, final TokenProvider tokenProvider) {
        super((id) -> userWriteRepository.findById(id));
        this.userWriteRepository = userWriteRepository;
        this.logInfoRepository = logInfoRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    @Override
    public Mono<AuthResponse> registerNewUser(CreateUserDTO dto, Role role) {
        User newUser = this.modelMapper.map(dto, User.class);
        newUser.setId(UUID.randomUUID());
        newUser.resetTries();
        newUser.setRole(role);
        newUser.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        return newUser
                .validate("email", newUser.getEmail(), (email) -> this.userWriteRepository.exists(email))
                .then(this.userWriteRepository.save(newUser, true))
                .flatMap(dbUser -> {
                    logger.info(this.serializeObject(dbUser, "added"));
                    return this.generateResponse(dbUser);
                });
    }

    public Mono<AuthResponse> login(AuthRequest userRequest) {
        return this.userWriteRepository
                .findUserByEmail(userRequest.getEmail())
                .flatMap(dbUser -> {
                    if(dbUser.validate(userRequest.getPassword())){
                        logger.info(this.serializeObject(dbUser, "login"));
                        return generateResponse(dbUser);
                    } else {
                        logger.info(dbUser.toString().concat(" login failed: wrong password"));
                        return this.userWriteRepository.save(dbUser, false)
                                .then(Mono.error(new BadRequestException("Wrong password")));
                    }
                });
    }

    public Mono<AuthResponse> refresh(String refreshToken) {
        return this.refreshTokenRepository
                        .getFromID(refreshToken)
                        .switchIfEmpty(Mono.error(new NotFoundException("Refresh token not found")))
                        .flatMap(id -> this.logInfoRepository.getFromID(id))
                        .switchIfEmpty(Mono.error(new UnauthorizedException("User needs to log in")))
                        .flatMap(logInfo -> {
                            if (logInfo.userHasUsed(refreshToken)) {
                                return this.logout(logInfo.getId())
                                            .then(Mono.error(new UnauthorizedException("Token used multiple times: forced logout")));
                            } else {
                                logInfo.addRefreshToken(refreshToken);
                                return this.logInfoRepository.set(logInfo.getId(), logInfo, 2);
                            }
                        })
                        .flatMap(logInfo -> this.generateResponse(logInfo));
    }

    public Mono<Boolean> logout(UUID id) {
        return this.logInfoRepository.removeFromID(id);
    }

    
    public Mono<Void> resetTries(String userId){
        return this.userWriteRepository
                        .findById(ApplicationBase.getUUIDfrom(userId))
                        .flatMap(dbUser -> {
                            dbUser.resetTries();
                            return this.userWriteRepository.save(dbUser, false);
                        })
                        .then();
    }

    public Mono<UserDTO> getProfile(UUID id) {
        return this.userWriteRepository
                        .findById(id)
                        .map(dbUser -> this.modelMapper.map(dbUser, UserDTO.class));
    }

    private Mono<AuthResponse> generateResponse(UUID id, Role role) {
        AuthResponse response = new AuthResponse();
        response.setAccessToken(tokenProvider.generateAccessToken(id.toString()));
        response.setRefreshToken(tokenProvider.generateRefreshToken());
        return this.logInfoRepository
                        .getFromID(id)
                        .switchIfEmpty(this.logInfoRepository.set(id, new UserLogInfo(id, role), 24))
                        .then(this.refreshTokenRepository.set(response.getRefreshToken(), id, 2))
                        .thenReturn(response);
    }
    
    private Mono<AuthResponse> generateResponse(User user) {
        return this.generateResponse(user.getId(), user.getRole());
    }

    private Mono<AuthResponse> generateResponse(UserLogInfo logInfo) {
        return this.generateResponse(logInfo.getId(), logInfo.getRole());
    }
}