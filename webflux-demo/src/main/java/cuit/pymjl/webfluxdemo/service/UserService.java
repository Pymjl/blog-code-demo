package cuit.pymjl.webfluxdemo.service;

import cuit.pymjl.webfluxdemo.entity.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/19 17:25
 **/
public interface UserService {
    /**
     * 添加用户
     *
     * @param userMono 用户mono
     * @return {@code Mono<Void>}
     */
    Mono<Void> addUser(Mono<User> userMono);

    /**
     * 通过id查询用户
     *
     * @param id id
     * @return {@code Mono<User>}
     */
    Mono<User> getUserById(Integer id);

    /**
     * 得到所有用户
     *
     * @return {@code Flux<User>}
     */
    Flux<User> getAllUser();
}
