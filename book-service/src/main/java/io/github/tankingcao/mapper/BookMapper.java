package io.github.tankingcao.mapper;

import io.github.tankingcao.domain.Book;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 木杉
* @description 针对表【book】的数据库操作Mapper
* @createDate 2024-03-16 11:39:31
* @Entity io.github.tankingcao.domain.Book
*/
@Mapper
public interface BookMapper extends BaseMapper<Book> {

}




