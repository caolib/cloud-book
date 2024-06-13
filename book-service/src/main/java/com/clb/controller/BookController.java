package com.clb.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.clb.common.constant.Cache;
import com.clb.common.domain.PageResult;
import com.clb.common.domain.Result;
import com.clb.common.domain.dto.Condition;
import com.clb.common.domain.entity.Book;
import com.clb.mapper.BookMapper;
import com.clb.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Pattern;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/book")
@RequiredArgsConstructor
@Validated
public class BookController {
    private final BookService bookService;
    private final BookMapper bookMapper;

    /**
     * 书籍信息的分页查询
     *
     * @param condition 查询条件，包含书名、作者和isbn及分页条件
     */
    @PostMapping
    @Cacheable(cacheNames = Cache.BOOK_PAGE)
    public PageResult<List<Book>> getBookPage(@RequestBody Condition condition) {
        log.debug("查询条件:{}", condition);

        Page<Book> bookPage = bookService.getBookPage(condition);

        List<Book> data = bookPage.getRecords();
        Long total = bookPage.getTotal();

        return PageResult.success(data, total);
    }

    /**
     * 根据isbn删除书籍信息
     *
     * @param isbn 书号
     */
    @CacheEvict(value = Cache.BOOK_PAGE, allEntries = true)
    @DeleteMapping("/{isbn}")
    public Result<String> deleteBookByIsbn(@PathVariable @Pattern(regexp = "^\\S{1,20}$") String isbn) {
        log.debug("isbn:{}", isbn);

        bookService.deleteBookByIsbn(isbn);
        return Result.success();
    }

    /**
     * 添加图书
     *
     * @param book 图书信息
     */
    @PostMapping("/add")
    @CacheEvict(value = Cache.BOOK_PAGE, allEntries = true)
    public Result<String> addBook(@RequestBody Book book) {
        log.debug("book:{}", book);

        return bookService.add(book);
    }

    /**
     * 根据isbn更新图书信息
     *
     * @param book 更新后图书信息
     */
    @PutMapping
    @CacheEvict(value = Cache.BOOK_PAGE, allEntries = true)
    public Result<String> updateBook(@RequestBody Book book) {
        log.debug("book:{}", book);

        return bookService.updateBook(book);
    }

    /**
     * 根据isbn更新图书库存
     *
     * @param isbn 书号
     * @param num  库存量
     */
    @PutMapping("/{isbn}/{num}")
    Result<String> updateNumByIsbn(@PathVariable String isbn, @PathVariable Integer num) {
        bookMapper.updateNumberByIsbn(isbn, num);
        return Result.success();
    }
}
