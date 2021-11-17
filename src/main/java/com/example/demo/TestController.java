package com.example.demo;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/v1/test")
public class TestController {

    private final UserHandler userHandler;

    @Autowired
    public TestController(final UserHandler userHandler) {
        this.userHandler = userHandler;
    }

    @GetMapping(path = "/hello")
    public String testHello() {
        return "Hello World";
    }

    @GetMapping(path = "/mono")
    public Mono<User> testMono() {
        return Mono.just(new User("pedro", "pedro@mail.com"));
    }

    @GetMapping(path = "/repository")
    public Mono<User> createUser() {
        User user = new User("pedro", "pedro@gmail.com");
        user.setThisNew(true);
        user.setId(UUID.randomUUID());
        return this.userHandler.createUser(user);
    }
}
