package com.clb.hanle;

import com.clb.domain.Result;
import com.clb.exception.TokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {
    @ExceptionHandler(TokenException.class)
    public Result<String> exceptionHandler(TokenException ex) {
        log.error("异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }
}
