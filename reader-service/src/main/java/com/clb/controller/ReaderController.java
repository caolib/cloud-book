package com.clb.controller;

import com.clb.common.constant.Cache;
import com.clb.common.domain.Result;
import com.clb.common.domain.dto.LoginDto;
import com.clb.common.domain.entity.Reader;
import com.clb.common.domain.vo.ReaderVo;
import com.clb.common.utils.ThreadLocalUtil;
import com.clb.config.ConfigProperties;
import com.clb.service.ReaderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Validated
@Slf4j
@RestController
@RequiredArgsConstructor
@RefreshScope
@RequestMapping("/reader")
public class ReaderController {
    private final ReaderService readerService;
    private final ConfigProperties properties;

    /**
     * 查询所有用户信息
     */
    @PostMapping
    @Cacheable(cacheNames = Cache.READER, key = "#condition")
    public Result<List<Reader>> getAllReader(@RequestBody Reader condition) {

        log.debug("condition:{}", condition);

        return readerService.getAllReader(condition);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update")
    @CacheEvict(value = Cache.READER, allEntries = true)
    public Result<Reader> updateReader(@RequestBody @Validated Reader reader) {
        log.info("reader:{}", reader);
        return readerService.updateReader(reader);
    }

    /**
     * 根据id删除用户
     */
    @DeleteMapping("/{id}")
    @CacheEvict(value = Cache.READER, allEntries = true)
    public Result<String> deleteById(@PathVariable Integer id) {
        log.debug("id:{}", id);

        ThreadLocalUtil.get();


        return readerService.deleteById(id);
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public Result<ReaderVo> login(@RequestBody LoginDto reader) {
        return readerService.login(reader);
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody Reader reader) {
        return readerService.register(reader);
    }


    @GetMapping("/config")
    public Result<String>config() {
        String name = properties.getName();
        log.debug(name);
        return Result.success(name);
    }

}
