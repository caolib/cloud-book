package com.clb.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clb.clients.BorrowClient;
import com.clb.common.constant.Excep;
import com.clb.common.domain.Borrow;
import com.clb.common.domain.Result;
import com.clb.common.domain.dto.Condition;
import com.clb.common.domain.entity.Book;
import com.clb.mapper.BookMapper;
import com.clb.service.BookService;
import com.clb.util.MyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {
    private final BookMapper bookMapper;
    private final BorrowClient borrowClient;

    @Override
    public Page<Book> getBookPage(Condition condition) {
        // 取出参数
        String isbn = condition.getIsbn();
        String bookName = condition.getBookName();
        String author = condition.getAuthor();
        Integer currentPage = condition.getCurrentPage();
        Integer pageSize = condition.getPageSize();

        LambdaQueryWrapper<Book> queryWrapper = new LambdaQueryWrapper<>();
        // 根据搜索条件查询
        queryWrapper.eq(MyUtils.notNull(isbn), Book::getIsbn, isbn)
                .like(MyUtils.notNull(bookName), Book::getTitle, bookName)
                .like(MyUtils.notNull(author), Book::getAuthor, author);
        // 分页查询
        return bookMapper.selectPage(new Page<>(currentPage, pageSize), queryWrapper);
    }

    @Override
    public Result<String> deleteBookByIsbn(String isbn) {
        // 查询isbn是否存在
        Book book = bookMapper.selectById(isbn);
        if (book == null) {
            String msg = Excep.ISBN_NOT_EXIST;
            log.error(msg);
            return Result.error(msg);
        }

        // 先查询借阅表中是否有记录，有记录就不允许删除
        List<Borrow> borrows = borrowClient.getBorrowByIsbn(isbn);
        if (!borrows.isEmpty()) {
            String msg = Excep.DELETE_BOOK_NOT_ALLOW;
            log.error(msg);
            return Result.error(msg);
        }

        bookMapper.deleteById(isbn);
        return Result.success();
    }

    @Override
    public Result<String> add(Book book) {
        String isbn = book.getIsbn();
        String title = book.getTitle();
        Integer number = book.getNumber();
        //isbn不能为空
        if (!MyUtils.notNull(isbn)) {
            String msg = Excep.ISBN_IS_NULL;
            log.error(msg);
            return Result.error(msg);
        }
        //书名不能为空
        if (!MyUtils.notNull(title)) {
            String msg = Excep.TITLE_IS_NULL;
            log.error(msg);
            return Result.error(msg);
        }
        // 库存量大于等于0
        if (number == null || number < 0) {
            String msg = Excep.BOOK_NUMBER_ERROR;
            log.error(msg);
            return Result.error(msg);
        }

        // 查找isbn是否存在
        Long l = bookMapper.getByIsbn(isbn);
        if (l > 0) {
            String msg = Excep.ISBN_ALREADY_EXIST;
            log.error(msg);
            return Result.error(msg);
        }

        bookMapper.insert(book);

        return Result.success();
    }

    @Override
    public Result<String> updateBook(Book book) {
        String isbn = book.getIsbn();
        //查询isbn是否存在
        Book b = bookMapper.selectById(isbn);
        if (b == null) {
            String msg = Excep.ISBN_NOT_EXIST;
            log.error(msg);
            return Result.error(msg);
        }

        if (book.getNumber() < 0) {
            String msg = Excep.BOOK_NUMBER_ERROR;
            log.error(msg);
            return Result.error(msg);
        }
        bookMapper.updateById(book);

        return Result.success();
    }
}
