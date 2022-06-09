# JSR303数据校验

## 1.概述

### 1.1.什么是JSR 303 ?

JSR-303 是JAVA EE 6 中的一项子规范，叫做Bean Validation，Hibernate Validator 是 Bean Validation 的参考实现 . Hibernate Validator 提供了 JSR 303 规范中所有内置 constraint 的实现，除此之外还有一些附加的 constraint。

JSR 是 Java Specification Requests 的缩写，即 Java 规范提案。
存在各种各样的 JSR，简单的理解为 JSR 是一种 Java 标准。
JSR 303 就是数据检验的一个标准（Bean Validation (JSR 303)）。

### 1.2.我们为什么要使用JSR 303？

我相信大家平时在开发的时候一定会对数据做必要的参数校验，如果我们不做参数校验那么系统很可能产生空指针异常，这并不是我们所希望看见的。

前端可以通过 js 程序校验数据是否合法，后端同样也需要进行校验。而后端最简单的实现就是直接在业务方法中对数据进行处理，但是不同的业务方法可能会出现同样的校验操作，这样就出现了数据的冗余。并且，传统的校验方式还会使我们的代码出现很多`if else` ,有的代码甚至会嵌套十几层，这样的代码的可读性大家想必也不用我多说了。

那么，我们要如何优雅的进行参数校验呢？`JSR 303` 就是这样一种规范，让我们可以在对象或者参数上加上注解，进行参数校验，这样既简化了代码，又提高了代码的可读性。

## 2.快速开始

上面说了那么多，可能大家还是对于JSR 303并没有一个直观的感受，接下来我就带大家通过代码的方式来感受`JSR 303` 带给我们的便利。

### 2.1.构建项目

我们先通过Idea构建一个基本的 `Spring Boot` 项目

1. 点击idea右上角新建

![image-20220608215304304](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220608215304304.png)

2. 选择项目的GAV

![image-20220608215437278](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220608215437278.png)

3. 勾选上常用的框架支持

![image-20220608215524055](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220608215524055.png)

OK，新建项目成功，等待Maven项目建立

### 2.2.引入依赖

导入`JSR 303` 依赖，springboot对于jsr有自己的starter，我们直接导入就行了

```xml
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```

### 2.3.不使用JSR 303

1. 先创建通用结果集进行返回

```java
package cuit.epoch.jsr303.result;


import cuit.epoch.jsr303.constant.ResultEnum;

import java.io.Serializable;

/**
 * @author Pymjl
 * @date 2022/2/26 18:41
 */
public class Result<T> implements Serializable {
    private T result;
    private Boolean succeed;
    private String message;

    public Result() {
    }

    public Result(ResultEnum resultEnum) {
        this.result = null;
        this.succeed = resultEnum.getSucceed();
        this.message = resultEnum.getMsg();
    }

    public Result(T result, ResultEnum resultEnum) {
        this.result = result;
        this.succeed = resultEnum.getSucceed();
        this.message = resultEnum.getMsg();
    }

    public Result(T result, Boolean succeed, String message) {
        this.result = result;
        this.succeed = succeed;
        this.message = message;
    }

    public Result(Boolean succeed, String message) {
        this.result = null;
        this.succeed = succeed;
        this.message = message;
    }

    public T getResult() {
        return result;
    }

    public void setResult(T result) {
        this.result = result;
    }

    public Boolean getSucceed() {
        return succeed;
    }

    public void setSucceed(Boolean succeed) {
        this.succeed = succeed;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return "Result{" +
                "result=" + result +
                ", succeed=" + succeed +
                ", message='" + message + '\'' +
                '}';
    }
}

```

2. 再创建一个工具类，用于对结果进行封装处理

