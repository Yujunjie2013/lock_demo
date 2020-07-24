package com.junjie.lock.lock_demo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 全局异常处理
 */
@CrossOrigin(origins = "*", maxAge = 3600)
@ControllerAdvice
public class GlobalExceptionHandler {
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public String jsonErrorHandler(Exception e, HttpServletRequest httpServletRequest) {
        return "服务打了个盹，请稍后再试";
    }

    /**
     * 自定义异常
     *
     * @param e
     * @return
     */
    @ResponseStatus(HttpStatus.OK)
    @ExceptionHandler(value = RequestLimitException.class)
    @ResponseBody
    public String invalidErrorHandler(RequestLimitException e, HttpServletRequest httpServletRequest) {
        return e.getMessage();
    }

}
