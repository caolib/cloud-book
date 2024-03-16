package io.github.tankingcao.domain;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @TableName book
 */
@TableName(value ="book")
@Data
public class Book implements Serializable {
    @TableId
    private String isbn;

    private String title;

    private String cover;

    private String introduction;

    private Integer number;

    private String author;

    private static final long serialVersionUID = 1L;
}