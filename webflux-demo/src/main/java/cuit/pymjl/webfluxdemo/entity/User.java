package cuit.pymjl.webfluxdemo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/19 17:23
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private String username;
    private String gender;
    private Integer age;
}
