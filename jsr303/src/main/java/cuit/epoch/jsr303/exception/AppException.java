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