```java
package cuit.epoch.jsr303.result;


import cuit.epoch.jsr303.constant.ResultEnum;

/**
 * @author Pymjl
 * @date 2022/2/26 18:44
 */
public class ResultUtil {
    public static Result<String> success() {
        return new Result<>(ResultEnum.OK);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data, ResultEnum.OK);
    }

    public static Result<String> success(Boolean succeed, String message) {
        return new Result<>(succeed, message);
    }

    public static Result<String> success(ResultEnum resultEnum) {
        return new Result<>(resultEnum);
    }

    public static Result<String> fail() {
        return new Result<>(ResultEnum.UNKNOWN_MISTAKE);
    }

    public static Result<String> fail(String message) {
        return new Result<>(false, message);
    }

    public static <T> Result<T> fail(T data) {
        return new Result<>(data, ResultEnum.UNKNOWN_MISTAKE);
    }
}

```

3. 自定义一个异常`AppException`

```java
package cuit.epoch.jsr303.exception;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/5/15 0:07
 **/
public class AppException extends RuntimeException {
    public AppException(String message, Throwable cause) {
        super(message, cause);
    }

    public AppException(String message) {
        super(message);
    }
}

```

4. 创建一个全局异常捕捉的handler

```java
package cuit.epoch.jsr303.handler;


import cuit.epoch.jsr303.exception.AppException;
import cuit.epoch.jsr303.result.Result;
import cuit.epoch.jsr303.result.ResultUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Set;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/4/29 11:07
 **/
@RestControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(Exception.class)
    public Result<String> error(Exception e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultUtil.fail();
    }

    @ExceptionHandler(AppException.class)
    public Result<String> error(AppException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return ResultUtil.fail(e.getMessage());
    }
}

```

5. 创建一个UserController用于测试

```java
package cuit.epoch.jsr303.controller;

import cuit.epoch.jsr303.exception.AppException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Pymjl
 * @version 1.0
 * @date 2022/6/8 21:27
 **/
@RestController
public class UserController {
    @GetMapping("/test")
    public String test(@RequestParam String username) {
        if (username == null || username.length() == 0) {
            throw new AppException("username is Null");
        }
        return "hello " + username;
    }
}

```

通过代码可知，我们这里先并没有使用`JSR 303` ，而是通过if语句判断，这也是我们大多数人在项目开发中经常用到的校验方式。我们先把项目跑起来，用`Apifox` 进行测试。得到的结果如图所示：

![image-20220608221316109](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220608221316109.png)

大家或许感觉这样写没毛病，就是一个if判断的问题而已，但是如果我们接受的数据是多个呢？就比如这样

我们现在有一个userDTO

```java
package cuit.epoch.jsr303.entity.dto;


import lombok.Data;

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
    private String username;
    /**
     * 密码
     */
    private String password;
    /**
     * 验证代码
     */
    private String verifyCode;
}

```

现在我们需要在业务中对其进行校验，验证里面的参数不能为空，并且密码的长度要大于6，如果使用if判断我们可能需要这样写

```java
    @PostMapping("/login")
    public Result<String> login(@RequestBody UserDTO userDTO) {
        String username = userDTO.getUsername();
        String password = userDTO.getPassword();
        String verifyCode = userDTO.getVerifyCode();

        if (username == null || username.length() == 0 ||
                password == null || password.length() == 0 ||
                verifyCode == null || verifyCode.length() == 0) {
            throw new AppException("参数异常");
        }
        /*
        业务逻辑
         */
        return ResultUtil.success();
    }
```

如果你觉得这样还是可以接受的话，那我现在再改一下需求，**现在规定username是邮箱，要求你验证username参数的格式是否为邮箱，验证密码和验证码不能为空字符串，并且要分别打印对应的错误信息。**

如果改成这样还使用`if` ` else if` 来判断的话那代码量就直接上去了，接下来我来给大家介绍如何使用` JSR 303`来对参数进行优雅的校验。

### 2.4.使用JSR 303进行参数校验

我们接下来使用JSR 303来实现上面的需求。

其实使用JSR 303很简单，我们只需要在DTO的参数上加上对应的注解就行了，常用的注解如下：

