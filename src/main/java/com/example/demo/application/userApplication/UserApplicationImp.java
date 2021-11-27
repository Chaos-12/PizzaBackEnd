package com.example.demo.application.userApplication;

import java.util.UUID;

import com.example.demo.core.ApplicationBase;
import com.example.demo.domain.userDomain.User;
import com.example.demo.domain.userDomain.UserProjection;
import com.example.demo.domain.userDomain.UserReadRepository;
import com.example.demo.domain.userDomain.UserWriteRepository;

import org.mindrot.jbcrypt.BCrypt;
import org.modelmapper.ModelMapper;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

public class UserApplicationImp extends ApplicationBase<User> implements UserApplication {

    private final UserWriteRepository userWriteRepository;
    private final UserReadRepository userReadRepository;
    private final ModelMapper modelMapper;
    private final Logger logger;

    @Autowired
    public UserApplicationImp(final Logger logger, final UserWriteRepository userWriteRepository,
            final UserReadRepository userReadRepository, final ModelMapper modelMapper) {
        super((id) -> userWriteRepository.findById(id));
        this.userWriteRepository = userWriteRepository;
        this.userReadRepository = userReadRepository;
        this.modelMapper = modelMapper;
        this.logger = logger;
    }

    @Override
    public Mono<UserDTO> add(CreateUserDTO dto) {
        User newUser = modelMapper.map(dto, User.class);
        newUser.setId(UUID.randomUUID());
        newUser.setPassword(BCrypt.hashpw(dto.getPassword(), BCrypt.gensalt()));
        return newUser
                .validate("email", newUser.getEmail(), (email) -> this.userWriteRepository.exists(email))
                .then(this.userWriteRepository.save(newUser, true))
                .map(dbUser -> {
                    logger.info(this.serializeObject(dbUser, "added"));
                    return this.modelMapper.map(dbUser, UserDTO.class);
                });
    }

    @Override
    public Mono<UserDTO> get(String id) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Mono<Void> update(String id, UpdateUserDTO dto) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Flux<UserProjection> getAll(String email) {
        return this.userReadRepository.getAll(email);
    }
}