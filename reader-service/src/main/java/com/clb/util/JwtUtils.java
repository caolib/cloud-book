package com.clb.util;


import com.clb.common.constant.Jwt;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;


@Component
@Slf4j
public class JwtUtils {

    /**
     * 生成JWT令牌
     *
     * @param claims JWT第二部分负载 payload 中存储的内容
     */
    public static String generateJwt(Map<String, Object> claims) {
        Date date = new Date(System.currentTimeMillis() + Jwt.EXPIRE_TIME * 60 * 60 * 1000);
        log.debug("过期时间:{}", date);

        return Jwts.builder()
                .addClaims(claims)
                .signWith(SignatureAlgorithm.HS256, Jwt.SIGNKEY)
                .setExpiration(date)
                .compact();
    }
}