```txt
空检查 
@Null 验证对象是否为null 
@NotNull 验证对象是否不为null, 无法查检长度为0的字符串 
@NotBlank 检查约束字符串是不是Null还有被Trim的长度是否大于0,只对字符串,且会去掉前后空格. 
@NotEmpty 检查约束元素是否为NULL或者是EMPTY.

Booelan检查 
@AssertTrue 验证 Boolean 对象是否为 true 
@AssertFalse 验证 Boolean 对象是否为 false

长度检查 
@Size(min=, max=) 验证对象（Array,Collection,Map,String）长度是否在给定的范围之内 
@Length(min=, max=) Validates that the annotated string is between min and max included.

日期检查 
@Past 验证 Date 和 Calendar 对象是否在当前时间之前，验证成立的话被注释的元素一定是一个过去的日期 
@Future 验证 Date 和 Calendar 对象是否在当前时间之后 ，验证成立的话被注释的元素一定是一个将来的日期 
@Pattern 验证 String 对象是否符合正则表达式的规则，被注释的元素符合制定的正则表达式，regexp:正则表达式 flags: 指定 Pattern.Flag 的数组，表示正则表达式的相关选项。

数值检查 
建议使用在Stirng,Integer类型，不建议使用在int类型上，因为表单值为“”时无法转换为int，但可以转换为Stirng为”“,Integer为null 
@Min 验证 Number 和 String 对象是否大等于指定的值 
@Max 验证 Number 和 String 对象是否小等于指定的值 
@DecimalMax 被标注的值必须不大于约束中指定的最大值. 这个约束的参数是一个通过BigDecimal定义的最大值的字符串表示.小数存在精度 
@DecimalMin 被标注的值必须不小于约束中指定的最小值. 这个约束的参数是一个通过BigDecimal定义的最小值的字符串表示.小数存在精度 
@Digits 验证 Number 和 String 的构成是否合法 
@Digits(integer=,fraction=) 验证字符串是否是符合指定格式的数字，interger指定整数精度，fraction指定小数精度。 
@Range(min=, max=) 被指定的元素必须在合适的范围内 
@Range(min=10000,max=50000,message=”range.bean.wage”) 
@Valid 递归的对关联对象进行校验, 如果关联对象是个集合或者数组,那么对其中的元素进行递归校验,如果是一个map,则对其中的值部分进行校验.(是否进行递归验证) 
@CreditCardNumber信用卡验证 
@Email 验证是否是邮件地址，如果为null,不进行验证，算通过验证。 
@ScriptAssert(lang= ,script=, alias=) 
@URL(protocol=,host=, port=,regexp=, flags=)
```

1. 根据上面的提示，我们对userDTO做一个修改

```java
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
    //注意，验证邮箱时一定先对其进行判空验证，@Email这个注解如果该值为空就会失效
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

```

2. 更改controller中的login方法

```java
    @PostMapping("/login")
	//这里一定记得在@RequestBody前面加上@Valid注解，否则就不生效
    public Result<String> login(@Valid @RequestBody UserDTO userDTO) {
        System.out.println(userDTO);
        /*
        业务逻辑
         */
        return ResultUtil.success();
    }

```

3. 接下来我们使用`Apifox`进行测试

```json
{
    "username": "wewqeq",
    "password": "cu",
    "verifyCode": ""
}
```

返回的结果如下：

```json
{
    "result": null,
    "succeed": false,
    "message": "发生了未知错误，请联系管理员或稍后重试"
}
```

我们来看一下控制台打印的异常信息：

