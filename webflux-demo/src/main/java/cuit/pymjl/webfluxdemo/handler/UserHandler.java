package cuit.pymjl.webfluxdemo.handler;

import cuit.pymjl.webfluxdemo.entity.User;
import cuit.pymjl.webfluxdemo.service.UserService;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/19 18:09
 **/
public class UserHandler {
    private final UserService userService;

    public UserHandler(UserService userService) {
        this.userService = userService;
    }

    /**
     * 根据ID获取用户信息
     *
     * @param request 请求
     * @return {@code Mono<ServerResponse>}
     */
    public Mono<ServerResponse> getUserById(ServerRequest request) {
        //获取ID值
        int userId = Integer.parseInt(request.pathVariable("id"));
        //空值处理
        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        //调用Service
        Mono<User> userMono = this.userService.getUserById(userId);
        //把userMono进行转换返回 ,使用Reactor操作符flatMap
        return userMono.flatMap(user ->
                ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).bodyValue(user))
                .switchIfEmpty(notFound);
    }

    /**
     * 得到所有用户
     *
     * @return {@code Mono<ServerResponse>}
     */
    public Mono<ServerResponse> getAllUsers(ServerRequest request) {
        //调用service得到结果
        Flux<User> users = this.userService.getAllUser();
        return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(users, User.class);
    }

    /**
     * 保存用户
     *
     * @param request 请求
     * @return {@code Mono<ServerResponse>}
     */
    public Mono<ServerResponse> saveUser(ServerRequest request) {
        //得到user对象
        Mono<User> userMono = request.bodyToMono(User.class);
        return ServerResponse.ok().build(this.userService.addUser(userMono));
    }
}
