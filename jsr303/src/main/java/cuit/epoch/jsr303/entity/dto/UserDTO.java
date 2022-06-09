package cuit.epoch.jsr303.entity.dto;


import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/8 21:26
 **/
@Data
public class UserDTO {
    /**
     * 用户名
     */
    @NotBlank(message = "用户名不能为null")
    @Email(message = "用户名格式不正确，用户名必须为邮箱")
    private String username;
    /**
     * 密码
     */
    @Length(min = 6, max = 255, message = "密码参数异常，密码必须大于等于六个字符")
    private String password;
    /**
     * 验证代码
     */
    @NotBlank(message = "验证码不能为空")
    private String verifyCode;
}