```java
Validation failed for argument [0] in public cuit.epoch.jsr303.result.Result<java.lang.String> cuit.epoch.jsr303.controller.UserController.login(cuit.epoch.jsr303.entity.dto.UserDTO) with 3 errors: [Field error in object 'userDTO' on field 'password': rejected value [cu]; codes [Length.userDTO.password,Length.password,Length.java.lang.String,Length]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userDTO.password,password]; arguments []; default message [password],255,6]; default message [密码参数异常，密码必须大于等于六个字符]] [Field error in object 'userDTO' on field 'username': rejected value [wewqeq]; codes [Email.userDTO.username,Email.username,Email.java.lang.String,Email]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userDTO.username,username]; arguments []; default message [username],[Ljavax.validation.constraints.Pattern$Flag;@425b97d4,.*]; default message [用户名格式不正确，用户名必须为邮箱]] [Field error in object 'userDTO' on field 'verifyCode': rejected value []; codes [NotBlank.userDTO.verifyCode,NotBlank.verifyCode,NotBlank.java.lang.String,NotBlank]; arguments [org.springframework.context.support.DefaultMessageSourceResolvable: codes [userDTO.verifyCode,verifyCode]; arguments []; default message [verifyCode]]; default message [验证码不能为空]]
```

这里我们可以看到，虽然我们的校验生效了，但是这个异常信息真的是太丑了，也不方便我们阅读，那么我们要怎么来优雅的处理这个异常呢？

### 2.5.优雅的处理异常

这里我们选用比较常用的全局异常处理的方式

在之前的`GlobalExceptionHandler` 类中添加对`ConstraintViolationException` `MethodArgumentNotValidException` `BindException` 等异常捕捉，并对异常信息进行特殊处理。

```java
	@ExceptionHandler(ConstraintViolationException.class)
    public Result<String> error(ConstraintViolationException e) {
        log.error(e.getMessage());
        e.printStackTrace();
        Set<ConstraintViolation<?>> constraintViolations = e.getConstraintViolations();
        StringBuilder res = new StringBuilder("参数异常: ");
        constraintViolations.forEach(c -> res.append(c.getMessage()).append(" "));
        return ResultUtil.fail(res.toString().trim());
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class, BindException.class})
    public Result<String> argumentError(Exception e) {
        e.printStackTrace();
        BindingResult bindingResult = null;
        if (e instanceof MethodArgumentNotValidException) {
            bindingResult = ((MethodArgumentNotValidException) e).getBindingResult();
        } else if (e instanceof BindException) {
            bindingResult = ((BindException) e).getBindingResult();
        }
        StringBuilder msg = new StringBuilder();
        assert bindingResult != null;
        bindingResult.getFieldErrors().forEach((fieldError) ->
                msg.append(fieldError.getDefaultMessage()).append(" ")
        );
        log.error(msg);
        return ResultUtil.fail(msg.toString().trim());
    }
```

接下来我们对添加异常处理后的接口进行测试：

```json
{
    "username": "张静",
    "password": "qui",
    "verifyCode": ""
}
```

返回结果如图：

![image-20220609183523581](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220609183523581.png)

除了在DTO类上使用注解，我们还可以在方法参数上使用

我们接下来看这个例子：

```java
    @GetMapping("/test")
    public String test(@NotBlank(message = "username不能为空") @RequestParam String username) {
        return "hello " + username;
    }
```

**注意，使用该方法对接收的参数进行校验时一定记得给controller上面加上`@Validated`注解**

接下来使用apifox进行测试

![image-20220609184017021](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220609184017021.png)

控制台打印信息：

![image-20220609184040935](https://pymjl.oss-cn-shanghai.aliyuncs.com/picgo/image-20220609184040935.png)

## 3.小结

`JSR 303`能够帮助我们从繁琐的`if` `else if`的陷进中跳出来 ，能够极大的减少我们的代码量，也可以让我们更优雅的进行鉴权。平时大家在写项目的时候应该注意多使用注解的方式来检验数据，而不是if else，更不能参数校验都不做。

好啦，我的介绍到这里就结束了。快于这次代码演示的测试样例都可以在我的仓库里面找到：[gitee](https://gitee.com/pymjl_0/blog-code-demo)

