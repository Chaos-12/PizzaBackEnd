package com.example.demo.infraestructure.userInfraestructure;

import java.util.UUID;

import com.example.demo.domain.userDomain.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public class UserRepositoryImp implements UserWriteRepository, UserReadRepository {
    private final UserRepository userRepository;

    @Autowired
    public UserRepositoryImp(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Mono<User> findById(UUID id) {
        return this.userRepository.findById(id);
    }

    @Override
    public Mono<Boolean> exists(String email) {
        return Mono.sequenceEqual(this.userRepository.existsByField(email), Mono.just(1));
    }

    @Override
    public Mono<User> save(User user, Boolean isNew) {
        user.setThisNew(isNew);
        return this.userRepository.save(user);
    }

    @Override
    public Flux<UserProjection> getAll(String email) {
        return this.userRepository.findByEmail(email);
    }
}