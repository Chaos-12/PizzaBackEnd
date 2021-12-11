package com.example.demo.infraestructure.userInfraestructure;
import java.util.UUID;

import com.example.demo.core.RepositoryBase;
import com.example.demo.domain.userDomain.User;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.stereotype.Repository;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface UserRepository extends RepositoryBase<User, UUID> {

    @Query("SELECT id, name, surname, email, password, provider, role, tries FROM user WHERE email = :email;")
    public Mono<User> findUserByEmail(String email);

    @Query("SELECT id, name, surname, email, password, provider, role, tries FROM user ORDER BY role, email LIMIT :firstIndex, :limit;")
    public Flux<User> findAllFrom(int firstIndex, int limit);

    @Query("SELECT CASE WHEN COUNT(id)>0 THEN 1 ELSE 0 END FROM user WHERE email = :email;")
    public Mono<Integer> existsByField(String email);
}