package io.github.tankingcao.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import io.github.tankingcao.domain.Book;
import io.github.tankingcao.service.BookService;
import io.github.tankingcao.mapper.BookMapper;
import org.springframework.stereotype.Service;

/**
* @author 木杉
* @description 针对表【book】的数据库操作Service实现
* @createDate 2024-03-16 11:39:31
*/
@Service
public class BookServiceImpl extends ServiceImpl<BookMapper, Book>
    implements BookService{

}




