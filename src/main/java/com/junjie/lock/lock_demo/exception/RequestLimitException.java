package com.junjie.lock.lock_demo.exception;

/**
 * 定义超过并发数异常
 */
public class RequestLimitException extends RuntimeException {
    public RequestLimitException(String message) {
        super(message);
    }

    public RequestLimitException() {
    }
}
