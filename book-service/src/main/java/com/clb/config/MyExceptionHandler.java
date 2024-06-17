package com.clb.config;


import com.clb.common.constant.Excep;
import com.clb.common.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {

    // 参数类型非法
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<String> handleException(HttpMessageNotReadableException e) {
        log.error("异常捕获:{}", e.getMessage());
        return Result.error(Excep.ARG_TYPE_ILLEGAL);
    }

}
