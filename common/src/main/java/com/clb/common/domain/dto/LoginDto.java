package com.clb.common.domain.dto;

import lombok.Data;

import javax.validation.constraints.Pattern;
import java.io.Serializable;

/**
 * 登录的用户名和密码
 */
@Data
public class LoginDto implements Serializable {
    @Pattern(regexp = "^\\S{1,10}$")
    private String username;
    @Pattern(regexp = "^\\S{1,10}$")
    private String password;
}
