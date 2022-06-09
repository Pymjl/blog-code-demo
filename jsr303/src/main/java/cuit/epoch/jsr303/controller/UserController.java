package cuit.epoch.jsr303.controller;

import cuit.epoch.jsr303.entity.dto.UserDTO;
import cuit.epoch.jsr303.result.Result;
import cuit.epoch.jsr303.result.ResultUtil;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/8 21:27
 **/
@RestController
@Validated
public class UserController {

    @GetMapping("/hello")
    public String sayHello() {
        return "hello";
    }

    @GetMapping("/test")
    public String test(@NotBlank(message = "username不能为空") @RequestParam String username) {
        return "hello " + username;
    }

    @PostMapping("/login")
    public Result<String> login(@Valid @RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        /*
        业务逻辑
         */
        return ResultUtil.success();
    }
}
