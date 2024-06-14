package com.clb.common.handle;


import com.clb.common.constant.Excep;
import com.clb.common.domain.Result;
import com.clb.common.exception.AlreadyExistException;
import com.clb.common.exception.BaseException;
import io.jsonwebtoken.ExpiredJwtException;
import io.lettuce.core.RedisCommandExecutionException;
import io.lettuce.core.RedisConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.SocketException;
import java.sql.SQLIntegrityConstraintViolationException;

/**
 * 全局异常处理器，处理项目中抛出的业务异常
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 捕获业务异常
     */
    @ExceptionHandler(BaseException.class)
    public Result<String> exceptionHandler(BaseException ex) {
        log.error("异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(AlreadyExistException.class)
    public Result<String> exceptionHandler(AlreadyExistException ex) {
        log.error("异常：{}", ex.getMessage());
        return Result.error(ex.getMessage());
    }

    @ExceptionHandler(ExpiredJwtException.class)
    public Result<String> exceptionHandler(ExpiredJwtException ex) {
        log.error(Excep.TOKEN_ALREADY_EXPIRED);
        return Result.error(ex.getMessage());
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
     * redis未开启可能导致的异常
     */
    @ExceptionHandler({SocketException.class, RedisConnectionFailureException.class, RedisConnectionException.class})
    public Result<String> exceptionHandler(SocketException socketException) {
        String message = socketException.getMessage();
        if (message.contains("Connection reset")) {
            log.error("{} redis未连接...", message);
            return Result.error(" redis未连接...");
        }
        log.error("检查redis是否开启 {}", message);
        return Result.error(message + " 检查redis是否连接");
    }

    @ExceptionHandler(RedisCommandExecutionException.class)
    public Result<String> redisCommandException(RedisCommandExecutionException exception) {
        String message = exception.getMessage();
        if (message.contains("no password is set")) {
            log.error("当前redis没有密码，yml中错误设置了密码");
            return Result.error(message);
        } else if (message.contains("invalid password")) {
            log.error("{} redis密码错误", message);
            return Result.error("redis密码错误!");
        } else if (message.contains("NOAUTH HELLO")) {
            log.error("先为redis配置密码 {}", message);
            return Result.error("redis未认证!");
        } else {
            log.error(message);
            return Result.error(Excep.UNKNOWN_ERROR);
        }
    }

    // 参数校验
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<String> MethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(Excep.ARG_NOT_VALID + "{}", e.getMessage());
        return Result.error(Excep.ARG_NOT_VALID);
    }

}
