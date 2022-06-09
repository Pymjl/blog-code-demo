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
