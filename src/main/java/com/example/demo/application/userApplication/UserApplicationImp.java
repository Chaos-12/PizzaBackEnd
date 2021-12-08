package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.core.exceptions.BadRequestException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.User;
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
    private final RedisRepository<UserLogInfo, String> logInfoRepository;
    private final RedisRepository<UUID, String> refreshTokenRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserApplicationImp(final Logger logger, final UserWriteRepository userWriteRepository,
            final ModelMapper modelMapper, final RedisRepository<UserLogInfo, String> logInfoRepository,
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
    public Mono<AuthResponse> registerUser(CreateUserDTO dto, Role role) {
        User newUser = this.modelMapper.map(dto, User.class);
        newUser.setId(UUID.randomUUID());
        newUser.setRole(role);
        newUser.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        AuthResponse response = new AuthResponse();
        response.setAccessToken(this.tokenProvider.generateAccessToken(newUser));
        response.setRefreshToken(this.tokenProvider.generateRefreshToken());
        return newUser
                .validate("email", newUser.getEmail(), (email) -> this.userWriteRepository.exists(email))
                .then(this.userWriteRepository.save(newUser, true))
                .flatMap(dbUser -> {
                    logger.info(this.serializeObject(dbUser, "added"));
                    UserLogInfo logInfo = new UserLogInfo(dbUser.getRole());
                    return this.logInfoRepository.set(dbUser.getId().toString(), logInfo, 24);
                })
                .then(this.refreshTokenRepository.set(response.getRefreshToken(), newUser.getId(), 2))
                .thenReturn(response);
    }

    public Mono<AuthResponse> login(AuthRequest userRequest) {
        return this.userWriteRepository
                .findUserByEmail(userRequest.getEmail())
                .filter(dbUser -> dbUser.validate(userRequest.getPassword()))
                .flatMap(dbUser -> {
                    logger.info(this.serializeObject(dbUser, "login"));
                    return generateResponse(dbUser);
                });
    }

    private Mono<AuthResponse> generateResponse(User user){
        AuthResponse response = new AuthResponse();
        response.setAccessToken(tokenProvider.generateAccessToken(user));
        response.setRefreshToken(tokenProvider.generateRefreshToken());
        return this.logInfoRepository
                    .getFromString(user.getId().toString())
                    .switchIfEmpty(this.logInfoRepository.set(user.getId().toString(), new UserLogInfo(user.getRole()), 24))
                    .then(this.refreshTokenRepository.set(response.getRefreshToken(), user.getId(), 2))
                    .thenReturn(response);
    }
}