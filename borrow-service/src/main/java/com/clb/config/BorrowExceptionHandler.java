package com.clb.config;

import com.clb.common.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class BorrowExceptionHandler {

    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        log.error("捕获异常:{}", e.getMessage());
        return Result.error(e.getMessage().split(":")[1].trim());
    }
}
