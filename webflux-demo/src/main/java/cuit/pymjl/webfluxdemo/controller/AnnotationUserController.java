package cuit.pymjl.webfluxdemo.controller;

import cuit.pymjl.webfluxdemo.entity.User;
import cuit.pymjl.webfluxdemo.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;

/**
 * 注解方式实现SpringWebFlux，编程方式和SpringMVC基本没区别
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/19 17:35
 **/
@RestController
@RequestMapping("/annotation")
public class AnnotationUserController {
    @Resource
    UserService userService;

    @GetMapping("/user/{id}")
    public Mono<User> getUserById(@PathVariable Integer id) {
        return userService.getUserById(id);
    }

    @GetMapping("/users")
    public Flux<User> getAllUsers() {
        return userService.getAllUser();
    }

    @PostMapping
    public Mono<Void> addUser(@RequestBody User user) {
        Mono<User> userMono = Mono.just(user);
        return userService.addUser(userMono);
    }
}
