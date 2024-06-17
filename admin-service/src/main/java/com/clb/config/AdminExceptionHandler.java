package com.clb.config;

import com.clb.common.domain.Result;
import com.clb.common.exception.BaseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class AdminExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public Result<String> handleBaseException(BaseException e) {
        log.error("业务异常:{}", e.getMessage());
        return Result.error(e.getMessage());
    }

}
