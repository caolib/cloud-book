package com.clb.controller;


import com.clb.common.domain.Result;
import com.clb.common.domain.dto.LoginDto;
import com.clb.common.domain.entity.Admin;
import com.clb.common.domain.vo.AdminVo;
import com.clb.service.AdminService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
@Slf4j
public class AdminController {

    private final AdminService adminService;

    /**
     * 更新管理员信息
     *
     * @param admin 管理员对象
     */
    @PutMapping("/update")
    public Result<String> update(@RequestBody Admin admin) {
        log.info("admin:{}", admin);

        return adminService.updateById(admin);
    }

    /**
     * 管理员登录
     *
     * @param admin 用户名和密码
     * @return 管理员相关信息，包含token
     */
    @PostMapping("/login")
    public Result<AdminVo> login(@RequestBody @Validated LoginDto admin) {
        log.debug("admin:{}", admin);

        return adminService.login(admin);
    }

    /**
     * 管理员注册
     *
     * @param admin 用户名、密码和昵称
     */
    @PostMapping("/register")
    public Result<String> register(@RequestBody @Validated Admin admin) {
        log.debug("admin{}", admin);

        return adminService.register(admin);
    }

}
