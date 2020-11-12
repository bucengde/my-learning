package com.learning.global;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Wang Xu
 * @date 2020/10/4
 */
@ControllerAdvice
public class GlobalExceptionHandle {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandle.class);

    @ExceptionHandler(CodeException.class)
    public <T> ResponseEntity<RespDTO<T>> handle(CodeException e) {
        LOGGER.error("CodeException -- exception code:{}, msg:{}, cause:{}", e.getCode(), e.getMsg(), e.toString(), e);
        return ResponseEntity.ok(RespDTO.fail(e.getMsg()));
    }

    @Order
    @ExceptionHandler(Exception.class)
    public <T> ResponseEntity<RespDTO<T>> handle(Exception e) {
        LOGGER.error("发生未知异常，异常信息为：{}", e.toString(), e);
        return ResponseEntity.ok(RespDTO.fail("系统繁忙，请稍后再试！"));
    }
}