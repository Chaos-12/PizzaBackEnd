package com.example.demo;

import java.io.UnsupportedEncodingException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
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
        return Mono.just(new User(33, "pedro", "pedro@mail.com"));
    }

    @GetMapping(path = "/repository")
    public Mono<User> get() {
        User user = new User(33, "pedro", "pedro@gmail.com");
        return userHandler.add(user).flatMap(this.userHandler::add);
    }
    /*
     * @ExceptionHandler({ UnsupportedEncodingException.class }) public
     * ResponseEntity<String> exceptionHandler(Exception ex) { return
     * ResponseEntity.badRequest().body(ex.getMessage()); }
     */
}
