package cuit.pymjl.webfluxdemo.service.impl;

import cuit.pymjl.webfluxdemo.entity.User;
import cuit.pymjl.webfluxdemo.service.UserService;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/19 17:28
 **/
@Service
public class UserServiceImpl implements UserService {
    /**
     * 模拟数据库，存储用户信息
     */
    ConcurrentHashMap<Integer, User> map = new ConcurrentHashMap<>();

    public UserServiceImpl() {
        map.put(1, new User("pymjl", "男", 20));
        map.put(2, new User("小明", "男", 22));
        map.put(3, new User("小红", "女", 18));
        map.put(4, new User("老王", "男", 40));
        map.put(5, new User("小英", "女", 13));
        map.put(6, new User("小周", "男", 22));
        map.put(7, new User("小林", "男", 18));
    }

    @Override
    public Mono<User> getUserById(Integer id) {
        return Mono.justOrEmpty(this.map.get(id));
    }

    @Override
    public Flux<User> getAllUser() {
        return Flux.fromIterable(this.map.values());
    }

    @Override
    public Mono<Void> addUser(Mono<User> userMono) {
        return userMono.doOnNext(user -> {
            int id = map.size() + 1;
            map.put(id, user);
        }).thenEmpty(Mono.empty());
    }
}
