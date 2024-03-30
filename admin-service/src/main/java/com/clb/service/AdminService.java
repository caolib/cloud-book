package com.clb.service;


import com.clb.common.domain.Result;
import com.clb.common.domain.dto.LoginDto;
import com.clb.common.domain.entity.Admin;
import com.clb.common.domain.vo.AdminVo;

public interface AdminService {

    Result<AdminVo> login(LoginDto admin);

    Result<String> register(Admin admin);

    Result<String> updateById(Admin admin);
}
