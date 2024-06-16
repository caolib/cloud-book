package com.clb.util;

import com.clb.common.constant.Common;
import com.clb.common.constant.Excep;
import com.clb.common.domain.Result;
import com.clb.common.domain.dto.UserDto;
import com.clb.common.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Aspect
@Slf4j
public class Advice {
    //匹配controller包下所有方法,返回值任意
    @Pointcut("execution(* com.clb.controller.*.*(..))")
    public void pointcut() {
    }

    //记录方法执行耗时ms
    @Around("pointcut()")
    public Object logAdvice(ProceedingJoinPoint joinPoint) {
        long start = System.currentTimeMillis();
        Object result;
        String name = joinPoint.getSignature().toString();
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
        long time = System.currentTimeMillis() - start;

        log.debug("[耗时:{}ms {}]", time, name);

        return result;
    }

    @Pointcut("execution(* com.clb.controller.*.deleteBookByIsbn(..)) " +
            "|| execution(* com.clb.controller.*.addBook(..))")
    public void identity() {
    }

    //校验执行人身份合法性
    @Around("identity()")
    public Object identityAdvice(ProceedingJoinPoint joinPoint) {

        // 判断用户身份是否为管理员
        UserDto user = ThreadLocalUtil.get();
        if (user == null || !Objects.equals(user.getIdentity(), Common.ADMIN)) {
            String msg = Excep.IDENTITY;
            log.error(msg);
            return Result.error(msg);
        }

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }

        return result;
    }
}
