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
