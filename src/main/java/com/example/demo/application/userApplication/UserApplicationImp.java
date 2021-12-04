package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.userDomain.User;
import com.example.demo.domain.userDomain.UserWriteRepository;
import com.example.demo.infraestructure.redisInfraestructure.RedisRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserApplicationImp extends ApplicationBase<User> implements UserApplication {

    private final UserWriteRepository userWriteRepository;
    private final RedisRepository<AutenticationUser, String> redisRepositoryImp;
    private final ModelMapper modelMapper;
    private final Logger logger;

    @Autowired
    public UserApplicationImp(final Logger logger, final UserWriteRepository userWriteRepository,
            final ModelMapper modelMapper, final RedisRepository<AutenticationUser, String> redisRepositoryImp) {
        super((id) -> userWriteRepository.findById(id));
        this.userWriteRepository = userWriteRepository;
        this.redisRepositoryImp = redisRepositoryImp;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    @Override
    public Mono<UserDTO> add(CreateUserDTO dto) {
        User newUser = modelMapper.map(dto, User.class);
        newUser.validate();
        newUser.setId(UUID.randomUUID());
        newUser.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        return newUser
                .validate("email", newUser.getEmail(), (email) -> this.userWriteRepository.exists(email))
                .then(this.userWriteRepository.save(newUser, true))
                .flatMap(dbUser -> {
                    logger.info(this.serializeObject(dbUser, "added"));
                    AutenticationUser autenticationUser = new AutenticationUser(dbUser.getId());
                    return this.redisRepositoryImp.set(dbUser.getId().toString(), autenticationUser, 1);
                })
                .map(user -> {
                    logger.info(this.serializeObject(user, "added to redis"));
                    return this.modelMapper.map(user, UserDTO.class);
                });
    }
}