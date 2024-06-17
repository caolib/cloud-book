package com.clb.common.handle;


import com.clb.common.constant.Excep;
import com.clb.common.domain.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    // 参数校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(Excep.ARG_NOT_VALID + "{}", e.getMessage());
        return Result.error(Excep.ARG_NOT_VALID);
    }

    /**
     * 完整性约束冲突异常
     */
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public Result<String> exceptionHandler(SQLIntegrityConstraintViolationException ex) {
        String message = ex.getMessage();
        // 如果借阅记录中有读者或图书信息，那么读者和图书都不能被删除
        if (message.contains("book")) {
            log.error(message);
            return Result.error(Excep.DELETE_BOOK_NOT_ALLOW);
        } else if (message.contains("reader")) {
            log.error(message);
            return Result.error(Excep.DELETE_READER_NOT_ALLOW);
        } else {
            log.error(message);
            return Result.error(Excep.UNKNOWN_ERROR);
        }
    }

    /**
     * 读者和图书删除异常
     */
    @ExceptionHandler(Exception.class)
    public Result<String> handleException(Exception e) {
        String msg = e.getMessage();
        if (msg.contains("Unable to connect to Redis")) {
            log.error("无法连接redis:{}", msg);
            return Result.error("数据库繁忙!");
        } else if (msg.contains("borrow")) {
            String errorMsg = "";
            if (msg.contains("book")) {
                errorMsg = Excep.DELETE_BOOK_NOT_ALLOW;
            } else if (msg.contains("reader")) {
                errorMsg = Excep.DELETE_READER_NOT_ALLOW;
            }
            log.error("删除失败:{}", errorMsg);
            return Result.error(errorMsg);
        }
        log.error("未知异常:{}", msg);
        return Result.error("未知异常!");
    }

}
