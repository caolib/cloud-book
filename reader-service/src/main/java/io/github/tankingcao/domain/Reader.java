package io.github.tankingcao.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName reader
 */
@TableName(value ="reader")
@Data
public class Reader implements Serializable {
    @TableId
    private Integer id;

    private String username;

    private String password;

    private String nickname;

    private String gender;

    private Integer age;

    private String tel;

    private static final long serialVersionUID = 1L;
}