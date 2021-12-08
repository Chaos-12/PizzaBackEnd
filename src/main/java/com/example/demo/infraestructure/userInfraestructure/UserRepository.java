package com.example.demo.infraestructure.userInfraestructure;
import java.util.UUID;

import com.example.demo.domain.userDomain.User;
import com.example.demo.domain.userDomain.UserProjection;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends ReactiveCrudRepository<User, UUID> {
    
    @Query("SELECT id, name, surname, email FROM user WHERE (email LIKE CONCAT('%', :email, '%')) ORDER BY email;")
    Flux<UserProjection> findByEmail(String email);

    @Query("SELECT id, name, surname, email, password, rol, provider FROM user WHERE email = :email;")
    Mono<User> findUserByEmail(String email);

    @Query("SELECT CASE WHEN COUNT(id)>0 THEN 1 ELSE 0 END FROM user WHERE email = :email;")
    Mono<Integer> existsByField(String email);
}
