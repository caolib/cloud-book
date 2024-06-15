package com.clb.interceptor;

import com.alibaba.fastjson.JSON;
import com.clb.constant.Common;
import com.clb.domain.Excep;
import com.clb.domain.Reader;
import com.clb.exception.TokenException;
import com.clb.util.JwtUtils;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenInterceptor implements GlobalFilter, Ordered {
    private final StringRedisTemplate redisTemplate;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        // 不对/login和/register接口校验
        if (path.endsWith("/login") || path.endsWith("/register")) {
            log.debug("登录注册接口，直接跳过...");
            return chain.filter(exchange);
        }

        String token = exchange.getRequest().getHeaders().getFirst(Common.TOKEN);
        log.debug("token:{}", token);
        // 如果token为空，拦截请求，返回状态码401
        if (token == null || token.isEmpty()) {
            log.error("token为空");
            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
            return exchange.getResponse().setComplete();
        }

        // 校验token是否过期
        try {
            String redisToken = redisTemplate.opsForValue().get(token);
            // 如果redis中没有对应的key，抛出异常
            if (redisToken == null) {
                throw new TokenException(Excep.TOKEN_ALREADY_EXPIRED);
            }
        } catch (Exception e) {
            // 身份过期
            exchange.getResponse().setRawStatusCode(499);
            return exchange.getResponse().setComplete();
        }

        // 解析令牌
        Claims claims = JwtUtils.parseJWT(token);
        String id = claims.get(Common.ID, String.class);
        String username = claims.get(Common.USERNAME, String.class);
        String identity = claims.get(Common.IDENTITY, String.class);
        log.debug("id:{},username:{},identity:{}", id, username, identity);

        Reader reader = Reader.builder().id(id).username(username).build();

        // 将令牌解析后的用户信息保存到请求头中，继续传给后面的服务使用
        exchange.mutate().request(consumer -> consumer.header("user", JSON.toJSONString(reader))).build();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
