package com.example.demo.infraestructure.userInfraestructure;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.example.demo.core.exceptions.NotFoundException;
import com.example.demo.domain.userDomain.Role;
import com.example.demo.domain.userDomain.User;
import com.example.demo.domain.userDomain.UserWriteRepository;

import org.mindrot.jbcrypt.BCrypt;

import reactor.core.publisher.Mono;

public class UserRepositoryMock implements UserWriteRepository{

    public Map<UUID, User> idMap = new HashMap<UUID, User>();
    public Map<String, User> emailMap = new HashMap<String, User>();

    public UserRepositoryMock(){
        this.reset();
    }

    @Override
    public Mono<User> findById(UUID userId) {
        return Mono.justOrEmpty(this.idMap.get(userId))
                    .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<User> findUserByEmail(String email) {
        return Mono.justOrEmpty(this.emailMap.get(email))
                    .switchIfEmpty(Mono.error(new NotFoundException()));
    }

    @Override
    public Mono<Boolean> exists(String field) {
        return Mono.just(this.emailMap.containsKey(field));
    }

    @Override
    public Mono<User> save(User user, Boolean isNew) {
        if (isNew) {
            this.add(user);
        } else {
            this.update(user);
        }
        return Mono.just(user);
    }

    private void add(User user){
        UUID userId = user.getId();
        String email = user.getEmail();
        this.idMap.put(userId, user);
        this.emailMap.put(email, user);
    }

    private void update(User user){
        UUID userId = user.getId();
        if (!this.idMap.containsKey(userId)){
            throw new NotFoundException();
        }
        User oldUser = this.idMap.get(userId);
        this.idMap.put(userId, user);
        String oldEmail = oldUser.getEmail();
        String newEmail = user.getEmail();
        this.emailMap.remove(oldEmail);
        this.emailMap.put(newEmail, user);
    }

    public void reset(){
        this.idMap.clear();
        this.emailMap.clear();
        this.populate();
    }

    private void populate(){
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        this.add(userId, Role.ROLE_ADMIN, "admin", "istrador", "admin@app.com", "adminSecret");
        for (int i=0; i<5; i++){
            userId = UUID.fromString("10000000-0000-0000-0000-00000000000"+i);
            this.add(userId, Role.ROLE_EMPLOYEE, "empl"+i, "", "empl"+i+"@app.com", "emplPass"+i);
        }
        for (int i=0; i<10; i++){
            userId = UUID.fromString("20000000-0000-0000-0000-00000000000"+i);
            this.add(userId, Role.ROLE_CUSTOMER, "cust"+i, "surn"+i, "cust"+i+"@app.com", "custPass"+i);
        }
    }

    private void add(UUID userId, Role role, String name, String surname, String email, String password){
        User newUser = new User();
        newUser.setId(userId);
        newUser.setRole(role);
        newUser.setName(name);
        newUser.setSurname(surname);
        newUser.setEmail(email);
        newUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        newUser.setTries(User.maxRetries);
        this.add(newUser);
    }
}
