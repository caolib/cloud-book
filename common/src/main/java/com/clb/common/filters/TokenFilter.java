package com.clb.common.filters;


import com.alibaba.fastjson.JSON;
import com.clb.common.domain.entity.Reader;
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
    public boolean preHandle(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Object handler) {

        String user = request.getHeader("user");
        Reader reader = JSON.parseObject(user, Reader.class);
        log.debug("reader:{}", reader);

        // 保存到ThreadLocal
        ThreadLocalUtil.set(reader);

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        // 请求完成后清除ThreadLocal中的数据
        ThreadLocalUtil.remove();
    }
}
