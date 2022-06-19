package cuit.pymjl.webfluxdemo;

import cuit.pymjl.webfluxdemo.handler.UserHandler;
import cuit.pymjl.webfluxdemo.service.UserService;
import cuit.pymjl.webfluxdemo.service.impl.UserServiceImpl;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.HttpHandler;
import org.springframework.http.server.reactive.ReactorHttpHandlerAdapter;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.netty.http.server.HttpServer;

import java.io.IOException;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.toHttpHandler;


/**
 * 函数式编程
 *
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/19 18:24
 **/
public class FunctionServer {
    /**
     * 创建路由
     *
     * @return {@code RouterFunction<ServerResponse>}
     */
    public RouterFunction<ServerResponse> routingFunction() {
        //创建hanler对象
        UserService userService = new UserServiceImpl();
        UserHandler handler = new UserHandler(userService);
        //设置路由
        return RouterFunctions.route(GET("/function/user/{id}").and(accept(MediaType.APPLICATION_JSON)), handler::getUserById)
                .andRoute(GET("/function/users").and(accept(MediaType.APPLICATION_JSON)), handler::getAllUsers);
    }

    public void createReactorServer() {
        //路由和handler适配
        RouterFunction<ServerResponse> route = routingFunction();
        HttpHandler handler = toHttpHandler(route);
        ReactorHttpHandlerAdapter adapter = new ReactorHttpHandlerAdapter(handler);
        //创建服务器
        HttpServer.create().host("127.0.0.1").port(8081).handle(adapter).bind().block();
    }

    public static void main(String[] args) throws IOException {
        FunctionServer server = new FunctionServer();
        server.createReactorServer();
        System.out.println("server start up successfully");
        System.out.println("press enter to exit");
        System.in.read();
    }
}
