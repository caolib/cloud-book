package com.clb.service.Impl;

import com.clb.common.constant.Common;
import com.clb.common.constant.Excep;
import com.clb.common.constant.Jwt;
import com.clb.common.domain.Result;
import com.clb.common.domain.dto.LoginDto;
import com.clb.common.domain.entity.Admin;
import com.clb.common.domain.vo.AdminVo;
import com.clb.mapper.AdminMapper;
import com.clb.service.AdminService;
import com.clb.util.JwtUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final AdminMapper adminMapper;

    private final StringRedisTemplate redisTemplate;

    public AdminServiceImpl(AdminMapper adminMapper, StringRedisTemplate redisTemplate) {
        this.adminMapper = adminMapper;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public Result<AdminVo> login(LoginDto admin) {
        Admin a = adminMapper.selectByUsername(admin.getUsername());
        //用户不存在
        if (a == null) {
            return Result.error(Excep.USER_NOT_EXIST);
        }
        String pwd = a.getPassword();

        //密码错误
        if (!Objects.equals(pwd, admin.getPassword())) {
            return Result.error(Excep.WRONG_PASSWORD);
        }

        //生成令牌
        Map<String, Object> claims = new HashMap<>();
        claims.put(Common.ID, a.getId().toString());
        claims.put(Common.USERNAME, a.getUsername());
        claims.put(Common.IDENTITY, Common.ADMIN);
        String token = JwtUtils.generateJwt(claims);

        // 将令牌保存到redis中
        redisTemplate.opsForValue().set(token, token, Jwt.EXPIRE_TIME, TimeUnit.MILLISECONDS);

        // 封装信息
        AdminVo adminVo = new AdminVo();
        BeanUtils.copyProperties(a, adminVo);
        adminVo.setToken(token);

        log.debug("a:{}", a);

        return Result.success(adminVo);
    }


    @Override
    public Result<String> register(Admin admin) {
        // 查询用户名是否已经存在
        Admin a = adminMapper.selectByUsername(admin.getUsername());
        if (a != null) {
            return Result.error(Excep.USER_ALREADY_EXIST);
        }
        adminMapper.insert(admin);

        return Result.success();
    }

    @Override
    public Result<String> updateById(Admin admin) {

        adminMapper.updateById(admin);

        return Result.success();
    }
}
