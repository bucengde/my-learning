package com.learning.vps.global;

import com.learning.commons.bean.request.UnionResponse;
import com.learning.commons.exception.RuntimeServerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 全局异常处理器
 * @author Wang Xu
 * @date 2020/10/21
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler({RuntimeServerException.class})
    public ResponseEntity<?> runtimeServerExceptionHandler(RuntimeServerException e) {
        LOGGER.warn("系统异常！ msg: {}", e.toString(), e);

        return new ResponseEntity<>(UnionResponse.fail(e.getMessage()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> exceptionHandler(Exception e) {
        LOGGER.warn("系统错误！ msg: {}", e.toString(), e);
        return new ResponseEntity<>(UnionResponse.fail(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase()), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}