package cuit.pymjl.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/7/2 12:32
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Student implements Serializable {
    @Serial
    private static final long serialVersionUID = -91809837793898L;

    private String name;
    private String password;
    private int age;
    private String address;
    private String phone;
}
