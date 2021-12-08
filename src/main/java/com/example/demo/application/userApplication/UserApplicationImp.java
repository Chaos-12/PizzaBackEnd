package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.User;
import com.example.demo.domain.userDomain.UserWriteRepository;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;
import com.example.demo.security.AuthRequest;
import com.example.demo.security.AuthResponse;
import com.example.demo.security.TokenProvider;
import com.example.demo.security.UserLogInfo;

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
    private final RedisRepository<String, String> refreshTokenRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;
    private final TokenProvider tokenProvider;

    @Autowired
    public UserApplicationImp(final Logger logger, final UserWriteRepository userWriteRepository,
            final ModelMapper modelMapper, final RedisRepository<UserLogInfo, String> logInfoRepository,
            final RedisRepository<String, String> refreshTokenRepository, final TokenProvider tokenProvider) {
        super((id) -> userWriteRepository.findById(id));
        this.userWriteRepository = userWriteRepository;
        this.logInfoRepository = logInfoRepository;
        this.refreshTokenRepository = refreshTokenRepository;
        this.tokenProvider = tokenProvider;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    @Override
    public Mono<OutUserDTO> registerUser(CreateUserDTO dto, Role role) {
        User newUser = modelMapper.map(dto, User.class);
        newUser.setId(UUID.randomUUID());
        newUser.setRole(role);
        newUser.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        return newUser
                .validate("email", newUser.getEmail(), (email) -> this.userWriteRepository.exists(email))
                .then(this.userWriteRepository.save(newUser, true))
                .flatMap(dbUser -> {
                    logger.info(this.serializeObject(dbUser, "added"));
                    UserLogInfo logInfo = new UserLogInfo(dbUser.getRole());
                    return this.logInfoRepository.set(dbUser.getId().toString(), logInfo, 24);
                })
                .flatMap(hola->{
                    logger.info("LogInfo entry created in redis");
                    String refreshToken = this.tokenProvider.generateRefreshToken();
                    return this.refreshTokenRepository
                            .set(refreshToken, newUser.getId().toString(), 2)
                            .flatMap(adios -> {
                                logger.info("refreshToken entry created in redis");
                                OutUserDTO userDTO = new OutUserDTO();
                                userDTO.setRefreshToken(refreshToken);
                                userDTO.setAccessToken(this.tokenProvider.generateAccessToken(newUser));
                                return Mono.just(userDTO);
                            });
                });
    }

    public Mono<AuthResponse> login(AuthRequest userRequest){
        return userWriteRepository
                .findUserByEmail(userRequest.getEmail())
                .filter(dbUser -> BCrypt.checkpw(userRequest.getPassword(), dbUser.getPassword()))
                .map(dbUser -> {
                    AuthResponse response = new AuthResponse();
                    response.setAccessToken(tokenProvider.generateAccessToken(dbUser));
                    response.setRefreshToken(tokenProvider.generateRefreshToken());
                    return response;
                });
                
    }
}