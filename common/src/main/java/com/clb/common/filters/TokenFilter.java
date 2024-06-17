package com.clb.common.filters;


import com.alibaba.fastjson.JSON;
import com.clb.common.domain.dto.UserDto;
import com.clb.common.utils.ThreadLocalUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Slf4j
@Component
@ConditionalOnClass(DispatcherServlet.class)
public class TokenFilter implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        String data = request.getHeader("user");
        UserDto user = JSON.parseObject(data, UserDto.class);
        log.debug("user:{}", user);

        // 保存到ThreadLocal
        ThreadLocalUtil.set(user);

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
